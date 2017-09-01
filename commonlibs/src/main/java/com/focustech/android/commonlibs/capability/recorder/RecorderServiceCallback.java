package com.focustech.android.commonlibs.capability.recorder;

/**
 * <录音服务回调接口>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface RecorderServiceCallback {
    public void onStateChanged(boolean recording);
    public void onErrorOccurred(int error);
}
