package com.focustech.android.components.mt.sdk.android.service.keepalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.focustech.android.components.mt.sdk.R;

/**
 * Android 4.3以后无法构建空的Notification
 * 启动另一个Service同时也让其发送一个Notification使自己置为前台，
 * 并且这个Notification的标志值也跟上面的一样，然后再把它取消掉再停止掉这个Service的前台显示，则会取消掉notification
 */
public class CancelService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(250, builder.build());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    stopForeground(true);
                    NotificationManager manager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(250);
                    stopSelf();
                }
            }).start();
        }
        return START_STICKY;
    }
}
