package com.plugins.database;

import android.content.ContentValues;
import android.content.Context;

import com.plugins.database.helper.DataRow;
import com.plugins.database.helper.DatabaseCreator;
import com.plugins.database.helper.TableFinder;
import com.plugins.resume.ResumeBuilderPlugin;
import com.webdroid.core.utils.OPreferenceManager;
import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;
import com.webdroid.core.wrapper.helper.utils.WDPluginCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DatabaseManager extends WDPluginHelper {

    private final int OPE_CREATE = 1;
    private final int OPE_UPDATE = 2;
    private final int OPE_DELETE = 3;
    private final int OPE_COUNT = 4;
    private final int OPE_SELECT = 5;
    private final int OPE_BROWSE = 6;

    private OPreferenceManager pref;
    private DatabaseCreator wrapper;
    private HashMap<String, Integer> operationIds = new HashMap<>();


    public DatabaseManager(Context context) {
        super(context);
        pref = new OPreferenceManager(context);
        wrapper = new DatabaseCreator(context);

        operationIds.put("create", OPE_CREATE);
        operationIds.put("update", OPE_UPDATE);
        operationIds.put("delete", OPE_DELETE);
        operationIds.put("count", OPE_COUNT);
        operationIds.put("select", OPE_SELECT);
        operationIds.put("browse", OPE_BROWSE);
    }

    public void create(WDPluginArgs args, WDPluginCallback callback) {
        wrapper.onCreate(wrapper.getWritableDatabase());
        try {
            JSONObject result = new JSONObject();
            result.put("success", true);
            callback.success(result);
            pref.setBoolean(ResumeBuilderPlugin.KEY_FIRST_LAUNCH, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes arguments with keys :
     * model        name of model (table),
     * operation    create, update, delete, count
     * data         Key value pair data
     * user_id      user id for filter data
     * domain       where domain [('column','operation',value), "AND", (),...]
     *
     * @param args
     * @param callback
     */
    public void perform(WDPluginArgs args, WDPluginCallback callback) {
        String table = args.getString("model");
        String operation = args.getString("operation");
        WDPluginArgs data = args.getMap("data");
        String user_id = args.getString("user_id");
        TableFinder tableFinder = new TableFinder(getContext());
        DatabaseWrapper db = tableFinder.findModel(table);
        if (db != null) {
            int ope = operationIds.get(operation);
            if (ope != -1) {
                switch (ope) {
                    case OPE_SELECT:
                        try {
                            JSONArray rows = new JSONArray();
                            for (DataRow row : db.select()) {
                                rows.put(row.toJSONObject());
                            }
                            callback.success(rows);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case OPE_BROWSE:
                        DataRow row = db.browse(getWhere(args), null);
                        if (row != null) {
                            callback.success(row.toJSONObject());
                        }else{
                            callback.success(false);
                        }
                        break;
                    case OPE_CREATE:
                    case OPE_UPDATE:
                        ContentValues values = new ContentValues();
                        for (String key : data.keys()) {
                            values.put(key, data.get(key) + "");
                        }
                        values.put("user_id", user_id);
                        if (ope == OPE_CREATE) {
                            int newId = db.insert(values);
                            callback.success(newId);
                        } else {
                            int count = db.update(values, getWhere(args), null);
                            callback.success(count);
                        }
                        break;
                    case OPE_DELETE:
                        int count = db.delete(getWhere(args), null);
                        callback.success(count);
                        break;
                    case OPE_COUNT:
                        count = db.count(getWhere(args), null);
                        callback.success(count);
                        break;
                }
            }
        } else {
            callback.fail(false);
        }
    }

    private String getWhere(WDPluginArgs args) {
        StringBuffer where = new StringBuffer();
        String user_id = args.getString("user_id");
        if (!args.getString("model").equals("system_users")) {
            where.append(" user_id = ");
            where.append(user_id);
        }
        if (args.contains("domain")) {
            try {
                JSONArray domain = new JSONArray(args.getString("domain"));
                if (domain.length() > 0 && !args.getString("model").equals("system_users")) {
                    where.append(" AND ");
                }
                for (int i = 0; i < domain.length(); i++) {
                    if (domain.get(i) instanceof JSONArray) {
                        JSONArray domainVal = domain.getJSONArray(i);
                        where.append(domainVal.getString(0));
                        where.append(" ");
                        where.append(domainVal.getString(1));
                        where.append(" '");
                        where.append(domainVal.get(2) + "'");
                    } else {
                        where.append(" ");
                        where.append(domain.getString(i));
                        where.append(" ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return where.toString();
    }

}
