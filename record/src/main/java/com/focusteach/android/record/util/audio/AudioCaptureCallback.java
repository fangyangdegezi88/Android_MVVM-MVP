package com.focusteach.android.record.util.audio;

import java.io.File;

/**
 * 音频数据回调
 *
 * @author zhangxu
 */
public interface AudioCaptureCallback {
    /**
     * 录音失败
     *
     * @param audioId
     * @param what
     * @param extra
     */
    void onError(String audioId, int what, int extra);

    /**
     * 录音失败
     *
     * @param audioId
     * @param cause
     */
    void onError(String audioId, Throwable cause);

    /**
     * 录音完成
     *
     * @param audioId
     * @param audioFile
     * @param startTimestamp
     * @param endTimestamp
     */
    void onAudioComplete(String audioId, File audioFile, long startTimestamp, long endTimestamp);

    /**
     * 分贝采样，采样间隔为200ms，基本振幅为1，那么db的值范围为：0 db ~ 90.3 db
     *
     * @param audioId
     * @param amp
     * @param db
     */
    void onSamplingOfDB(String audioId, int amp, double db);

    /**
     * 准备完成，开始录音
     *
     * @param audioId
     */
    void onPrepareSuccessful(String audioId);
}
