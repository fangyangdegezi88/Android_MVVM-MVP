package com.focusteach.android.record.util.audio;

import android.media.MediaRecorder;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 录音，非线程安全。
 *
 * @author zhangxu
 */
public class AudioCapture {
    private static final ScheduledExecutorService threads = Executors.newScheduledThreadPool(1);
    private final AudioCaptureConfig audioCaptureConfig;
    private MediaRecorder recorder = null;
    private File audioFile;
    private String lastAudioId;
    private transient boolean running = false;
    private long startTimestamp;
    private long endTimestamp;
    private final int BASE = 1;
    private int count = 0;

    private final MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            stop();
            audioCaptureConfig.getCallback().onError(lastAudioId, what, extra);
        }
    };

    private final MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            stop();
            audioCaptureConfig.getCallback().onError(lastAudioId, what, extra);
        }
    };

    public AudioCapture(AudioCaptureConfig audioCaptureConfig) {
        this.audioCaptureConfig = audioCaptureConfig;
    }

    public boolean start(String audioId, File target, long timeout, TimeUnit unit) {
        if (running) {
            throw new RuntimeException("already running");
        }

        count++;

        if (target == null) {
            return false;
        }

        stop();

        audioFile = target;
        running = true;
        this.lastAudioId = audioId;

        try {
            recorder = audioCaptureConfig.getRecorder(errorListener, infoListener);
            recorder.setOutputFile(target.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            audioCaptureConfig.getCallback().onPrepareSuccessful(audioId);
            startTimestamp = System.currentTimeMillis();

            threads.schedule(new Runnable() {
                final int currentCount = count;

                @Override
                public void run() {
                    if (currentCount == count) {
                        stop();
                    }
                }
            }, timeout, unit);
            if (audioCaptureConfig.isSampling()) {
                threads.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (running) {
                            int amp = recorder.getMaxAmplitude();
                            audioCaptureConfig.getCallback().onSamplingOfDB(lastAudioId, amp, computeDB(amp));
                            threads.schedule(this, audioCaptureConfig.getSamplingInterval(), TimeUnit.MILLISECONDS);
                        }
                    }
                }, audioCaptureConfig.getSamplingInterval(), TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            audioFile = null;
            running = false;
            this.lastAudioId = null;
            return false;
        }
    }

    public synchronized boolean stop() {
        running = false;
        lastAudioId = null;

        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                endTimestamp = System.currentTimeMillis();
                audioCaptureConfig.getCallback().onAudioComplete(lastAudioId, audioFile, startTimestamp, endTimestamp);
            } catch (Exception e) {
                audioCaptureConfig.getCallback().onError(lastAudioId, e);
            }

            audioFile = null;
            recorder = null;
        }

        startTimestamp = 0;
        endTimestamp = 0;

        return true;
    }

    private double computeDB(int amplitude) {
        double ratio = (double) amplitude / BASE;
        double db = 0;// 分贝
        if (ratio > 1)
            db = 20 * Math.log10(ratio);

        return db;
    }
}
