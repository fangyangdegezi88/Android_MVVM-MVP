package com.focustech.android.commonlibs.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/2/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ProcessUtils {


    /**
     * 判断前台进程是否在运行
     * @param context
     * @return
     */
    public boolean isProcessRuning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(com.focustech.android.commonlibs.BuildConfig.APPLICATION_ID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是在后台
     * @param context
     * @return
     */
    public static boolean isRunInBack(Context context)
    {
        boolean inBackground = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if(runningApps == null)
        {
            return false;
        }

        String processName = null;
        String currentProcessName = context.getPackageName();

        for (ActivityManager.RunningAppProcessInfo process : runningApps)
        {
            processName = process.processName;

            if (processName.equals(currentProcessName))
            {
                switch (process.importance)
                {
                    case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND:// 100前台

                        inBackground = false;

                        break;

                    case ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND:// 400后台,当home键后，在service里面调用此方法，得到这个值

                        inBackground = true;

                        break;

                    case ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY://500 这个进程不存在


                        break;

                    case ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE://300这个进程包括service在里面

                        inBackground = true;

                        break;
                    case ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE://200这个进程是可见的，正展现给用户


                        break;

                    case 130://4.0以上专有的值  IMPORTANCE_PERCEPTIBLE(可察觉到的)

                        inBackground = true;

                        break;

                    default :

                        inBackground = true;

                        break;
                }

            }
        }

        return inBackground;
    }
}
