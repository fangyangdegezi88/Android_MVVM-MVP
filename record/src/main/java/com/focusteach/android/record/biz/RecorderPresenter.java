package com.focusteach.android.record.biz;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.focusteach.android.record.R;
import com.focusteach.android.record.util.AudioPermissionUtil;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.recorder.RecorderManager;
import com.focustech.android.commonlibs.capability.recorder.Recorder;
import com.focustech.android.commonlibs.capability.recorder.RecorderService;
import com.focustech.android.commonlibs.capability.recorder.RemainingTimeCalculator;
import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonuis.biz.BaseCommonPresenter;

import java.io.File;


/**
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class RecorderPresenter extends BaseCommonPresenter<IRecorderView> implements Recorder.OnStateChangedListener {
    private static final String TAG = "SoundRecorder";

    private static final String AUDIO_3GPP = "audio/3gpp";

    private static final String AUDIO_AMR = "audio/amr";

    private static final String AUDIO_ANY = "audio/*";

    private static final String ANY_ANY = "*/*";

    private static final String FILE_EXTENSION_AMR = ".amr";

    private static final String FILE_EXTENSION_3GPP = ".3gpp";
    /**
     * 请求的最长录音时间
     */
    private long mRequestMaxSecond = -1;
    /**
     * 请求存入的文件夹
     */
    private String mRequestOutputDir = null;
    /**
     * 目前默认低品质，高品质在iOS端转码技术还不健全，不利于多端分享
     */
    private boolean isHighQuality = false;
    /**
     * 录音类型
     */
    private String mRequestedType = AUDIO_AMR;
    /**
     * 时间格式
     */
    private String mTimerFormat;
    /**
     * 资源
     */
    private Resources res;
    /**
     * SDCard卡发生变动广播接收
     */
    private BroadcastReceiver mSDCardMountEventReceiver = null;
    /**
     * 根据Sd卡剩余内存估算最大剩余可录制时间
     */
    private RemainingTimeCalculator mRemainingTimeCalculator;
    /**
     * 本地媒体文件Id
     */
    private String mLocalMediaId;
    /**
     * 最大文件大小限制
     */
    private long mMaxFileSize = -1;
    /**
     * true表示录音中止
     */
    private boolean mSampleInterrupted = false;
    /**
     * 需要展示在UI中的错误信息
     */
    private String mErrorUiMessage = null;
    /**
     * 停止Ui更新
     */
    private boolean mStopUiUpdate = false;
    /**
     * 录音实例
     */
    private Recorder mRecorder;
    /**
     * 录音权限判断类
     */
    private AudioPermissionUtil mPermissionUtil;

    private final Handler mHandler = new Handler();

    private Runnable mUpdateTimer = new Runnable() {
        public void run() {
            if (!mStopUiUpdate) {
                updateUi();
            }
        }
    };

    /**
     * 初始化录音
     *
     * @param res
     * @param mRequestMaxTime   最长录音时间 <单位>秒<单位/>
     * @param mRequestOutputDir 录音文件输出路径
     */
    public void initRecorder(Resources res, long mRequestMaxTime, String mRequestOutputDir) {
        this.mRequestMaxSecond = mRequestMaxTime > 0 ? mRequestMaxTime : -1;
        this.mRequestOutputDir = mRequestOutputDir;

        RecorderManager recorderManager = BridgeFactory.getBridge(Bridges.RECORDER, BaseApplication.getContext());
        mRecorder = recorderManager.getRecorder(mAppContext, mRequestOutputDir, this);
        this.res = res;
        mTimerFormat = res.getString(R.string.timer_format);
        mRemainingTimeCalculator = new RemainingTimeCalculator();

        mPermissionUtil = new AudioPermissionUtil();
    }

    /**
     * 开始录音
     */
    public void startRecording() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mSampleInterrupted = true;
            mErrorUiMessage = res.getString(R.string.insert_sd_card);
            updateUi();
        } else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
            mSampleInterrupted = true;
            mErrorUiMessage = res.getString(R.string.storage_is_full);
            updateUi();
        } else {
            mStopUiUpdate = false;
            mLocalMediaId = System.currentTimeMillis() + "";

            if (AUDIO_AMR.equals(mRequestedType)) {
                mRemainingTimeCalculator.setBitRate(RecorderService.BITRATE_AMR);
                int outputfileformat = isHighQuality ? MediaRecorder.OutputFormat.AMR_WB
                        : MediaRecorder.OutputFormat.AMR_NB;
                mRecorder.startRecording(outputfileformat, mLocalMediaId,
                        FILE_EXTENSION_AMR, isHighQuality, mMaxFileSize);
            } else if (AUDIO_3GPP.equals(mRequestedType)) {
                // HACKME: for HD2, there is an issue with high quality 3gpp
                // use low quality instead
                if (Build.MODEL.equals("HTC HD2")) {
                    isHighQuality = false;
                }

                mRemainingTimeCalculator.setBitRate(RecorderService.BITRATE_3GPP);
                mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, mLocalMediaId, FILE_EXTENSION_3GPP, isHighQuality, mMaxFileSize);
            } else {
                Log.e(TAG, "Invalid output file type requested");
//                throw new IllegalArgumentException("Invalid output file type requested");
            }

            if (mMaxFileSize != -1) {
                mRemainingTimeCalculator.setFileSizeLimit(mRecorder.sampleFile(), mMaxFileSize);
            }
        }
    }

    private void updateUi() {
        Log.d(TAG, "updateUi");
        checkAudioPermission();
        updateTimerView();
        updateDBView();
    }

    /**
     * 检查权限
     */
    private void checkAudioPermission() {
        int state = mRecorder.state();

        if (state == Recorder.RECORDING_STATE) {
            boolean permission = mPermissionUtil.compareAmplitude(mRecorder.amplitude())
                    && mPermissionUtil.fileExist(mRecorder.sampleFile())
                    && !mPermissionUtil.fileLengthAlwaysZero(mRecorder.sampleFile());

            if (!permission) {
                stopRecorder();
                if (mvpView != null) mvpView.alertNoPermission();
            }

        }
    }

    /**
     * 更新dB视图
     */
    private void updateDBView() {
        if (mvpView != null) mvpView.updateDb(mRecorder.db());
    }

    /**
     * Update the big MM:SS timer. If we are in playback, also update the
     * progress bar.
     */
    private void updateTimerView() {
        int state = mRecorder.state();

        boolean ongoing = state == Recorder.RECORDING_STATE;

        if (state == Recorder.RECORDING_STATE) {
            long time = mRecorder.progress();
            String timeStr = String.format(mTimerFormat, time / 60, time % 60);
            if (mvpView != null) mvpView.updateRecorderLength(time, timeStr);
            updateTimeRemaining(time);
        }

        if (ongoing) {
            mHandler.postDelayed(mUpdateTimer, 100);
        }
    }

    /*
     * Called when we're in recording state. Find out how much longer we can go
     * on recording. If it's under 5 minutes, we display a count-down in the UI.
     * If we've run out of time, stop the recording.
     */
    private void updateTimeRemaining(long progress) {
        long t = mRemainingTimeCalculator.timeRemaining();

        if (t <= 0) {
            mSampleInterrupted = true;

            int limit = mRemainingTimeCalculator.currentLowerLimit();
            switch (limit) {
                case RemainingTimeCalculator.DISK_SPACE_LIMIT:
                    mErrorUiMessage = res.getString(R.string.storage_is_full);
                    break;
                case RemainingTimeCalculator.FILE_SIZE_LIMIT:
                    mErrorUiMessage = res.getString(R.string.max_length_reached);
                    break;
                default:
                    mErrorUiMessage = null;
                    break;
            }

            recordOk();
        } else {
            long idealT = -1;  // 当前，在理想情况下的最长可录时间
            if (mRequestMaxSecond > 0) {
                idealT = mRequestMaxSecond - progress;
            }

            if (idealT >= 0) {
                idealT = Math.min(idealT, t);  // 理想值和实际值中的较小值
            } else {
                idealT = t;
            }

            if (idealT < 15 && idealT >= 0) {
                String raw = String.format(res.getString(R.string.recorder_remaining_time), idealT);
                SpannableString ss = new SpannableString(raw);
                ss.setSpan(new ForegroundColorSpan(res.getColor(R.color.app_support_txt_color)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(res.getColor(R.color.app_caution_color)), 6, raw.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (mvpView != null) mvpView.updateRemainingTime(ss);
            }

            if (idealT <= 0) {
                recordOk();
                if (mvpView != null) mvpView.recordTimeout();
            }
        }
    }

    /**
     * 停止Recorder
     */
    public void stopRecorder() {
        mHandler.removeCallbacks(mUpdateTimer);
        mRecorder.clear();
        mStopUiUpdate = true;

    }

    /**
     * 录音成功，停止Recorder
     */
    public void recordOk() {
        mHandler.removeCallbacks(mUpdateTimer);
        mRecorder.recordOk();
        mStopUiUpdate = true;

    }

    /**
     * Registers an intent to listen for
     * ACTION_MEDIA_EJECT/ACTION_MEDIA_UNMOUNTED/ACTION_MEDIA_MOUNTED
     * notifications.
     *
     * @see RecorderPresenter#unregisterExternalStorageListener(Context)
     */
    public void registerExternalStorageListener(Context context) {
        if (mSDCardMountEventReceiver == null) {
            mSDCardMountEventReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mRecorder.reset();
                }
            };
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            iFilter.addDataScheme("file");
            context.registerReceiver(mSDCardMountEventReceiver, iFilter);
        }
    }

    /**
     * Called on destroy to unregister the SD card mount event receiver.
     *
     * @see RecorderPresenter#registerExternalStorageListener(Context)
     */
    public void unregisterExternalStorageListener(Context context) {
        if (mSDCardMountEventReceiver != null) {
            context.unregisterReceiver(mSDCardMountEventReceiver);
            mSDCardMountEventReceiver = null;
        }
    }

    @Override
    public void onStateChanged(int state) {
        if (state == Recorder.RECORDING_STATE) {
            mSampleInterrupted = false;
            mErrorUiMessage = null;
        }

        if (!mStopUiUpdate) { // 解决StackOverflowError
            updateUi();
        }
    }

    @Override
    public void onError(int error) {
        String message = null;
        switch (error) {
            case Recorder.STORAGE_ACCESS_ERROR:
                message = res.getString(R.string.error_sdcard_access);
                break;
            case Recorder.IN_CALL_RECORD_ERROR:
                // TODO: update error message to reflect that the recording
                // could not be
                // performed during a call.
            case Recorder.INTERNAL_ERROR:
                message = res.getString(R.string.error_app_internal);
                break;
        }
        //内部错误在6.0以下==没有权限
        if (Recorder.INTERNAL_ERROR == error) {
            if (mvpView != null) mvpView.showNoPer();
        } else if (mvpView != null) mvpView.alertError(message);
    }

    public void changeWindowStyle(Activity activity, int dpHeight) {
        WindowManager.LayoutParams wParams = activity.getWindow().getAttributes();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);     // 调整至底部/水平居中

        wParams.height = dpHeight;                                   // 设置高度
        wParams.width = DensityUtil.getXScreenpx(activity);                              // 设置宽度
        activity.getWindow().setAttributes(wParams);
    }

    /**
     * 得到录音文件
     *
     * @return
     */
    public File getSampleFile() {
        File file = mRecorder.sampleFile();
        if (file != null && file.exists() && file.length() > 0) {
            return file;
        }

        return null;
    }
}
