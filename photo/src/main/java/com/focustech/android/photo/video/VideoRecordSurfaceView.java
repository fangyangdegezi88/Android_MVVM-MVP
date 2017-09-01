package com.focustech.android.photo.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.focustech.android.commonlibs.bridge.cache.localstorage.LocalFileStorageManager;

import java.io.IOException;

/**
 * 录制视频
 *
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/23]
 * @see [相关类/方法]
 * @since [V1]
 */
public class VideoRecordSurfaceView extends SurfaceView implements SurfaceHolder.Callback, MediaRecorder.OnErrorListener {
    private static final String TAG = "VideoSurfaceView";
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    /**
     * 标记，判断当前是否正在录制
     */
    private boolean isRecording = false;
    /**
     * 录制
     */
    private MediaRecorder mRecorder;
    /**
     * 路径保存
     */
    private String mVideoFilePath;

    private VideoRecordCallBack mRecordCallBack;

    public VideoRecordSurfaceView(Context context) {
        this(context, null);
    }

    public VideoRecordSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoRecordSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoRecordSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public void setCallBack(VideoRecordCallBack mRecordCallBack) {
        this.mRecordCallBack = mRecordCallBack;
    }


    public void initVideoRecord() {
        //配置SurfaceHodler
        mSurfaceHolder = getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(this);//回调接口
    }

    /**
     * 开始录制视频
     */
    public void startRecord() {
        initCamera();
        mCamera.unlock();
        setConfigRecord();
        //开始录制
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            if (mRecordCallBack != null) {
                mRecordCallBack.initRecorderFail();
            }
            e.printStackTrace();
            return;
        }
        try {
            mRecorder.start();
        } catch (Exception e) {
            if (mRecordCallBack != null) {
                mRecordCallBack.initRecorderFail();
            }
            e.printStackTrace();
            return;
        }
        if (mRecordCallBack != null) {
            mRecordCallBack.onRecordStart();
        }
        isRecording = true;
//        mRecordTime.setBase(SystemClock.elapsedRealtime());
//        mRecordTime.start();
    }

    /**
     * 停止录制视频
     */
    public void stopRecord() {
        if (isRecording && mRecorder != null) {
            // 设置后不会崩
            mRecorder.setOnErrorListener(null);
            mRecorder.setPreviewDisplay(null);
            mRecorder.setOnInfoListener(null);
            //停止录制
            try {
                mRecorder.stop();
                mRecorder.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //释放资源
                mRecorder.release();
                mRecorder = null;
            }

            if (mRecordCallBack != null) {
                mRecordCallBack.onRecordEnd(mVideoFilePath);
            }
//            mRecordTime.stop();
            isRecording = false;
        }
        mCamera.lock();
        stopCamera();
    }

    /**
     * 停止录制视频
     */
    public void cancelRecord() {
        if (isRecording && mRecorder != null) {
            // 设置后不会崩
            mRecorder.setOnErrorListener(null);
            mRecorder.setPreviewDisplay(null);
            mRecorder.setOnInfoListener(null);
            //停止录制
            try {
                mRecorder.stop();
                mRecorder.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //释放资源
                mRecorder.release();
                mRecorder = null;
            }
//            mRecordTime.stop();
            isRecording = false;
        }
        mCamera.lock();
        stopCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = getHolder();
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera == null) return;
        surfaceHolder.removeCallback(this);     //http://blog.csdn.net/u010665691/article/details/46633801
        stopCamera();
    }

    /**
     * 初始化摄像头
     */
    private void initCamera() {
        if (mCamera != null) {
            stopCamera();
        }
        try {
            //默认启动后置摄像头
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            if (mCamera == null) {
                Toast.makeText(mContext, "未能获取到相机！", Toast.LENGTH_SHORT).show();
                return;
            }
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            setCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            if (mRecordCallBack != null) {
                mRecordCallBack.initCameraFail();
            }
        }
    }

    /**
     * 释放摄像头资源
     */
    private void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 设置摄像头为竖屏
     */
    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            //设置相机的横竖屏(竖屏需要旋转90°)
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                params.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
            } else {
                params.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
            }
            //设置聚焦模式
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            //缩短Recording启动时间
            params.setRecordingHint(true);
            //影像稳定能力
            if (params.isVideoStabilizationSupported())
                params.setVideoStabilization(true);
            mCamera.setParameters(params);
        }
    }

    /**
     * 配置MediaRecorder()
     */
    private void setConfigRecord() {
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setCamera(mCamera);
        mRecorder.setOnErrorListener(this);

        //使用SurfaceView预览
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mRecorder.setVideoEncodingBitRate(1024 * 1024);
        mRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mRecorder.setOrientationHint(90);
        //设置录像的分辨率
        mRecorder.setVideoSize(352, 288);

        //设置录像视频保存地址
        String videoName = getVideoName();
        LocalFileStorageManager localFileStorageManager = LocalFileStorageManager.getInstance();
        String filePath = localFileStorageManager.getCacheVideoFilePath();//视频保存路径
        mVideoFilePath = filePath + videoName;
        mRecorder.setOutputFile(mVideoFilePath);
    }

    private String getVideoName() {
        return System.currentTimeMillis() + ".mp4";
    }

    @Override
    public void onError(MediaRecorder mediaRecorder, int what, int extra) {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mRecordCallBack != null) {
            mRecordCallBack.onRecordError();
        }
    }
}
