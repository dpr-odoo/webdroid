package com.webdroid.core.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.webdroid.WebDroidActivity;
import com.webdroid.WebDroidApp;
import com.webdroid.core.wrapper.WDWebViewWrapper;

public class WDWebView extends WebView {
    public static final String WEB_DROID_USER_AGENT = "web-droid-webapp";
    private WDWebClient webClient;
    private WDWebViewChromeClient chromeClient;
    private WebDroidApp mApp;
    private WebDroidActivity webDroidActivity;

    public WDWebView(Context context) {
        super(context);
        init();
    }

    public WDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WDWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WDWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mApp = (WebDroidApp) getContext().getApplicationContext();
        setDefaults();
    }

    @SuppressLint("AddJavascriptInterface")
    private void setDefaults() {
        webClient = new WDWebClient(this);
        chromeClient = new WDWebViewChromeClient(getContext(), this);
        setWebViewClient(webClient);
        setWebChromeClient(chromeClient);
        getSettings().setUserAgentString(WEB_DROID_USER_AGENT);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 10MB
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setAppCachePath(mApp.getCacheDir().getAbsolutePath());

        WDWebViewWrapper webViewWrapper = new WDWebViewWrapper(getContext());
        webViewWrapper.setWebView(this);
        addJavascriptInterface(webViewWrapper, WDWebViewWrapper.ALIAS);
    }

    public void setActivity(WebDroidActivity activity) {
        webDroidActivity = activity;
    }

    public WebDroidActivity getActivity() {
        return webDroidActivity;
    }
}
