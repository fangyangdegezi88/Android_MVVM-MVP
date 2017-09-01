package com.focustech.android.commonlibs.capability.recorder;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

/**
 * <录音服务类>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class RecorderService implements OnErrorListener {

    private static RecorderService instance = null;

    private static final String TAG = "RecorderService";

    private static final int BASE = 1;

    public static final int BITRATE_AMR = 2 * 1024 * 8; // bits/sec---> 2Kb/s

    public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec---> 20Kb/s
    /**
     * 使用MediaRecorder实现录音功能
     */
    private static MediaRecorder mRecorder = null;
    /**
     * 录音文件地址
     */
    private static String mFilePath = null;
    /**
     * 录音开始时间戳
     */
    private static long mStartTime = 0L;
    /**
     * 根据Sd卡剩余内存估算最大剩余可录制时间
     */
    private RemainingTimeCalculator mRemainingTimeCalculator;
    /**
     * 唤醒屏幕，保持屏幕亮度
     */
    private WakeLock mWakeLock;
    /**
     * 获取通话的状态
     */
    private TelephonyManager mTeleManager;
    /**
     * 回调接口
     */
    private RecorderServiceCallback mCallback;
    /**
     * 关于是否估算剩余时间的变量
     */
    private boolean mNeedUpdateRemainingTime;
    /**
     * 存储上一次的Amplitude,防止过于频繁调用方法，导致取不到amp
     */
    private static int mLastMaxAmplitude;
    private static long mLastGetAmp;
    /**
     * 调用话机时，停止录音
     *
     * @see TelephonyManager#CALL_STATE_RINGING 来电响铃
     * @see TelephonyManager#CALL_STATE_OFFHOOK 去电拨号
     * @see TelephonyManager#CALL_STATE_IDLE 静态
     */
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                localStopRecording();
            }

        }
    };

    /**
     * 关闭实例化方法
     */
    private RecorderService() {
    }

    /**
     * 获取单例
     */
    public static RecorderService getInstance() {
        if (null == instance) {
            instance = new RecorderService();
        }
        return instance;
    }

    /**
     * 初始化，全局初始化，只需要初始化一次
     */
    public void init(Context context) {
        Log.v(TAG, "==================== init ====================");
        mRecorder = null;
        mRemainingTimeCalculator = new RemainingTimeCalculator();
        mNeedUpdateRemainingTime = false;
        mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PARTIAL_WAKE_LOCK, "SoundRecorder");
    }

    /**
     * 绑定回调接口
     *
     * @param callback 回调接口
     */
    public void bind(RecorderServiceCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 解绑回调接口
     */
    public void unbind() {
        mCallback = null;
    }

    /**
     * 初始化计时器-->实例化MediaRecorder并配置-->prepare-->start-->初始变量并通知前台
     *
     * @param outputfileformat 输出文件格式
     * @param path             文件输出路径
     * @param highQuality      是否高品质
     * @param maxFileSize      最大文件大小
     */
    private void localStartRecording(Context context, int outputfileformat, String path, boolean highQuality, long maxFileSize) {
        if (mRecorder == null) {
            mRemainingTimeCalculator.reset();
            if (maxFileSize != -1) {
                mRemainingTimeCalculator.setFileSizeLimit(new File(path), maxFileSize);
            }

            mRecorder = new MediaRecorder();
            try {
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // MIC=microphone
            } catch (Exception e) { // 魅蓝报错
                signalError(Recorder.INTERNAL_ERROR);
                mRecorder = null;
                return;
            }
            if (outputfileformat == MediaRecorder.OutputFormat.THREE_GPP) { // 3GPP
                mRemainingTimeCalculator.setBitRate(BITRATE_3GPP); // 20kb
                mRecorder.setAudioSamplingRate(highQuality ? 44100 : 22050);
                mRecorder.setOutputFormat(outputfileformat);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            } else {
                mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
                mRecorder.setAudioSamplingRate(highQuality ? 16000 : 8000);
                mRecorder.setOutputFormat(outputfileformat);
                mRecorder.setAudioEncoder(highQuality ? MediaRecorder.AudioEncoder.AMR_WB // 2016第一版语音功能用是AMR_NB
                        : MediaRecorder.AudioEncoder.AMR_NB);
            }

            mRecorder.setOutputFile(path);
            mRecorder.setOnErrorListener(this);

            // Handle IO Exception
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                signalError(Recorder.INTERNAL_ERROR);
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                return;
            }

            // Handle IOException
            try {
                mRecorder.start();
            } catch (RuntimeException e) {
                e.printStackTrace();
                AudioManager audioMngr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                boolean isInCall = (audioMngr.getMode() == AudioManager.MODE_IN_CALL);
                if (isInCall) {
                    signalError(Recorder.IN_CALL_RECORD_ERROR);
                } else {
                    signalError(Recorder.INTERNAL_ERROR);
                }
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                return;
            }

            mFilePath = path;
            mStartTime = System.currentTimeMillis();
            mWakeLock.acquire();
            mNeedUpdateRemainingTime = false;
            signalState();
        }
    }

    /**
     * 关闭录音->发送广播->停止服务
     */
    private void localStopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
            } catch (RuntimeException e) {
                Log.e(TAG, "error occurred in localStopRecording()... \n" + e.toString());
            }

            mRecorder.release();
            mRecorder = null;

            signalState();
        }

        stopSelf();

        unbind();
    }

    /**
     * 停止屏幕唤醒锁和来电监听
     */
    private void stopSelf() {
        mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        if (mWakeLock.isHeld()) { // if the wake lock has been acquired but not yet released.
            mWakeLock.release();
        }
    }

    /**
     * 调用此方法释放实例
     */
    public void destroy() {
        localStopRecording();
        instance = null;
    }

    /**
     * 判断当前是否在录音
     *
     * @return boolean true表示正在录音
     */
    public static boolean isRecording() {
        return mRecorder != null;
    }

    /**
     * 获取当前录音文件路径
     *
     * @return String 传入的文件路径
     */
    public static String getFilePath() {
        return mFilePath;
    }

    /**
     * 获取本次录音的开始时间戳
     *
     * @return long recorder.start()成功调用后的时间
     */
    public static long getStartTime() {
        return mStartTime;
    }

    /**
     * 获得最大的振幅(绝对值)
     *
     * @return 从上一次调用此方法或者从0s开始到本次调用期间的最大振幅
     */
    public static int getMaxAmplitude() {
        if (mRecorder == null) {
            return 0;
        }

        if (!isFastGetAmp()) {
            mLastMaxAmplitude = mRecorder.getMaxAmplitude();
        }

        return mLastMaxAmplitude;
    }

    /**
     * @return true表示过于频繁调用该方法
     */
    private static boolean isFastGetAmp() {
        long now = System.currentTimeMillis();
        if (now - mLastGetAmp > 0 && now - mLastGetAmp < 100) {
            return true;
        }
        mLastGetAmp = now;
        return false;
    }

    /**
     * 获得由振幅换算而来的音量值
     *
     * @return 分贝值db
     */
    public static double getDB() {
        double ratio = (double) getMaxAmplitude() / BASE;
        double db = 0;// 分贝
        if (ratio > 1)
            db = 20 * Math.log10(ratio);

        return db;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        signalError(Recorder.INTERNAL_ERROR);
        localStopRecording();
    }

    /**
     * 主动回调接口，发信号告知状态改变
     */
    private void signalState() {
        if (mCallback != null) {
            mCallback.onStateChanged(mRecorder != null);
        }
    }

    /**
     * 主动回调接口，发信号告知发生错误
     *
     * @param error
     */
    private void signalError(int error) {
        if (mCallback != null) {
            mCallback.onErrorOccurred(error);
        }
    }

    /**
     * 主动启动录音服务(入口)
     *
     * @param context
     * @param outputFileFormat 输出文件格式
     * @param path             输出文件路径
     * @param highQuality      是否高品质录音
     * @param maxFileSize      最大文件大小
     */
    public static void startRecording(Context context, int outputFileFormat, String path,
                                      boolean highQuality, long maxFileSize) {
        RecorderService.getInstance().localStartRecording(context, outputFileFormat, path, highQuality, maxFileSize);
    }

    /**
     * 主动终止录音服务(出口)
     */
    public static void stopRecording() {
        RecorderService.getInstance().localStopRecording();
    }

}
