package com.focustech.android.photo.video;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface VideoRecordCallBack {
    void initRecorderFail();
    void initCameraFail();
    void onRecordStart();
    void onRecordEnd(String videoPath);
    void onRecordError();
}
