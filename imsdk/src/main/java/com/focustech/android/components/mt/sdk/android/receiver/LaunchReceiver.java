package com.focustech.android.components.mt.sdk.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.focustech.android.components.mt.sdk.android.IntentAction;
import com.focustech.android.components.mt.sdk.android.service.MTCoreService;

/**
 * 监听启动和心跳时间启动Receiver
 *
 * @author zhangxu
 */
public class LaunchReceiver extends BroadcastReceiver {
    private static final LaunchReceiver instance = new LaunchReceiver();
    private static final IntentFilter FILTERS = new IntentFilter();

    private LaunchReceiver() {

    }

    static {
        FILTERS.addAction(IntentAction.ANDROID_SYSTEM_BOOT);
        FILTERS.addAction(IntentAction.ANDROID_TIMER_TICK);
    }

    @Override
    public void onReceive(Context context, Intent i) {
        if (!MTCoreService.isRunning()) {
            Intent intent = new Intent(IntentAction.MT_SERVICE_CORE_BOOT);
            context.startService(intent);
        }
    }

    public static synchronized LaunchReceiver getInstance() {
        return instance;
    }

    public static IntentFilter getFilters() {
        return FILTERS;
    }
}
