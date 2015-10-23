package com.plugins.account;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.EditText;

import com.plugins.database.helper.DataRow;
import com.plugins.database.tables.SystemUsers;
import com.webdroid.core.plugins.notify.OAlert;
import com.webdroid.core.utils.BitmapUtils;
import com.webdroid.core.utils.OPreferenceManager;
import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;
import com.webdroid.core.wrapper.helper.utils.WDPluginCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginSignupPlugin extends WDPluginHelper {
    public static final String KEY_ANY_USER = "key_any_user";
    public static final String KEY_CURRENT_USER = "key_current_user";
    public static final String KEY_CURRENT_USER_NAME = "key_current_user_name";
    private OPreferenceManager pref;

    public LoginSignupPlugin(Context context) {
        super(context);
        pref = new OPreferenceManager(context);
    }

    public void authenticate(WDPluginArgs args, WDPluginCallback callback) {
        try {
            JSONObject result = new JSONObject();

            SystemUsers users = new SystemUsers(getContext());
            DataRow user = users.browse(SystemUsers.COL_EMAIL + " = ? and " + SystemUsers.COL_PASSWORD + " = ?",
                    new String[]{args.getString("username"), args.getString("password")});
            if (user != null) {
                result.put("success", true);
                pref.setBoolean(KEY_ANY_USER, true);
                pref.putInt(KEY_CURRENT_USER, user.getInt(BaseColumns._ID));
                pref.putString(KEY_CURRENT_USER_NAME, user.getString(SystemUsers.COL_FULL_NAME));
            } else {
                result.put("success", false);
            }
            callback.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            callback.fail(false);
        }
    }

    public void logout(WDPluginArgs args, WDPluginCallback callback) {
        pref.deleteKey(KEY_ANY_USER);
        pref.deleteKey(KEY_CURRENT_USER);
        pref.deleteKey(KEY_CURRENT_USER_NAME);
        callback.success(true);
    }

    public void signup(WDPluginArgs args, WDPluginCallback callback) {
        try {
            JSONObject result = new JSONObject();
            SystemUsers users = new SystemUsers(getContext());
            ContentValues values = new ContentValues();
            values.put(SystemUsers.COL_FULL_NAME, args.getString("full_name"));
            values.put(SystemUsers.COL_EMAIL, args.getString("username"));
            values.put(SystemUsers.COL_PASSWORD, args.getString("password"));
            int id = users.insert(values);
            result.put("success", (id != -1));
            Log.v("signup", "New user created with id " + id);
            pref.putInt(KEY_CURRENT_USER, id);
            pref.putString(KEY_CURRENT_USER_NAME, args.getString("full_name"));
            pref.setBoolean(KEY_ANY_USER, true);
            callback.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            callback.fail(false);
        }
    }

    public boolean hasAnyUser(WDPluginArgs args) {
        return pref.getBoolean(KEY_ANY_USER, false);
    }

    public Object getUser(WDPluginArgs args) {
        int id = pref.getInt(KEY_CURRENT_USER, -1);
        if (pref.getBoolean(KEY_ANY_USER, false) && id != -1) {
            SystemUsers users = new SystemUsers(getContext());
            return users.browse(id).toJSONObject();
        }
        return false;
    }


    public void getAvailableUsers(WDPluginArgs args, WDPluginCallback callback) {
        JSONArray users = new JSONArray();
        try {
            SystemUsers systemUsers = new SystemUsers(getContext());
            for (DataRow row : systemUsers.select()) {
                Bitmap bitmap = BitmapUtils.getAlphabetImage(getContext(), row.getString("full_name"));
                row.put("image", BitmapUtils.bitmapToBase64(bitmap));
                users.put(row.toJSONObject());
            }
            callback.success(users);
        } catch (Exception e) {
            e.printStackTrace();
            callback.fail(e.getMessage());
        }
    }

    public void requestQuickLogin(WDPluginArgs args, final WDPluginCallback callback) {
        try {
            final JSONObject result = new JSONObject();
            SystemUsers systemUsers = new SystemUsers(getContext());
            final DataRow user = systemUsers.browse(args.getInt("user_id"));
            if (user != null) {
                OAlert.inputDialog(getContext(),
                        "Enter password ", new OAlert.OnUserInputListener() {
                            @Override
                            public void onViewCreated(EditText inputView) {

                            }

                            @Override
                            public void onUserInputted(Object value) {
                                try {
                                    WDPluginArgs userArgs = new WDPluginArgs();
                                    userArgs.put("username", user.getString("email"));
                                    userArgs.put("password", user.getString("password"));
                                    if (user.getString("password").equals(value.toString())) {
                                        authenticate(userArgs, callback);
                                    } else {
                                        result.put("success", false);
                                        callback.fail(result);
                                        OAlert.showError(getContext(), "Invalid password !");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
