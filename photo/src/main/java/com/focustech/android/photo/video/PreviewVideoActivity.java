package com.focustech.android.photo.video;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.base.BaseMVVMActivity;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.focustech.android.photo.PhotoConstants;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.MediaPicker;

import java.io.File;

import static com.focustech.android.photo.PhotoConstants.BundleKey.BROADCAST_CAMERA_PATH;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]  视频预览界面   这个界面需要有网络交互
 * @since [V1]
 */
public class PreviewVideoActivity extends BaseMVVMActivity implements MediaPlayer.OnPreparedListener ,View.OnClickListener{
    public static final String TAG = "VideoPreviewActivity";
    private MyVideoView videoView;
    private String mVideoPath;

    private Button mSureBtn,mCancelBtn;

    private boolean checkSingleMaxSize;


    @Override
    public int getSfHeaderId() {
        return 0;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
    }


    @Override
    public void initData(Bundle bundle) {
        mSureBtn = (Button)findViewById(R.id.record_finish);
        mSureBtn.setOnClickListener(this);
        mCancelBtn = (Button)findViewById(R.id.record_retry);
        mCancelBtn.setOnClickListener(this);

        mVideoPath = bundle.getString(PhotoConstants.BundleKey.VIDEO_FILE_PATH_OR_URL);
        checkSingleMaxSize = bundle.getBoolean(PhotoConstants.BundleKey.CHECK_SINGLE_FILE_MAX_SIZE,false);

        final File sourceVideoFile = new File(mVideoPath);
        videoView = (MyVideoView) findViewById(R.id.videoView);
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        int screenH = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = screenW;
        params.height = screenH;
        videoView.setLayoutParams(params);

        videoView.setOnPreparedListener(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            videoView.setVideoURI(Uri.fromFile(sourceVideoFile));
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVideoURI(Uri.fromFile(sourceVideoFile));
                videoView.start();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_preview;
    }


    /**
     * 发送广播
     *
     * @param filePath
     */
    private void sendBroadcast(String filePath) {
        Intent intent = new Intent();
        intent.setAction((String) BuildConfigUtil.getBuildConfigValue(getBaseContext(), "APPLICATION_ID") + ".record_action");
        intent.putExtra(BROADCAST_CAMERA_PATH, filePath);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View view) {
        if(view == mCancelBtn){
            finish();
        }else if(view == mSureBtn){
            if(checkSingleMaxSize){
                if(GeneralUtils.isNotNullOrEmpty(mVideoPath)){
                    File videoFile = new File(mVideoPath);
                    if(videoFile.length() > MediaPicker.SINGLE_UPLOAD_FILE_MAX_SIZE){
                        showFileReachMaxSize();
                        return;
                    }
                }
            }
            //存储到系统相册
            insertVideoIntoSystemDB(mVideoPath);
            sendBroadcast(mVideoPath);
            setResult(RESULT_OK);
            finish();
        }
    }

    private void showFileReachMaxSize(){
        if (mLayerHelper != null){
            mLayerHelper.showAlert(getString(R.string.upload_file_reach_max_size), SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE);
            mLayerHelper.getAlertDialog().setSingleBtnText(getString(R.string.i_know));
            mLayerHelper.getAlertDialog().setSFAlertDialogListener(new SFAlertDialog.SFAlertDialogListener() {
                @Override
                public void clickCancel(int tag) {

                }

                @Override
                public void clickOk(String input, int tag) {

                }

                @Override
                public void singleOk(String input, int tag) {
                    finish();
                }
            });
        }
    }

    /**
     * 视频信息通过ContentResolver插入到系统相册
     * @param filePath
     */
    private void insertVideoIntoSystemDB(String filePath) {
        File file = new File(filePath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, file.getName());
        values.put(MediaStore.Video.Media.DISPLAY_NAME, file.getName());
        values.put(MediaStore.Video.Media.DATA, filePath);
        values.put(MediaStore.Video.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Video.Media.SIZE, file.length());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");

        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

}
