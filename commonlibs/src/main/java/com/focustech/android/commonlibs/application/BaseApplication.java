package com.focustech.android.commonlibs.application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.log.Slf4jAndLogback;

import ch.qos.logback.classic.Level;


/**
 * <Application基类>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/12]
 * @see [相关类/方法]
 * @since [V1]
 */

public abstract class BaseApplication extends MultiDexApplication implements InitApplication {

    protected L l = new L(getClass().getName());

    public final static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * app实例
     */
    private static Context context = null;

    /**
     * 是否经历过loading
     */
    private static boolean hasLoading = false;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            initData();
        }
    }

    @Override
    public void initData() {
        l.i("initData");
        context = this;
    }

    @Override
    public void initLog(String logPath, String logName, Level level, int days) {
        Slf4jAndLogback.initLogFramework(logPath
                , logName, level
                , days, "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg%n");
        l.i("initLog");
    }


    /**
     * 进程初始化判断
     *
     * @return true 主进程
     */
    @Override
    public boolean isMainProcess() {
        String[] processName = getOtherProcess();
        if (null == processName)
            return true;
        // 当前进程
        String currProcessName = getProcessName(getApplicationContext());
        // 不是主进程，返回false
        for (String process : processName) {
            if (process.equals(currProcessName)) {
                initOtherProcess(process);
                l.i("currProcessName--->>" + currProcessName);
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    private String getProcessName(Context context) {
        int pid = android.os.Process.myPid();

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : am
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }

        return "";
    }

    public static boolean isHasLoading() {
        return hasLoading;
    }

    public static void setHasLoading(boolean b) {
        hasLoading = b;
    }
}
