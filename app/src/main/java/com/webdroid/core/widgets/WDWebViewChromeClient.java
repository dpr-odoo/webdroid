package com.webdroid.core.widgets;

import android.content.Context;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.webdroid.core.plugins.notify.OAlert;

public class WDWebViewChromeClient extends WebChromeClient {

    private Context mContext;
    private WDWebView webView;

    public WDWebViewChromeClient(Context context, WDWebView webView) {
        mContext = context;
        this.webView = webView;
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        OAlert.showAlert(mContext, message, new OAlert.OnAlertDismissListener() {
            @Override
            public void onAlertDismiss() {
                result.confirm();
            }

            @Override
            public void onAlertCancel() {
                result.cancel();
            }
        });
        return true;
    }
}
