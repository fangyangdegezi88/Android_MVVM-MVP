package com.focustech.android.photo.video;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.focustech.android.commonuis.base.BaseMVVMActivity;
import com.focustech.android.commonuis.view.header.SFActionBar;
import com.focustech.android.photo.R;

import static com.focustech.android.photo.PhotoConstants.BundleKey.VIDEO_FILE_DISPLAY_NAME;
import static com.focustech.android.photo.PhotoConstants.BundleKey.VIDEO_FILE_PATH_OR_URL;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]  播放视频 需要视频路径的参数，用path变量传递过来
 * @since [V1]
 */
public class PlayVideoActivity extends BaseMVVMActivity implements MediaPlayer.OnPreparedListener ,CountTimer.CountDownCallBack {
    private SFActionBar mSfHeader;
	private VideoView videoView;
	private MediaController mController;
	private int position;
	private Uri mUri;
	boolean isDestroy = false;

    /**
     * 计时器
     */
    private CountTimer mCDT;

	/**
	 * 初始化数据
	 */
    @Override
    public void initData(Bundle bundle) {
        mSfHeader = (SFActionBar) findViewById(R.id.sf_header);
        videoView = (VideoView) this.findViewById(R.id.videoView);

        String path = bundle.getString(VIDEO_FILE_PATH_OR_URL);
        String name = bundle.getString(VIDEO_FILE_DISPLAY_NAME,"");
        mSfHeader.setActionTitle(name);

        mController = new MediaController(this);
        videoView.setMediaController(mController);
        mUri = Uri.parse(path);
        videoView.setVideoURI(mUri);
        videoView.requestFocus();
        mLayerHelper.showProgressDialog(R.string.loading);
        mLayerHelper.setProgressDialogCancelable(true);
        mController.show();
        initListener();
        mCDT = new CountTimer(30*1000, this);
        mCDT.start();
        mCDT.mIsRuning = true;
    }

    private void initListener(){
        mSfHeader.setSFActionBarListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isDestroy) {
                    videoView.seekTo(0);
                    mController.show();
                }
            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int arg1, int arg2) {
                videoLoadError();
                return false;
            }
        });
    }

    private void videoLoadError(){
        if (!isDestroy) {
            mLayerHelper.hideProgressDialog();
            mLayerHelper.showToastFail(getString(R.string.video_load_fail));
            videoView.seekTo(videoView.getDuration());
            mController.show();
        }
    }

	@Override
	protected void onDestroy() {
        if(mCDT.mIsRuning){
            mCDT.cancel();
        }
        isDestroy = true;
        super.onDestroy();
	}

    @Override
    public int getSfHeaderId() {
        return R.id.sf_header;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

	@Override
	protected void onPause() {
		position = videoView.getCurrentPosition();
		videoView.stopPlayback();
		super.onPause();
	}

	public void onResume() {
		// Resume video player
		if (position >= 0) {
			videoView.seekTo(position);
			position = -1;
		}
		super.onResume();
	}

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(mCDT.mIsRuning){
            mCDT.cancel();
        }
        mLayerHelper.hideProgressDialog();
        videoView.start();
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        mLayerHelper.hideProgressDialog();
        finish();
    }

    @Override
    public void leftBtnClick() {
        onBackPressed();
    }

    @Override
    public void onFinish() {
        videoLoadError();
        videoView.stopPlayback();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    /*
	 * @Override protected void onRestart() { if (position == 0) { position =
	 * videoView.getDuration(); } videoView.seekTo(position); super.onRestart();
	 * }
	 */
}