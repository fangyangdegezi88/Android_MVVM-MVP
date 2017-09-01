package com.focusteach.android.record.util.audio;

/**
 * <音频状态>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public enum AudioState {
    /**
     * 正在播放
     */
    PLAY_ING,
    /**
     * 暂停
     */
    PAUSE,
    /**
     * 停止
     */
    STOP,
    /**
     * 播放完成
     */
    PLAY_FINISHED,
    /**
     * 初始状态
     */
    NONE,
}
