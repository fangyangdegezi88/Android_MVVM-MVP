package com.focustech.android.photo.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.crash.FocusPackage;
import com.focustech.android.commonuis.base.BaseMVVMPermissionActivity;
import com.focustech.android.commonuis.bean.PermissionCode;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.focustech.android.photo.PhotoConstants;
import com.focustech.android.photo.R;

import java.lang.ref.WeakReference;

import static com.focustech.android.photo.PhotoConstants.BundleKey.VIDEO_FILE_PATH_OR_URL;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]  视频录制界面   通过广播将路径发送出去  然后在预览界面如果上传结束  则再次通过广播告诉当前界面 关闭
 * @since [V1]
 */
public class RecordVideoActivity extends BaseMVVMPermissionActivity implements View.OnClickListener, VideoRecordCallBack {

    private static final String TAG = "VideoRecordActivity";
    public static final int CONTROL_CODE = 1;
    //UI
    private ImageView mRecordControl;
    private VideoRecordSurfaceView mRcoder;
    private TextView mCancel;

    private Chronometer mRecordTime;

    //DATA
    private boolean isRecording = false;// 标记，判断当前是否正在录制

    /**
     * 是否需要重新布局
     */
    private boolean needReLayout = true;
    /**
     * 应用是否正在运行
     */
    private boolean mActivityIsStart = false;

    private boolean showRationale = false;
    private boolean showAudioRationale = false;

    private boolean checkSingleMaxSize = false;

    private Handler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<RecordVideoActivity> mActivity;

