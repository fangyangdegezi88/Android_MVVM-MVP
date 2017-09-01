package com.focusteach.android.record.util.audio;

/**
 * Created by zhangxu on 2015/6/16.
 */
public interface AudioPlayerCallback {
    void onAudioBufferingUpdated(String audioId, int percent);

    void onAudioPlayCompleted(String audioId);

    void onAudioSeekCompleted(String audioId);

    void onError(String audioId, int what, int extra, String message);

    void onInfo(String audioId, int what, int extra, String message);
}
