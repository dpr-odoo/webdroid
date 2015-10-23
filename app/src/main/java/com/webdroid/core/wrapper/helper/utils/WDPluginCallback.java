package com.webdroid.core.wrapper.helper.utils;

import android.content.Context;
import android.util.Log;

import com.webdroid.core.widgets.WDWebView;

import org.json.JSONObject;

public class WDPluginCallback {
    public static final String TAG = WDPluginCallback.class.getSimpleName();
    private Context mContext;
    private WDWebView webView;
    private String pluginCallbackId;

    public WDPluginCallback(Context context, WDWebView webView, String callbackId) {
        mContext = context;
        this.webView = webView;
        pluginCallbackId = callbackId;
    }

    public void success(Object result) {
        doCallback(result, true);
    }

    public void fail(Object error) {
        doCallback(error, false);
    }

    private void doCallback(Object data, boolean success) {
        try {
            final JSONObject result = new JSONObject();
            result.put("success", success);
            result.put("result", data);
            if (pluginCallbackId != null) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Returning : " + result);
                        webView.loadUrl("javascript:WebDroidCallbacks.invoke('" +
                                pluginCallbackId + "', " + result + ");");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