        public MyHandler(RecordVideoActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case CONTROL_CODE:
                    mActivity.get().mRecordControl.setEnabled(true);
                    break;
            }
        }
    }

    String app_name;

    @Override
    public void initData(Bundle bundle) {
        if (null != bundle){
            app_name = FocusPackage.newBuilder().getAppName();
            checkSingleMaxSize = bundle.getBoolean(PhotoConstants.BundleKey.CHECK_SINGLE_FILE_MAX_SIZE,false);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera_nopermission;
    }

    @Override
    public String getName() {
        return "录像";
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
    protected void onStart() {
        super.onStart();
        reqPermission(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
    }

    @Override
    public void hasPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            reqPermission(Manifest.permission.RECORD_AUDIO, PermissionCode.PERMISSION_RECORDER);
            return;
        }

        if (needReLayout) {
            setContentView(R.layout.activity_record_video);
            mRcoder = (VideoRecordSurfaceView) findViewById(R.id.record_surfaceView);
            getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mActivityIsStart = true;
                }
            });
            //设置初始化回调
            mRecordControl = (ImageView) findViewById(R.id.record_control);
            mRecordTime = (Chronometer) findViewById(R.id.record_time);
            mCancel = (TextView) findViewById(R.id.record_cancel);
            mRecordControl.setOnClickListener(this);
            mCancel.setOnClickListener(this);
            mRcoder.setCallBack(this);
            needReLayout = false;
        }
        mRcoder.initVideoRecord();
    }

    @Override
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //用户不同意，向用户展示该权限作用
        if(requestCode == PermissionCode.PERMISSION_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission();
                return;
            }
            showPermissionRationale(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
        }
        if(requestCode == PermissionCode.PERMISSION_RECORDER){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission();
                return;
            }
            showPermissionRationale(Manifest.permission.RECORD_AUDIO, PermissionCode.PERMISSION_RECORDER);
        }

    }

    @Override
    public void showPermissionRationale(String permission, int code) {
        if (isFinishing())
            return;
        if(permission.equals(Manifest.permission.CAMERA)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showRationale = true;
                String content = String.format(getString(R.string.permission_tip_none), "相机", "方便您使用拍照功能", app_name);
                mLayerHelper.showAlert("获取相机", content);
                mLayerHelper.getAlertDialog().setCancelText("取消");
                mLayerHelper.getAlertDialog().setOKText("确定");
            } else {
                showRationale = false;
                String content = String.format(getString(R.string.permission_tip_new), "相机", "方便您使用拍照功能", app_name, app_name);
                mLayerHelper.showAlert("获取相机", content);
                mLayerHelper.getAlertDialog().setCancelText("取消");
                mLayerHelper.getAlertDialog().setOKText("去设置");
            }
            mLayerHelper.getAlertDialog().setTag(code);
            mLayerHelper.getAlertDialog().setCancelable(false);
            mLayerHelper.getAlertDialog().setCanceledOnTouchOutside(false);
            mLayerHelper.getAlertDialog().setSFAlertDialogListener(new SFAlertDialog.SFAlertDialogListener() {
                @Override
                public void clickCancel(int tag) {
                    finish();
                }

                @Override
                public void clickOk(String input, int tag) {
                    if (showRationale) {
                        reqPermission(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
                    } else {
                        jumpToPermissionSetting(RecordVideoActivity.this, (String) BuildConfigUtil.getBuildConfigValue(getBaseContext(), "APPLICATION_ID"));
                        finish();
                    }
                }

                @Override
                public void singleOk(String input, int tag) {

                }
            });
            mLayerHelper.getAlertDialog().show();
        } else if(permission.equals(Manifest.permission.RECORD_AUDIO)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                showAudioRationale = true;
                String content = String.format(getString(R.string.permission_tip_none), "麦克风", "方便您使用语音功能",app_name);
                mLayerHelper.showAlert("获取麦克风权限", content);
                mLayerHelper.getAlertDialog().setCancelText("取消");
                mLayerHelper.getAlertDialog().setOKText("确定");
            } else {
                showAudioRationale = false;
                String content = String.format(getString(R.string.permission_tip_new), "麦克风", "方便您使用语音功能", app_name, app_name);
                mLayerHelper.showAlert("获取麦克风权限", content);
                mLayerHelper.getAlertDialog().setCancelText("取消");
                mLayerHelper.getAlertDialog().setOKText("设置");
            }
            mLayerHelper.getAlertDialog().setTag(code);
            mLayerHelper.getAlertDialog().setCancelable(false);
            mLayerHelper.getAlertDialog().setCanceledOnTouchOutside(false);
            mLayerHelper.getAlertDialog().setSFAlertDialogListener(new SFAlertDialog.SFAlertDialogListener() {
                @Override
                public void clickCancel(int tag) {
                    finish();
                }

                @Override
                public void clickOk(String input, int tag) {
                    jumpToPermissionSetting(RecordVideoActivity.this, (String) BuildConfigUtil.getBuildConfigValue(getBaseContext(), "APPLICATION_ID"));
                    finish();
                }

                @Override
                public void singleOk(String input, int tag) {

                }
            });
            mLayerHelper.getAlertDialog().show();
        }
    }

    /**
     * 开始录制视频
     */
    public void startRecord() {
        mRcoder.startRecord();
    }

    /**
     * 停止录制视频
     */
    public void stopRecord() {
        if (isRecording) {
            mRcoder.stopRecord();
        }
    }

    public void cancelRecord(){
        if (isRecording) {
            mRcoder.cancelRecord();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.record_control) {
            if (isRecording) {
                mRecordControl.setImageResource(R.drawable.recordvideo_start);
                stopRecord();
                mRecordTime.stop();
                mRecordTime.setBase(SystemClock.elapsedRealtime());
                isRecording = false;
            } else {
                //开始录制视频
                startRecord();
                mRecordControl.setImageResource(R.drawable.recordvideo_stop);
                mRecordControl.setEnabled(false);//1s后才能停止
                mHandler.sendEmptyMessageDelayed(CONTROL_CODE, 1000);
            }
        } else if(i == R.id.record_cancel){
            finish();
        }
    }

    @Override
    public void initRecorderFail() {
        isRecording = false;
        mRecordControl.setImageResource(R.drawable.recordvideo_start);
        cancelRecord();
        //提示用户初始化失败，没有权限
        showPermissionRationale(Manifest.permission.RECORD_AUDIO, PermissionCode.PERMISSION_RECORDER);
    }

    @Override
    public void initCameraFail() {
        isRecording = false;
        mRecordControl.setImageResource(R.drawable.recordvideo_start);
        //提示用户初始化失败，没有权限
        showPermissionRationale(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
    }

    @Override
    public void onRecordStart() {
        isRecording = true;
        mRecordTime.setBase(SystemClock.elapsedRealtime());
        mRecordTime.start();
    }

    @Override
    public void onRecordEnd(String videoPath) {
        isRecording = false;
        mRecordTime.stop();

        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_FILE_PATH_OR_URL, videoPath);
        bundle.putBoolean(PhotoConstants.BundleKey.CHECK_SINGLE_FILE_MAX_SIZE,checkSingleMaxSize);
        startActivityForResult(PreviewVideoActivity.class, PhotoConstants.ActivityRequestCode.REQUEST_PREVIEW_VIDEO, bundle);
    }

    @Override
    public void onRecordError() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoConstants.ActivityRequestCode.REQUEST_PREVIEW_VIDEO:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
    }
}
