package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.android.components.mt.sdk.android.service.CMD;

/**
 * 抽象系统消息
 *
 * @author zhangxu
 */
public abstract class AbstractSystemNotify {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public abstract CMD getCMD();
}
