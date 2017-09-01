package com.focusteach.android.update.sp;

import android.content.Context;

import com.focustech.android.commonlibs.capability.cache.BaseSharedPreference;

/**
 * @author zhangzeyu
 * @version [版本号, 2016/8/8]
 * @see [相关类/方法]
 * @since [V1]
 */
public class FTSharedPrefDownApk extends BaseSharedPreference {
    /**
     * DownloadManager给出的downloadId
     */
    public static final String CURRENT_DOWNLOAD_ID = "currentDownloadId";
    /**
     * 下载地址
     */
    public static final String CURRENT_DOWNLOAD_URL = "currentDownloadUrl";
    /**
     * 备用下载地址
     */
    public static final String SPARE_DOWNLOAD_URL  = "spareDownloadUrl";
    /**
     * 下载Apk名称
     */
    public static final String DOWNLOAD_APK_NAME    = "downloadApkName";
    /**
     * 下载Apk描述
     */
    public static final String DOWNLOAD_APK_DESC    = "downloadApkDesc";

    public FTSharedPrefDownApk(Context context, String fileName) {
        super(context, fileName);
    }

    public Long getCurrentDownloadId() {
        return getLong(CURRENT_DOWNLOAD_ID, 0L);
    }

    public void setCurrentDownloadId(Long downloadId) {
        saveLong(CURRENT_DOWNLOAD_ID, downloadId);
    }

    public void setCurrentDownloadUrl(String downloadUrl) {
        saveString(CURRENT_DOWNLOAD_URL, downloadUrl);
    }

    public String getCurrentDownloadUrl() {
        return getString(CURRENT_DOWNLOAD_URL, "");
    }

    public void setSpareDownloadUrl(String spareDownloadUrl) {
        saveString(SPARE_DOWNLOAD_URL, spareDownloadUrl);
    }

    public String getSpareDownloadUrl() {
        return getString(SPARE_DOWNLOAD_URL, "");
    }

    public void setDownloadApkDesc(String mDownloadApkDesc) {
        saveString(DOWNLOAD_APK_DESC, mDownloadApkDesc);
    }

    public String getDownloadApkDesc() {
        return getString(DOWNLOAD_APK_DESC, "");
    }

    public void setDownloadApkName(String mDownloadApkName) {
        saveString(DOWNLOAD_APK_NAME, mDownloadApkName);
    }

    public String getDownloadApkName() {
        return getString(DOWNLOAD_APK_NAME, "");
    }

}
