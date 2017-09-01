package com.focustech.android.components.mt.sdk.android.service.pojo;

/**
 * @author zhangxu
 */
public class EquipmentInfo {
    private String osVersion;  // 手机版本号
    private String sdkVersion; // sdk版本号
    private String vendor;     // 手机制造商
    private String model;      // 手机型号
    private String supportedABI; // cpu支持的ABI

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSupportedABI() {
        return supportedABI;
    }

    public void setSupportedABI(String supportedABI) {
        this.supportedABI = supportedABI;
    }
}
