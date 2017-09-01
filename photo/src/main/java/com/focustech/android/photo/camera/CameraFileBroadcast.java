package com.focustech.android.photo.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <相机拍照文件广播接收>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/13]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CameraFileBroadcast extends BroadcastReceiver {
    private BroadcastCallback callback;

    public CameraFileBroadcast(BroadcastCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onReceive(intent);
    }
}
