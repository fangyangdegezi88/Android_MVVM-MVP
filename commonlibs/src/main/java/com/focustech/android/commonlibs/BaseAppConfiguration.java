package com.focustech.android.commonlibs;

import android.content.Context;
import android.util.Log;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.util.BuildConfigUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <*****>
 *
 * @author liuzaibing
 * @version [版本号, 2017/6/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class BaseAppConfiguration {
    protected static final Map<String, Integer> MIME_MAPPING = new HashMap<>();
    protected static final Map<String, Byte> EMOTION_MAPPING = new HashMap<>();
    protected static String PARSER;

    public static void init(Context context) {
        String configFile = (String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "CONFIG_FILE");
        String sdkFile = (String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "SDK_FILE");

        initAppConfig(context, configFile);
        initAppConfig(context, sdkFile);
    }

    /**
     * 适用于初始化conf,而不要初始化sdk-conf类型
     * */
    public static void init(Context context , boolean initSdkFile) {
        String configFile = (String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "CONFIG_FILE");
        initAppConfig(context, configFile);
        if(initSdkFile){
            String sdkFile = (String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "SDK_FILE");
            initAppConfig(context, sdkFile);
        }
    }

    private static void initAppConfig(Context context, String file) {
        InputStream inputStream = null;

        try {
            inputStream = context.getAssets().open(file);
            Properties properties = new Properties();
            properties.load(inputStream);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                System.setProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            Log.e("init", "configuration:" + file, e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("init", "configuration:" + file, e);
                }
            }
        }
    }

    /**
     * <URL>升级地址<URL/>
     *
     * @return
     */
    private static String getUpdateURL() {
        return System.getProperty("app.url.update");
    }

    /**
     * <设置>升级域Domain<设置/>
     *
     * @return
     */
    public static String getUpgradeDomain() {
        return System.getProperty("app.update.domain");
    }

    /**
     * <URL>上传崩溃日志<URL/>
     *
     * @return
     */
    public static String getReportCrashURL() {
        return getUpdateURL() + System.getProperty("app.url.report.crash");
    }

    /**
     * <URL>检查当前版本</URL>
     *
     * @return
     */
    public static String getCheckCurrentVersionUrl() {
        return getUpdateURL() + System.getProperty("app.url.update.current.version");
    }

    /**
     * <URL>获取升级策略</URL>
     *
     * @return
     */
    public static String getUpdateAdviseUrl() {
        return getUpdateURL() + System.getProperty("app.url.update.advise");
    }

    /**
     * <URL>升级下载地址</URL>
     *
     * @return
     */
    public static String getUpdateDownloadUrl() {
        return getUpdateURL() + System.getProperty("app.url.update.download");
    }

    /**
     * <URL>下载完成报告服务器<URL/>
     *
     * @return
     */
    public static String getUpdateCompleteUrl() {
        return getUpdateURL() + System.getProperty("app.url.update.complete");
    }
}
