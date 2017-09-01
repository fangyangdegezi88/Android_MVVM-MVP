package com.focustech.android.commonlibs.util.crash;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.capability.json.GsonHelper;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.device.Device;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @version :1.0.0
 * @描述 ：crash异常捕获
 * @user ：yanguozhu
 * @date 创建时间 ： 2016/3/22
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private L l = new L(CrashHandler.class);
    private Context mContext;

    private UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler mCrashHandler = null;

    /**
     * 实例化
     */
    public static CrashHandler getInstance() {
        if (null == mCrashHandler) {
            mCrashHandler = new CrashHandler();
        }
        return mCrashHandler;
    }


    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        FocusPackage.getInstance().init(context);

        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try { // 暂停2秒
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            ActivityManager.getInstance().exit();
            android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        l.e("CrashHandler-handleException:" + GsonHelper.toJson(FocusPackage.getInstance()) + "\n\r" + GsonHelper.toJson(Device.getInstance()), ex);
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常，即将退出～", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
