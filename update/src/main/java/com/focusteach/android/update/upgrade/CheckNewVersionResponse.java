package com.focusteach.android.update.upgrade;

import java.io.Serializable;

/**
 * @version : V1.0.0
 * @描述 : checkNewVersion（检查新版本）返回response
 * @user : zhangzeyu
 * @date : 2016/3/11
 */
public class CheckNewVersionResponse implements Serializable{
    // UPGRADE INFO
    private String token;//客户端升级令牌
    private String validTime;//token有效时间
    private String nowTime;//当前时间
    private String thresholdSpeed;//服务器限速
    private String upgradeVersion;//升级目标版本
    private String upgradeStyle;//升级类型
    private String upgradeMode;//升级模式
    private String upgradeAction;//升级动作
    private String desc;//升级描述

    @Override
    public String toString() {
        return new StringBuilder()
                .append("token=").append(token).append(",\n")
                .append("validTime=").append(validTime).append(",\n")
                .append("nowTime=").append(nowTime).append(",\n")
                .append("thresholdSpeed=").append(thresholdSpeed).append(",\n")
                .append("upgradeVersion=").append(upgradeVersion).append(",\n")
                .append("upgradeStyle=").append(upgradeStyle).append(",\n")
                .append("upgradeMode=").append(upgradeMode).append(",\n")
                .append("upgradeAction=").append(upgradeAction).append(",\n")
                .append("desc=").append(desc)
                .toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getThresholdSpeed() {
        return thresholdSpeed;
    }

    public void setThresholdSpeed(String thresholdSpeed) {
        this.thresholdSpeed = thresholdSpeed;
    }

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public String getUpgradeStyle() {
        return upgradeStyle;
    }

    public void setUpgradeStyle(String upgradeStyle) {
        this.upgradeStyle = upgradeStyle;
    }

    public String getUpgradeMode() {
        return upgradeMode;
    }

    public void setUpgradeMode(String upgradeMode) {
        this.upgradeMode = upgradeMode;
    }

    public String getUpgradeAction() {
        return upgradeAction;
    }

    public void setUpgradeAction(String upgradeAction) {
        this.upgradeAction = upgradeAction;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
