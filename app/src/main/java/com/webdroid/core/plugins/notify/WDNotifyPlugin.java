package com.webdroid.core.plugins.notify;

import android.content.Context;
import android.widget.Toast;

import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;
import com.webdroid.core.wrapper.helper.utils.WDPluginCallback;

import org.json.JSONObject;

public class WDNotifyPlugin extends WDPluginHelper {

    public WDNotifyPlugin(Context context) {
        super(context);
    }

    public void showToast(WDPluginArgs args) {
        Toast.makeText(getContext(), args.getString("message"), Toast.LENGTH_LONG).show();
    }

    public void showAlert(WDPluginArgs args, final WDPluginCallback callback) {
        OAlert.showAlert(getContext(), args.getString("message"), new OAlert.OnAlertDismissListener() {
            @Override
            public void onAlertDismiss() {
                callback.success(true);
            }

            @Override
            public void onAlertCancel() {
                callback.fail(true);
            }
        });
    }

    public void showConfirm(WDPluginArgs args, final WDPluginCallback callback) {
        OAlert.showConfirm(getContext(), args.getString("message"), new OAlert.OnAlertConfirmListener() {
            @Override
            public void onConfirmChoiceSelect(OAlert.ConfirmType type) {
                try {
                    JSONObject result = new JSONObject();
                    switch (type) {
                        case NEGATIVE:
                            result.put("key", "no");
                            break;
                        case POSITIVE:
                            result.put("key", "yes");
                            break;
                    }
                    callback.success(result);
                } catch (Exception e) {
                    callback.fail(false);
                    e.printStackTrace();
                }
            }
        });
    }
}
