package com.plugins.gallery;

import android.content.Context;
import android.content.Intent;

import com.plugins.gallery.utils.OFileManager;
import com.webdroid.WebDroidActivity;
import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;
import com.webdroid.core.wrapper.helper.utils.WDPluginCallback;

public class GalleryPlugin extends WDPluginHelper implements WebDroidActivity.OnActivityResultHandler {
    private OFileManager fileManager = null;
    private WDPluginCallback callback;

    public GalleryPlugin(Context context) {
        super(context);
        fileManager = new OFileManager(getContext());
    }

    public void requestImage(WDPluginArgs args, WDPluginCallback callback) {
        this.callback = callback;
        fileManager.requestForFile(OFileManager.RequestType.IMAGE_OR_CAPTURE_IMAGE);
        getWebView().getActivity().setResultHandler(this);
    }

    @Override
    public void onHandleResult(int requestCode, int resultCode, Intent data) {
        WDPluginArgs result = fileManager.handleResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.contains("datas") && !result.getString("datas").equals("false")) {
                callback.success(result.getString("datas"));
            } else {
                callback.success(false);
            }
        }
    }
}
