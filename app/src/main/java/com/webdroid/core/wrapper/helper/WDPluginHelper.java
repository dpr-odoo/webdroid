package com.webdroid.core.wrapper.helper;

import android.content.Context;

import com.webdroid.core.widgets.WDWebView;

public class WDPluginHelper {
    public static final String USER_PLUGINS_PATH = "com.plugins";
    public static final String SYSTEM_PLUGINS_PATH = "com.webdroid.core.plugins";
    private Context mContext;
    private WDWebView mWebView;

    public WDPluginHelper(Context context) {
        mContext = context;
    }

    public void setWebView(WDWebView webView) {
        mWebView = webView;
    }

    public WDWebView getWebView() {
        return mWebView;
    }

    public Context getContext() {
        return mContext;
    }
}
