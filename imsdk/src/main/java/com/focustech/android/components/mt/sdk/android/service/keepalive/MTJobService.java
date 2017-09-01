package com.focustech.android.components.mt.sdk.android.service.keepalive;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.components.mt.sdk.android.service.MTCoreService;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/16]
 * @see [相关类/方法]
 * @since [V1]
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MTJobService extends JobService {

    private L l = new L(MTJobService.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        l.d("onCreate");
        startJobSheduler();
    }

    public void startJobSheduler() {
        Intent intent = new Intent(this, MTCoreService.class);
        startService(intent);
        l.d("startJobSheduler");
        try {
            int id = 1;
            JobInfo.Builder builder = new JobInfo.Builder(id,
                    new ComponentName(getPackageName(), MTJobService.class.getName()));
            builder.setPeriodic(3000);  //间隔2000毫秒调用onStartJob函数
            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            int ret = jobScheduler.schedule(builder.build());

            // Android24版本才有scheduleAsPackage方法， 期待中
            //Class clz = Class.forName("android.app.job.JobScheduler");
            //Method[] methods = clz.getMethods();
            //Method method = clz.getMethod("scheduleAsPackage", JobInfo.class , String.class, Integer.class, String.class);
            //Object obj = method.invoke(jobScheduler, builder.build(), "com.brycegao.autostart", "brycegao", "test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        }
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        l.d("onStartJob alive");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        l.d("onStopJob alive");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        l.d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
