package com.focustech.android.commonlibs.bridge.recorder;

import android.app.Activity;
import android.content.Context;

import com.focustech.android.commonlibs.bridge.BridgeLifeCycleListener;
import com.focustech.android.commonlibs.capability.recorder.Recorder;
import com.focustech.android.commonlibs.capability.recorder.Recorder.OnStateChangedListener;
import com.focustech.android.commonlibs.capability.recorder.RecorderService;

/**
 * <录音公用模块>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class RecorderManager implements BridgeLifeCycleListener {
    /**
     * 调用此方法获得实例
     * @param activity
     * @param onStateChangedListener
     * @return
     */
    public Recorder getRecorder(Activity activity, OnStateChangedListener onStateChangedListener) {
        Recorder recorder = new Recorder(activity);
        recorder.setOnStateChangedListener(onStateChangedListener);
        return recorder;
    }

    /**
     * 调用此方法获得实例
     * @param context
     * @param path
     * @param onStateChangedListener
     * @return
     */
    public Recorder getRecorder(Context context, String path, OnStateChangedListener onStateChangedListener) {
        Recorder recorder = new Recorder(context, path);
        recorder.setOnStateChangedListener(onStateChangedListener);
        return recorder;
    }



    @Override
    public void initOnApplicationCreate(Context context) {
        RecorderService.getInstance().init(context);
    }

    @Override
    public void clearOnApplicationQuit() {
        RecorderService.getInstance().destroy();
    }
}
