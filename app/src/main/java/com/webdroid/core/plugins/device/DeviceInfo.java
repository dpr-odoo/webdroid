package com.webdroid.core.plugins.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

public class DeviceInfo {
    private Context mContext;
    private TelephonyManager mTelephonyManager = null;
    private PackageManager lPackageManager;

    public DeviceInfo(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        lPackageManager = mContext.getPackageManager();
    }

    public String deviceId() {
        return mTelephonyManager.getDeviceId();
    }

    public String phoneNumber() {
        String number = mTelephonyManager.getLine1Number();
        if (number != null && !TextUtils.isEmpty(number)) {
            return number;
        }
        return "N/A";
    }

    public String getDeviceBrand() {
        return Build.BRAND;
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    public String getAppPackage() {
        return mContext.getApplicationContext().getPackageName();
    }

    public String getAppName() {
        ApplicationInfo lApplicationInfo = null;
        try {
            lApplicationInfo = lPackageManager.getApplicationInfo(
                    mContext.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) (lApplicationInfo != null ? lPackageManager
                .getApplicationLabel(lApplicationInfo) : "Unknown");
    }

    public String getAppVersion() {
        PackageInfo mPackage = null;
        try {
            mPackage = lPackageManager.getPackageInfo(
                    mContext.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (mPackage != null ? mPackage.versionName : "Unknown");
    }

    public String getOperator() {
        String operator = mTelephonyManager.getSimOperatorName();
        if (operator != null && !TextUtils.isEmpty(operator))
            return operator;
        return "N/A";
    }

    public String getSubscriberId() {
        String sub_id = mTelephonyManager.getSubscriberId();
        if (sub_id != null && !TextUtils.isEmpty(sub_id))
            return sub_id;
        return "N/A";
    }

    public String getCountryCode() {
        String code = mTelephonyManager.getNetworkCountryIso().toUpperCase(
                Locale.getDefault());
        if (code != null && !TextUtils.isEmpty(code))
            return code;
        return "N/A";
    }

    public String androidOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getDeviceUUID() {
        return Build.SERIAL;
    }

    public String getDeviceLanguage() {
        return Locale.getDefault().toString();
    }

}
