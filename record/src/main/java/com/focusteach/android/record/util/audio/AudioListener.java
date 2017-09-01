package com.focusteach.android.record.util.audio;

/**
 * <音频回调，接口实现>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface AudioListener {
    /**
     * 播放错误
     *
     * @param path
     */
    void onPlayError(String path);

    /**
     * 播放完成
     *
     * @param path
     */
    void onPlayCompletion(String path);

    /**
     * 播放暂停
     *
     * @param path
     */
    void onPlayStop(String path);
}
