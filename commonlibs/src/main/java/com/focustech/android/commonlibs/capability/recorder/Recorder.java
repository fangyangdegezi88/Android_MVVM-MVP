package com.focustech.android.commonlibs.capability.recorder;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.focustech.android.commonlibs.util.GeneralUtils;

import java.io.File;
import java.io.IOException;

/**
 * <录音器>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class Recorder implements RecorderServiceCallback {

    /**
     * 休止状态
     */
    public static final int IDLE_STATE = 0;
    /**
     * 正在录音
     */
    public static final int RECORDING_STATE = 1;
    /**
     * 示例文件前缀
     * @see Recorder#startRecording(int, String, String, boolean, long)
     */
    private static final String SAMPLE_PREFIX = "recording";
    /**
     * 录音存储文件所在的文件夹
     * @see Recorder#Recorder(Context)
     */
    public static final String SAMPLE_DEFAULT_DIR = "/sound_recorder";
    /**
     * 无错误
     */
    public static final int NO_ERROR = 0;
    /**
     * 存储卡内存不足
     */
    public static final int STORAGE_ACCESS_ERROR = 1;
    /**
     * 录音器内部错误
     */
    public static final int INTERNAL_ERROR = 2;
    /**
     * 在通话时录音错误
     */
    public static final int IN_CALL_RECORD_ERROR = 3;
    /**
     * 当前状态
     *
     * @see Recorder#state()
     */
    private int mState = IDLE_STATE;

    /**
     * 实例化录音机的上下文
     */
    private Context mContext;
    /**
     * 示例文件夹
     * @see Recorder#getRecordDir()
     */
    private File mSampleDir = null;
    /**
     * 传入的参数path
     * @see Recorder#Recorder(Context context, String path)
     */
    private String mRequestDir = null;
    /**
     * 录音开始录制或者开始播放的时间点
     */
    private long mSampleStart = 0;
    /**
     * 当前示例音频长度(单位：秒)
     * @see Recorder#sampleLength()
     */
    private int mSampleLength = 0;
    /**
     * 示例文件
     * @see Recorder#sampleFile()
     */
    private File mSampleFile = null;
    /**
     * @see Recorder#setOnStateChangedListener(OnStateChangedListener)
     */
    private OnStateChangedListener mOnStateChangedListener;
    /**
     * 状态回调接口
     */
    public interface OnStateChangedListener {
        public void onStateChanged(int state);
        public void onError(int state);
    }

    public Recorder(Context context) {
        this(context, Environment.getExternalStorageDirectory().getAbsolutePath()
                + SAMPLE_DEFAULT_DIR);
    }

    public Recorder(Context context, String path) {
        mContext = context;
        this.mRequestDir = path;
        File sampleDir = new File(GeneralUtils.isNullOrEmpty(path) ?
                Environment.getExternalStorageDirectory().getAbsolutePath() + SAMPLE_DEFAULT_DIR
                : path);

        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        mSampleDir = sampleDir;

        syncStateWithService();

        RecorderService.getInstance().bind(this);

    }

    public void unbindContext() {
        mContext = null;
    }

    /**
     * 和 录音服务 进行同步
     * @return false表示错误标记正在录音，或者找不到成功录制完成的音频
     */
    public boolean syncStateWithService() {
        if (RecorderService.isRecording()) {
            mState = RECORDING_STATE;
            mSampleStart = RecorderService.getStartTime();
            mSampleFile = new File(RecorderService.getFilePath());
            return true;
        } else if (mState == RECORDING_STATE) {
            // service is idle but local state is recording
            return false;
        } else if (mSampleFile != null && mSampleLength == 0) {
            // this state can be reached if there is an incoming call
            // the record service is stopped by incoming call without notifying
            // the UI
            return false;
        }
        return true;
    }

    /**
     * 开始录音
     * @param outputfileformat 输出文件格式
     * @param name 输出文件名
     * @param extension 扩展名
     * @param highQuality 是否高品质
     * @param maxFileSize 最大文件大小
     */
    public void startRecording(int outputfileformat, String name, String extension,
                               boolean highQuality, long maxFileSize) {
        stopRecording();

        if (mSampleFile == null) {
            try {
                mSampleFile = File.createTempFile(SAMPLE_PREFIX, extension, mSampleDir);
                renameSampleFile(name);
            } catch (IOException e) {
                signalError(STORAGE_ACCESS_ERROR);
                return;
            }
        }

        RecorderService.startRecording(mContext, outputfileformat, mSampleFile.getAbsolutePath(),
                highQuality, maxFileSize);
        mSampleStart = System.currentTimeMillis();
    }

    /**
     * 将创建好的临时文件重命名
     * @param name 新名称
     */
    public void renameSampleFile(String name) {
        if (mSampleFile != null && mState != RECORDING_STATE) {
            if (!TextUtils.isEmpty(name)) {
                String oldName = mSampleFile.getAbsolutePath();
                String extension = oldName.substring(oldName.lastIndexOf('.'));
                File newFile = new File(mSampleFile.getParent() + "/" + name + extension);
                if (!TextUtils.equals(oldName, newFile.getAbsolutePath())) {
                    if (mSampleFile.renameTo(newFile)) {
                        mSampleFile = newFile;
                    }
                }
            }
        }
    }

    /**
     * Resets the recorder state. If a sample was recorded, the file is deleted.
     */
    public void delete() {
        stopRecording();

        if (mSampleFile != null)
            mSampleFile.delete();

        mSampleFile = null;
        mSampleLength = 0;

        signalStateChanged(IDLE_STATE);
    }

    /**
     * 主动结束录音，并认为录音成功
     */
    public void recordOk() {
        stopRecording();
        signalStateChanged(IDLE_STATE);
    }

    /**
     * Resets the recorder state. If a sample was recorded, the file is left on
     * disk and will be reused for a new recording.
     */
    public void clear() {
        stopRecording();
        mSampleLength = 0;
        signalStateChanged(IDLE_STATE);
    }

    /**
     * 回到刚实例化完成的状态
     */
    public void reset() {
        stopRecording();

        mSampleLength = 0;
        mSampleFile = null;
        mState = IDLE_STATE;

        File sampleDir = new File(mRequestDir != null ? mRequestDir : Environment.getExternalStorageDirectory().getAbsolutePath()
                + SAMPLE_DEFAULT_DIR);
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        mSampleDir = sampleDir;

        signalStateChanged(IDLE_STATE);
    }

    /**
     * 获得音量值（由振幅换算而来）
     * @return 分贝值db
     */
    public double db() {
        if (mState != RECORDING_STATE)
            return 0;
        return RecorderService.getDB();
    }

    /**
     * 获得最大振幅
     * @return 从上一次调用此方法或者从0s开始到本次调用期间的最大振幅
     */
    public int amplitude() {
        if (mState != RECORDING_STATE)
            return 0;
        return RecorderService.getMaxAmplitude();
    }

    /**
     * @return 录音进行到的时间，如果不在录音则为0
     */
    public int progress() {
        if (mState == RECORDING_STATE) {
            if (mSampleStart == 0) {
                return 0;
            } else {
                return (int) ((System.currentTimeMillis() - mSampleStart) / 1000);
            }
        }

        return 0;
    }

    /**
     * 停止录音
     */
    private void stopRecording() {
        if (RecorderService.isRecording()) {
            RecorderService.stopRecording();
            mSampleLength = (int) ((System.currentTimeMillis() - mSampleStart) / 1000);
            if (mSampleLength == 0) {
                // round up to 1 second if it's too short
                mSampleLength = 1;
            }
        }
    }

    /**
     * 得到录音文件存放的文件夹
     * @return 文件夹地址
     */
    public String getRecordDir() {
        return mSampleDir.getAbsolutePath();
    }

    /**
     * 当前录音服务状态
     * @return int
     * @see Recorder#setState(int)
     */
    public int state() {
        return mState;
    }

    /**
     * 录制音频长度
     * @return (单位 秒)
     */
    public int sampleLength() {
        return mSampleLength;
    }

    /**
     * 录音文件
     * @return 文件
     */
    public File sampleFile() {
        return mSampleFile;
    }

    /**
     * 绑定 状态变更 回调接口
     * @param onStateChangedListener
     */
    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListener = onStateChangedListener;
    }

    /**
     * 当RecorderService发生状态变化主动回调接口
     * @param recording 正在录音
     */
    @Override
    public void onStateChanged(boolean recording) {
        setState(recording ? Recorder.RECORDING_STATE : Recorder.IDLE_STATE);
    }

    /**
     * 当RecorderService运作时发生错误
     * @param error 错误类型
     */
    @Override
    public void onErrorOccurred(int error) {
        signalError(error);
    }

    /**
     * 发信号-发生错误
     * @param error 错误码
     *              @see Recorder#STORAGE_ACCESS_ERROR = 1
     *              @see Recorder#INTERNAL_ERROR = 2
     *              @see Recorder#IN_CALL_RECORD_ERROR = 3
     */
    private void signalError(int error) {
        if (mOnStateChangedListener != null)
            mOnStateChangedListener.onError(error);
    }

    /**
     * 设置当前状态
     * @param state 当前状态值,状态值如下：
     *
     * @see Recorder#IDLE_STATE = 0
     * @see Recorder#RECORDING_STATE = 1
     *
     * 如果状态发生了变化，会通知到回调接口
     */
    public void setState(int state) {
        if (state == mState)
            return;

        mState = state;
        signalStateChanged(mState);
    }

    /**
     * 发信号-告知状态变化
     * @param state 状态值
     *              @see Recorder#IDLE_STATE = 0
     *              @see Recorder#RECORDING_STATE = 1
     *
     * @see Recorder#setState(int)
     */
    public void signalStateChanged(int state) {
        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(state);
        }
    }

}
