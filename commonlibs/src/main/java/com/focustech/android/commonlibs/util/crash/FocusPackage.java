package com.focustech.android.commonlibs.util.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.focustech.android.commonlibs.application.BaseApplication;

import java.io.Serializable;

/**
 * @version :
 * @描述 ：
 * @user ：yanguozhu
 * @date 创建时间 ： 2016/3/22
 */
public class FocusPackage implements Serializable {
    /**
     * Package单例
     */
    private static FocusPackage instance;

    /**
     * 获取Package单例
     *
     * @return
     */
    public static FocusPackage getInstance() {
        if (instance == null) {
            synchronized (FocusPackage.class) {
                if (instance == null) {
                    instance = new FocusPackage();
                }
            }
        }
        return instance;
    }


    /**
     * 创建实例--主进程使用
     *
     * @return
     */
    public static FocusPackage newBuilder() {
        FocusPackage focusPackage = new FocusPackage();
        focusPackage.init(BaseApplication.getContext());
        return focusPackage;
    }

    /**
     * 创建实例--子进程使用
     *
     * @param context
     * @return
     */
    public static FocusPackage newBuilder(Context context) {
        FocusPackage focusPackage = new FocusPackage();
        focusPackage.init(context);
        return focusPackage;
    }


    /**
     * 包信息实例
     */
    private PackageInfo info = null;
    /**
     * 包管理器实例
     */
    private PackageManager pm;

    public void init(Context context) {
        pm = context.getPackageManager();
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用版本CODE
     *
     * @return
     */
    public int getLocalVersionCode() {
        return info != null ? info.versionCode : 0;
    }

    /**
     * 获取应用版本名称
     *
     * @return
     */
    public String getLocalVersionName() {
        return info != null ? info.versionName : "defaultVName";
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public String getAppName() {
        return info != null ? (String) info.applicationInfo.loadLabel(pm) : "defaultAppName";
    }

    /**
     * 获取包名
     *
     * @return
     */
    public String getPackageName() {
        return info != null ? info.packageName : "defaultPackageName";
    }
}
