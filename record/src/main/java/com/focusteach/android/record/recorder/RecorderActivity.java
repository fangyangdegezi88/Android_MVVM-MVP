package com.focusteach.android.record.recorder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focusteach.android.record.R;
import com.focusteach.android.record.biz.IRecorderView;
import com.focusteach.android.record.biz.RecorderPresenter;
import com.focusteach.android.record.view.AlignTextView;
import com.focusteach.android.record.view.WaveWheelView;
import com.focusteach.android.record.view.voice.RecorderView;
import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.crash.FocusPackage;
import com.focustech.android.commonuis.base.BaseActivity;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;

import java.io.File;

import static com.focusteach.android.record.recorder.OpenRecorder.AUDIO_MAX_LENGTH;
import static com.focusteach.android.record.recorder.OpenRecorder.DIALOG_HEIGHT_PX;
import static com.focusteach.android.record.recorder.OpenRecorder.OUTPUT_FILE_DIR;

/**
 * use:
 * record the audio and save the file to the target path for output file
 * <p>
 * how to call this Activity:
 * Intent intent = new Intent();
 * intent.setClass(this, RecorderActivity.class);
 * startActivityForResult(intent, Constants.ACTIVITY_REQUEST_CODE.RECORDER);
 * optional params:
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/8]
 * @see OpenRecorder#DIALOG_HEIGHT_PX
 * intent.putExtra(DIALOG_HEIGHT_PX, 253);  // int
 * @see OpenRecorder#OUTPUT_FILE_DIR
 * intent.putExtra(OUTPUT_FILE_DIR, directory);  // String
 * @see OpenRecorder#AUDIO_MAX_LENGTH
 * intent.putExtra(AUDIO_MAX_LENGTH, maxLength);  // int(second)
 * or
 * @see OpenRecorder
 * <p>
 * <p>
 * what return:
 * onActivityResult
 * success：
 * Uri data = Uri.parse(path.getAbsolutePath());
 * getIntent().setData(data);
 * setResult(RESULT_OK, getIntent());
 * @see [相关类/方法]
 * @since [V1]
 */
public class RecorderActivity extends BaseActivity<RecorderPresenter> implements IRecorderView, View.OnClickListener, SFAlertDialog.SFAlertDialogListener {

    private LinearLayout rootLayout;
    /**
     * 计时器所在容器
     */
    private LinearLayout mTimerContainer;
    /**
     * 左半边声浪的容器
     */
    private LinearLayout mLeftWaveContainer;
    /**
     * 右半边声浪的容器
     */
    private LinearLayout mRightWaveContainer;
    /**
     * 电子钟，用于显示录音时长
     */
    private AlignTextView mTimerTv;
    /**
     * 左声浪视图
     */
    private WaveWheelView mLeftWave;
    /**
     * 右声浪视图
     */
    private WaveWheelView mRightWave;
    /**
     * 停止录音按钮
     */
    private ImageView mStopIv;
    /**
     * 剩余时间
     */
    private TextView mRemainingTimeTv;

    public static final int NO_RECORD_PERMISSION = 201;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recorder;
    }

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        mTimerContainer = (LinearLayout) findViewById(R.id.audio_length_container);
        mLeftWaveContainer = (LinearLayout) findViewById(R.id.left_wave_container);
        mRightWaveContainer = (LinearLayout) findViewById(R.id.right_wave_container);
        mStopIv = (ImageView) findViewById(R.id.stop_recorder_iv);
        mRemainingTimeTv = (TextView) findViewById(R.id.remaining_time_tv);

        initAudioLength();
        initWave();
        mStopIv.setImageDrawable(RecorderView.newStopRecordBtnSelector(this));  // 停止按钮样式

    }

    @Override
    public void initListeners() {
        mStopIv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter = new RecorderPresenter();
        presenter.attachView(this);

        String requestDir = getIntent().getStringExtra(OUTPUT_FILE_DIR);
        int requestMaxSecond = getIntent().getIntExtra(AUDIO_MAX_LENGTH, -1);
        int dialogHeight = getIntent().getIntExtra(DIALOG_HEIGHT_PX, DensityUtil.dip2px(this,253));

        rootLayout.getLayoutParams().height = dialogHeight;

        presenter.changeWindowStyle(this, dialogHeight);

        presenter.initRecorder(getResources(), requestMaxSecond, requestDir);

        presenter.registerExternalStorageListener(this);

        presenter.startRecording();
    }

    /**
     * 初始化左右声浪
     */
    private void initWave() {
        mLeftWave = new WaveWheelView(this);
        mLeftWave.setReverse(true);
        mLeftWaveContainer.addView(mLeftWave);
        mRightWave = new WaveWheelView(this);
        mRightWaveContainer.addView(mRightWave);
    }

    /**
     * 初始化录音计时
     */
    public void initAudioLength() {
        mTimerTv = new AlignTextView(this);

        mTimerTv.setText("0:00");
        mTimerTv.setTextSizeDp(14);
        mTimerTv.setTextAlign(AlignTextView.TEXT_ALIGN_CENTER_HORIZONTAL | AlignTextView.TEXT_ALIGN_CENTER_VERTICAL);
        mTimerTv.setTextColor(getResources().getColor(R.color.app_support_txt_color));
        mTimerContainer.addView(mTimerTv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.recordOk();
            setData();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.unregisterExternalStorageListener(this);
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    public String getName() {
        return getString(R.string.module_recorder);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.stop_recorder_iv) {
            presenter.recordOk();
            setData();
            finish();

        }
    }

    /**
     * 设置返回数据
     */
    private void setData() {
        if (presenter != null) {
            File path = presenter.getSampleFile();
            int time = 0;
            if (mTimerTv.getTag() != null) { // 时长存储再mTimerTv中
                Long tag = (Long) mTimerTv.getTag();
                time = tag.intValue();
            }
            if (path != null && GeneralUtils.isNotNullOrEmpty(path.getAbsolutePath())) {
                Uri data = Uri.parse(path.getAbsolutePath());
                getIntent().setData(data);
                getIntent().putExtra(OpenRecorder.RESULT_AUDIO_LENGTH_SEC, time);
                setResult(RESULT_OK, getIntent());
            }
        }
    }

    @Override
    public int getSfHeaderId() {
        return 0;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 更新录音进行到的时间
     *
     * @param time
     * @param timeStr
     */
    @Override
    public void updateRecorderLength(long time, String timeStr) {
        mTimerTv.setTag(time);  // 把时长放入到tag中
        mTimerTv.setText(timeStr);
    }

    @Override
    public void updateRemainingTime(SpannableString timeStr) {
        mRemainingTimeTv.setText(timeStr);
    }

    @Override
    public void alertError(String message) {
        ToastUtil.showFocusToastWithoutActivity(this, message);

        finish();
    }

    @Override
    public void alertNoPermission() {
        mLayerHelper.showAlert(String.format(getString(R.string.please_give_authority_to_record_audio), FocusPackage.getInstance().getAppName())
                , SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE);
        mLayerHelper.setAlertDialogCancelable(false);
    }

    @Override
    public void showNoPer() {
        setResult(NO_RECORD_PERMISSION);
        finish();
    }

    @Override
    public void recordTimeout() {
        mLayerHelper.showToastFail(getString(R.string.max_length_reached));
        setData();
        finish();
    }

    @Override
    public void updateDb(double db) {
        mLeftWave.addDB(db);
        mRightWave.addDB(db);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override
    public void clickCancel(int tag) {
    }

    @Override
    public void clickOk(String input, int tag) {
    }

    @Override
    public void singleOk(String input, int tag) {
        this.finish();
    }

}
