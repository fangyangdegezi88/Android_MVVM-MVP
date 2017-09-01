package com.focustech.android.photo.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.focustech.android.photo.camera.BroadcastCallback;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]  这个广播主要作用是 预览界面因为有网络操作--需要放在主包中   组建包和主包交互
 * @since [V1]
 */
public class VideoActionBroadcast extends BroadcastReceiver {
    private BroadcastCallback callback;

    public VideoActionBroadcast(BroadcastCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onReceive(intent);
    }
}
