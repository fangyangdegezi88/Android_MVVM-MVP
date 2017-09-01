package com.focustech.android.components.mt.sdk.android.service.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ProtectService extends Service {
    public static boolean isRunning;

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
        ServiceProtectUtils.getInstance().mtCoreService(this);
        return START_STICKY;
    }
}
