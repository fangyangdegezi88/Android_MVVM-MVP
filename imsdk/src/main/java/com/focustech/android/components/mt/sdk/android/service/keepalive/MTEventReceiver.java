package com.focustech.android.components.mt.sdk.android.service.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.components.mt.sdk.android.service.MTCoreService;

/**
 * <自定义广播接收类，用于启动MTCoreService>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/29]
 * @see [相关类/方法]
 * @since [V1]
 */

public class MTEventReceiver extends BroadcastReceiver {
    private L l = new L(MTEventReceiver.class.getSimpleName());

    @Override
    public void onReceive(Context context, Intent intent) {
        l.d("onReceive  action is: " + intent.getAction());
        Intent intent0 = new Intent(context, MTCoreService.class);
        context.startService(intent0);
    }
}
