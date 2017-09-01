package com.focustech.android.photo.camera;

import android.content.Intent;

/**
 * <广播接收callback>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/13]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface BroadcastCallback {
    /**
     * 接收到广播
     */
    public void onReceive(Intent intent);
}
