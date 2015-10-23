package com.webdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.config.WDConfig;
import com.webdroid.core.widgets.WDWebView;

public class WebDroidActivity extends AppCompatActivity {

    private WDWebView webView;
    private OnActivityResultHandler mOnActivityResultHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_droid);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        webView = (WDWebView) findViewById(R.id.webDroidWebView);
        webView.setActivity(this);
        if (savedInstanceState == null)
            webView.loadUrl(WDConfig.LAUNCH_URL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the WebView
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the state of the WebView
        webView.restoreState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mOnActivityResultHander != null) {
            mOnActivityResultHander.onHandleResult(requestCode, resultCode, data);
        }
    }

    public void setResultHandler(OnActivityResultHandler callback) {
        mOnActivityResultHander = callback;
    }

    public interface OnActivityResultHandler {
        void onHandleResult(int requestCode, int resultCode, Intent data);
    }
}
