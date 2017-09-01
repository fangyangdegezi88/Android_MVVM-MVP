package com.focustech.android.commonlibs.util.device;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * <SDK Service 能力类>
 *
 * @author yanguozhu
 * @version [版本号, 2016-5-26]
 * @see [相关类/方法]
 * @since [V1]
 */
public class Device implements Serializable {

    /**
     * 手机厂商
     */
    public enum MOBILE_BRAND {
        samsung,
        huawei,
        xiaomi,
        meizu,
        oppo,
        htc,
        other;

        public static MOBILE_BRAND getBrand(String name) {
            for (MOBILE_BRAND item : MOBILE_BRAND.values()) {
                if (name.toLowerCase().equals(item.name())) {
                    return item;
                }
            }
            return other;
        }
    }

    /**
     * Device单例
     */
    private static Device instance;

    /**
     * 获取Device单例
     *
     * @return
     */
    public static Device getInstance() {
        if (instance == null) {
            instance = new Device();
        }
        return instance;
    }


    private String myUUID;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 设备OS版本号
     */
    private String deviceOsVersion;
    /**
     * User-Agent
     */
    private String userAgent;

    private MOBILE_BRAND brand;

    /**
     * 应用版本信息
     */
    private String appVersion;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 初始化获取设备信息
     * <p/>
     * 注意：<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @param mContext
     */
    public void initDevice(Context mContext, String appVersion, String channel) {
        this.appVersion = appVersion;
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + mTm.getDeviceId();

        tmSerial = "" + mTm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        myUUID = deviceUuid.toString();
        // 设备型号
//        deviceModel = android.os.Build.MODEL;
        //为了兼容魅族系统推送，将model，改为厂商brand
        deviceModel = Build.BRAND;
        brand = MOBILE_BRAND.getBrand(deviceModel);
        // 设备OS版本号
        deviceOsVersion = android.os.Build.VERSION.RELEASE;
        //UA
        try {
            String temp = "ANDROID_MOBILE_PARENT_" + deviceModel + "_" + appVersion;
            userAgent = URLEncoder.encode(temp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            userAgent = "ANDROID_MOBILE_PARENT_" + "UNKNOW" + "_" + appVersion;
        }
        this.channel = channel;
    }

    public String getMyUUID() {
        return myUUID;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getUserAgent() {
        return userAgent == null ? "ANDROID_MOBILE" : userAgent;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public String info() {
        return JSONObject.toJSONString(this);
    }

    public MOBILE_BRAND getBrand() {
        return brand;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setMyUUID(String myUUID) {
        this.myUUID = myUUID;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setBrand(MOBILE_BRAND brand) {
        this.brand = brand;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
