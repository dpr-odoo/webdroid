package com.webdroid.core.plugins.device;

import android.content.Context;

import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;

import org.json.JSONObject;

public class WDDeviceInfoPlugin extends WDPluginHelper {
    public WDDeviceInfoPlugin(Context context) {
        super(context);
    }

    public JSONObject getDeviceInformation(WDPluginArgs args) {
        JSONObject deviceInfo = new JSONObject();
        try {
            DeviceInfo info = new DeviceInfo(getContext());
            deviceInfo.put("app_name", info.getAppName());
            deviceInfo.put("package", info.getAppPackage());
            deviceInfo.put("version", info.getAppVersion());
            deviceInfo.put("country_code", info.getCountryCode());
            deviceInfo.put("device_branch", info.getDeviceBrand());
            deviceInfo.put("device_lang", info.getDeviceLanguage());
            deviceInfo.put("device_model", info.getDeviceModel());
            deviceInfo.put("device_uuid", info.getDeviceUUID());
            deviceInfo.put("operator", info.getOperator());
            deviceInfo.put("subscriber_id", info.getSubscriberId());
            deviceInfo.put("device_id", info.deviceId());
            deviceInfo.put("phone_number", info.phoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }
}
