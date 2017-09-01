package com.focustech.android.components.mt.sdk.android.service.pojo;

import java.io.Serializable;

/**
 * 登录数据
 *
 * @author zhangxu
 */
public class LoginData implements Serializable {
    private String userName;
    private String password;
    private String status;
    private String role;
    private String equipmentInfo;
    private String locale;
    private String deviceToken = "";
    private String identityCode = "";
    /**
     * 这个默认第一次填false
     * 一旦服务端返回密码错 将这个标志位置为true （客户端重启之后默认为false）
     * 作用主要是告诉服务端 要不要累计3次 和兼容老版本
     */
    private boolean needIdentify = false;

    /**
     * 是否检查登录逾期(默认true)
     */
    private boolean needOverdue = true;

    public LoginData() {

    }

    public LoginData(String userName, String password, String status, String role, String equipmentInfo) {
        this(userName, password, status, role, equipmentInfo, "zh_cn");
    }

    public LoginData(String userName, String password, String status, String role, String equipmentInfo, String locale) {
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.role = role;
        this.equipmentInfo = equipmentInfo;
        this.locale = locale;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEquipmentInfo(String equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public boolean isNeedIdentify() {
        return needIdentify;
    }

    public void setNeedIdentify(boolean needIdentify) {
        this.needIdentify = needIdentify;
    }

    public boolean isNeedOverdue() {
        return needOverdue;
    }

    public void setNeedOverdue(boolean needOverdue) {
        this.needOverdue = needOverdue;
    }
}
