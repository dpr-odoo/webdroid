package com.webdroid.core.wrapper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.webdroid.core.utils.JSONUtils;
import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginsFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WDWebViewWrapper extends WDPluginHelper {
    public static final String ALIAS = "WebDroid";
    private WDPluginsFinder pluginsFinder;

    public WDWebViewWrapper(Context context) {
        super(context);
        pluginsFinder = new WDPluginsFinder(context, this);
    }

    @JavascriptInterface
    public String execute(String action, String data, String callbackId) {
        logAction(action, data, callbackId);
        String[] actionParts = action.split("\\.");
        List<String> alias = new ArrayList<>();
        String method = "";
        if (actionParts.length >= 2) {
            for (int i = 0; i < actionParts.length; i++) {
                if (i == actionParts.length - 1) {
                    method = actionParts[i];
                } else {
                    alias.add(actionParts[i]);
                }
            }
            return pluginsFinder.triggerAction(TextUtils.join(".", alias), method,
                    data, callbackId).toString();
        }
        return "false";
    }

    @JavascriptInterface
    public String getPlugins() {
        JSONArray plugins = new JSONArray();
        try {
            for (String plugin : pluginsFinder.getAllPluginsAlias()) {
                JSONObject pluginData = new JSONObject();
                pluginData.put("name", plugin);
                pluginData.put("methods", JSONUtils.listToArray(pluginsFinder.getPluginMethods(plugin)));
                plugins.put(pluginData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plugins.toString();
    }

    private void logAction(String action, String data, String callbackId) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n--------------------------------\n");
        builder.append("ACTION TRIGGERED FROM JAVASCRIPT\n");
        builder.append("--------------------------------\n");
        builder.append("ACTION      :  " + action + "\n");
        builder.append("DATA        :  " + data + "\n");
        builder.append("CALLBACK ID :  " + callbackId + "\n");
        Log.v("JS Action Trigger: ", builder.toString());
    }


}
