package com.focusteach.android.update.sp;

import android.content.Context;

import com.focustech.android.commonlibs.capability.cache.BaseSharedPreference;
import com.focustech.android.commonlibs.util.GeneralUtils;


/**
 * <升级信息缓存>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/8/5]
 * @see [相关类/方法]
 * @since [V1]
 */
public class FTSharedPrefUpgrade extends BaseSharedPreference {

    /**
     * 升级令牌
     */
    public static final String UPGRADE_TOKEN = "token";
    /**
     * 升级描述
     */
    public static final String UPGRADE_DESC = "desc";
    /**
     * 升级措施
     */
    public static final String UPGRADE_ACTION = "upgradeAction";
    /**
     * 目标版本
     */
    public static final String UPGRADE_TARGET_VERSION = "upgradeVersion";
    /**
     * 有效时长
     */
    public static final String UPGRADE_VALID_TIME = "validTime";

    /**
     * 最近一次请求网络的时间
     */
    public static final String LAST_TIME_CHECK_FOR_UPGRADE = "lastTimeCheckForUpgrade";
    /**
     * 有效时长
     */
    public static final String UPGRADE_DOWNLOAD_URL = "downloadUrl";
    /**
     * 有效时长
     */
    public static final String UPGRADE_SPARE_DOWNLOAD_URL = "spareDownloadUrl";

    /**
     * 建议升级框弹出时间
     */
    public static final String ADVISE_UPGRADE_ALERT_TIME = "adviseUpgradeAlertTime";

    public FTSharedPrefUpgrade(Context context, String fileName) {
        super(context, fileName);
    }


    public String getUpgradeToken() {
        return getString(UPGRADE_TOKEN, "");
    }

    public void setUpgradeToken(String mUpgradeToken) {
        saveString(UPGRADE_TOKEN, mUpgradeToken);
    }

    public String getUpgradeDesc() {
        return getString(UPGRADE_DESC, "");
    }

    public void setUpgradeDesc(String mUpgradeDesc) {
        saveString(UPGRADE_DESC, mUpgradeDesc);
    }

    public String getUpgradeAction() {
        return getString(UPGRADE_ACTION, "");
    }

    public void setUpgradeAction(String mUpgradeAction) {
        saveString(UPGRADE_ACTION, mUpgradeAction);
    }

    public String getTargetVersion() {
        return getString(UPGRADE_TARGET_VERSION, "");
    }

    public void setTargetVersion(String mTargetVersion) {
        saveString(UPGRADE_TARGET_VERSION, mTargetVersion);
    }

    public void setUpgradeValidTime(String validTime) {
        saveString(UPGRADE_VALID_TIME, validTime);
    }

    public String getUpgradeValidTime() {
        return getString(UPGRADE_VALID_TIME, "1800000");
    }

    public void setLastTimeCheckForUpgrade(Long milliseconds) {
        saveLong(LAST_TIME_CHECK_FOR_UPGRADE, milliseconds);
    }

    public Long getLastTimeCheckForUpgrade() {
        return getLong(LAST_TIME_CHECK_FOR_UPGRADE, 0L);
    }

    public void setDownloadUrl(String downloadUrl) {
        saveString(UPGRADE_DOWNLOAD_URL, downloadUrl);
    }

    public String getDownloadUrl() {
        return getString(UPGRADE_DOWNLOAD_URL, "");
    }

    public void setSpareDownloadUrl(String spareDownloadUrl) {
        saveString(UPGRADE_SPARE_DOWNLOAD_URL, spareDownloadUrl);
    }

    public String getSpareDownloadUrl() {
        return getString(UPGRADE_SPARE_DOWNLOAD_URL, "");
    }

    /**
     * 记录形如  version_time 的格式
     * @param version
     * @param time
     */
    public void setAdviseUpgradeAlertTime(String version, long time) {
        saveString(ADVISE_UPGRADE_ALERT_TIME, version + "_" + time);
    }

    /**
     * 取得建议升级上次弹出框时间
     * @param version
     * @return
     */
    public long getAdviseUpgradeAlertTime(String version) {
        if (GeneralUtils.isNullOrEmpty(version)) {
            return -1;
        }
        String alertTimeStr = getString(ADVISE_UPGRADE_ALERT_TIME, "");
        if (GeneralUtils.isNullOrEmpty(alertTimeStr)) {
            return -1;
        }
        long time = -1;
        try {
            String[] str = alertTimeStr.split("_");
            if (str.length == 2) {
                if (version.equals(str[0])) {
                    time = Long.parseLong(str[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
}
