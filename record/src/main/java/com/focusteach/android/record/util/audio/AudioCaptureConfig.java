package com.focusteach.android.record.util.audio;

import android.media.MediaRecorder;

/**
 * Created by zhangxu on 2015/6/15.
 */
public class AudioCaptureConfig {
    private static final long DEFAULT_SAMPLING_INTERVAL = 200;

    public String defaultProfile = "none";

    public int format;
    public int maxDuration;
    public int maxFileSize;

    public int audioSource;
    public int audioEncoder;
    public int audioChannels;
    public int audioBitRate;
    public int audioSamplingRate;
    private boolean sampling = false;
    private long samplingInterval; // 单位毫秒

    private AudioCaptureCallback callback;

    public AudioCaptureConfig(boolean highQuality, AudioCaptureCallback callback) {
        this(highQuality, callback, false);
    }

    public AudioCaptureConfig(boolean highQuality, AudioCaptureCallback callback, boolean sampling) {
        this(highQuality, callback, sampling, DEFAULT_SAMPLING_INTERVAL);
    }

    public AudioCaptureConfig(boolean highQuality, AudioCaptureCallback callback, boolean sampling, long samplingInterval) {
        setDefault(highQuality);
        this.callback = callback;
        this.sampling = sampling;
        this.samplingInterval = samplingInterval;
    }

    public String toString() {
        String formatString = (format == MediaRecorder.OutputFormat.MPEG_4) ? "MPEG_4" : "THREE_GPP";
        String audioSourceString;
        switch (audioSource) {
            case MediaRecorder.AudioSource.CAMCORDER:
                audioSourceString = "CAMCORDER";
                break;
            case MediaRecorder.AudioSource.MIC:
                audioSourceString = "MIC";
                break;
            case MediaRecorder.AudioSource.VOICE_CALL:
                audioSourceString = "VOICE_CALL";
                break;
            case MediaRecorder.AudioSource.VOICE_DOWNLINK:
                audioSourceString = "VOICE_DOWNLINK";
                break;
            case MediaRecorder.AudioSource.VOICE_UPLINK:
                audioSourceString = "VOICE_UPLINK";
                break;
            case MediaRecorder.AudioSource.VOICE_RECOGNITION:
                audioSourceString = "VOICE_RECOGNITION";
                break;
            default:
                audioSourceString = "DEFAULT";
                break;
        }

        String audioEncoderString;
        switch (audioEncoder) {
            case 1:
                audioEncoderString = "AMR_NB";
                break;
            case 2:
                audioEncoderString = "AMR_WB";
                break;
            case 3:
                audioEncoderString = "AAC";
                break;
            case 4:
                audioEncoderString = "AAC_PLUS";
                break;
            case 5:
                audioEncoderString = "EAAC_PLUS";
                break;
            default:
                audioEncoderString = "DEFAULT";
                break;
        }

        String rtn = "";

        rtn += "\"defaultProfile\":\"" + defaultProfile + "\"";
        rtn += ",\"format\":\"" + formatString + "\"";
        rtn += ",\"maxDuration\":" + maxDuration;
        rtn += ",\"maxFileSize\":" + maxFileSize;

        rtn += ",\"audioSource\":\"" + audioSourceString + "\"";
        rtn += ",\"audioEncoder\":\"" + audioEncoderString + "\"";
        rtn += ",\"audioBitRate\":" + audioBitRate;
        rtn += ",\"audioChannels\":" + audioChannels;
        rtn += ",\"audioSamplingRate\":" + audioSamplingRate;

        return "{" + rtn + "}";
    }

    private void setDefault(boolean highQuality) {
        defaultProfile = (highQuality) ? "highQuality" : "lowQuality";

        audioSource = MediaRecorder.AudioSource.MIC;
        maxDuration = -1;
        maxFileSize = -1;

        if (highQuality) {
            format = MediaRecorder.OutputFormat.MPEG_4;
            audioEncoder = 3; //AAC
            audioBitRate = 96000;
            audioChannels = 2;
            audioSamplingRate = 44100;
        } else {
            format = MediaRecorder.OutputFormat.MPEG_4;
            audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;
            audioBitRate = 12200;
            audioChannels = 1;
            audioSamplingRate = 8000;
        }
    }

    public MediaRecorder getRecorder(MediaRecorder.OnErrorListener errorListener, MediaRecorder.OnInfoListener infoListener) {
        MediaRecorder recorder = new MediaRecorder();

        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        if (maxDuration > 0) recorder.setMaxDuration(maxDuration);
        if (maxFileSize > 0) recorder.setMaxFileSize(maxFileSize);

        recorder.setAudioSource(audioSource);
        recorder.setOutputFormat(format);
        recorder.setAudioEncoder(audioEncoder);

        recorder.setAudioChannels(audioChannels);

        if (audioBitRate > 0) {
            recorder.setAudioEncodingBitRate(audioBitRate);
        }

        if (audioSamplingRate > 0) {
            recorder.setAudioSamplingRate(audioSamplingRate);
        }

        return recorder;
    }

    public AudioCaptureCallback getCallback() {
        return callback;
    }

    public boolean isSampling() {
        return sampling;
    }

    public long getSamplingInterval() {
        return samplingInterval;
    }
}
