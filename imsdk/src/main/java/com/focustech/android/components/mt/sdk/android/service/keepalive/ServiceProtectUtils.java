package com.focustech.android.components.mt.sdk.android.service.keepalive;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.components.mt.sdk.R;
import com.focustech.android.components.mt.sdk.android.service.MTCoreService;

/**
 * <功能详细描述>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/12]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceProtectUtils {

//    private boolean isXiaomi = false; 小米手机因为此变量，不能进程保活，先注释

    private static ServiceProtectUtils serviceProtectUtils = null;

    private ServiceProtectUtils() {
//        String deviceModel = android.os.Build.MODEL;
//        if (deviceModel.contains("MI") || deviceModel.contains("mi")) {
//            isXiaomi = true;
//        }
    }

    public static ServiceProtectUtils getInstance() {
        if (serviceProtectUtils == null) {
            serviceProtectUtils = new ServiceProtectUtils();
        }
        return serviceProtectUtils;
    }

    /**
     * 前台进程
     *
     * @param service
     */
    public void startForeground(Service service) {
//        if (isXiaomi) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(service);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            service.startForeground(250, builder.build());
            service.startService(new Intent(service, CancelService.class));
        } else {
            service.startForeground(250, new Notification());
        }
    }

    /**
     * 守护进程
     *
     * @param service
     */
    public void protectService(final Service service) {
//        if (isXiaomi) return;
        if (!MTCoreService.isRunning) {
            MTCoreService.isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(3000);
                    MTCoreService.isRunning = false;
                    service.startService(new Intent(service, ProtectService.class));
                }
            }).start();
        }
    }

    /**
     * 守护进程
     *
     * @param service
     */
    public void mtCoreService(final Service service) {
//        if (isXiaomi) return;
        if (!ProtectService.isRunning) {
            ProtectService.isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1500);
                    ProtectService.isRunning = false;
                    service.startService(new Intent(service, MTCoreService.class));
                }
            }).start();
        }
    }

    /**
     * 每隔一分钟启动一次服务
     *
     * @param service
     */
    public void alarmManagerProtect(Service service) {
//        if (isXiaomi) return;
        //注意：这里面是每隔一分钟启动一次服务，而不是检测服务是否开启
        //此事若用常规方法检测服务是否存在,就会得到返回值为true，即服务在运行，这样就无法启动服务了.
        AlarmManager mAlarmManager = null;
        PendingIntent mPendingIntent = null;
        Intent sendIntent = new Intent(service.getApplicationContext(), MTCoreService.class);
        mAlarmManager = (AlarmManager) service.getSystemService(service.ALARM_SERVICE);
        mPendingIntent = PendingIntent.getService(service, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60000, mPendingIntent);
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : am
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取子进程的主进程名称
     *
     * @param context
     * @return
     */
    public String getMainProcessName(Context context) {
        String currentName = getProcessName(context);
        if (GeneralUtils.isNotNullOrEmpty(currentName)) {
            if (currentName.contains(":")) {
                int index = currentName.indexOf(":");
                currentName = currentName.substring(0, index);
            }
        }
        return currentName;
    }
}
