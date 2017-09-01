package com.focustech.android.photo.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.DensityUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <拍照view>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/8]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private L l = new L(CameraSurfaceView.class);

    private static final String TAG = "CameraSurfaceView";
    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;
    /**
     * 手机屏幕的宽度
     */
    private int mScreenWidth;
    /**
     * 手机屏幕的高度
     */
    private int mScreenHeight;
    /**
     * 相机屏幕的高度
     */
    private int mCameraHeight;

    /**
     * 拍得的图片文件
     */
    private byte[] picData;

    /**
     * 预览二进制数据，vivo手机禁用相机权限，但没有报RuntimeException，抓取不到权限被禁用的情况，因此添加 mCamera.setPreviewCallback，结合定时器，如果2s数据还是null,则认为是没有权限
     */
    private byte[] preData;

    private Handler handler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (null == preData && null != mInitCallBack)
                mInitCallBack.initFail();
        }
    };
    /**
     * 相机初始化回调
     */
    private CameraInitCallBack mInitCallBack;

    private static ExecutorService threads = Executors.newFixedThreadPool(1);

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        getScreenMetrix(context);
    }

    /**
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        l.d(TAG + "---surfaceCreated");

        if (mCamera == null) {
            try {
                l.d(TAG + "---mCamera null---");
                int cameraCount = Camera.getNumberOfCameras();
                //没有摄像头
                if (cameraCount <= 0) {
                    return;
                }
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                //是否有前置摄像头
                boolean hasFront = false;
                int frontIndex = -1;
                for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                    Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                        mCamera = Camera.open();//开启后置相机

                        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data, Camera camera) {
                                preData = data;
                            }
                        });
                        mCamera.setPreviewDisplay(holder);
                        if (null != handler)
                            handler.removeCallbacks(runnable);
                        handler = new Handler();
                        handler.postDelayed(runnable, 2 * 1000);

                        break;
                    }

                    //前置摄像头，只有在没有后置摄像头的时候才会用
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        hasFront = true;
                        frontIndex = camIdx;
                    }
                }
                if (null == mCamera && hasFront) {
                    //如果mCamera是null,说明没有后置摄像头
                    mCamera = Camera.open(frontIndex);
                    mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
                //提示用户开启camera权限
                if (null != mInitCallBack) mInitCallBack.initFail();
            }
        }
    }

    @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        l.d(TAG + "---surfaceChanged");
        if (mCamera == null) return;
        try {
            setCameraParams(mCamera, mScreenWidth, mScreenHeight, mCameraHeight);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        l.d(TAG + "---surfaceDestroyed");
        if (mCamera == null) return;
        surfaceHolder.removeCallback(this);     //http://blog.csdn.net/u010665691/article/details/46633801
        cameraDestory();
    }

    /**
     * 设置相机参数
     *
     * @param camera
     * @param width
     * @param height
     */
    private void setCameraParams(Camera camera, int width, int height, int cameraH) {
        l.d(TAG + "---setCameraParams width=" + width + " height=" + height);
        Camera.Parameters parameters = mCamera.getParameters();
        //------------------------------------
        this.setLayoutParams(new LinearLayout.LayoutParams(width, cameraH));
        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        List<Camera.Size> picSizeList = parameters.getSupportedPictureSizes();
        Camera.Size preSize = null;
        Camera.Size picSize = null;
        try {
            //preview size
            preSize = getPreSize(previewSizeList, width, height);
            if (null == preSize) {
                l.d(TAG + "---preSize.width=" + preSize.width + " preSize.height=" + preSize.height);
                preSize = parameters.getPreviewSize();
            } else {
                Camera.Size size = parameters.getPreviewSize();
                if (preSize.width < size.width || preSize.height < size.height)
                    preSize = size;
            }
            parameters.setPreviewSize(preSize.width, preSize.height);

            //picture size
            picSize = getPicSize(picSizeList);
            if (null == picSize) {
                l.d(TAG + "---picSize.width=" + preSize.width + " preSize.height=" + preSize.height);
                picSize = parameters.getPictureSize();
            }

            parameters.setPictureSize(picSize.width, picSize.height);
            parameters.setJpegQuality(100); // 设置照片质量
            if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            }
            mCamera.cancelAutoFocus();//自动对焦。
            mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择小于屏幕宽高的最大的一个previewSize
     *
     * @param pictureSizeList
     * @param width
     * @param heigth
     * @return
     */
    private Camera.Size getPreSize(List<Camera.Size> pictureSizeList, float width, float heigth) {
        Camera.Size result = null;
        Collections.sort(pictureSizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.height < rhs.height) {
                    return 1;
                }
                if (lhs.height == rhs.height) {
                    if (lhs.width < rhs.width) {
                        return 1;
                    }
                    if (lhs.width == rhs.width) {
                        return 0;
                    }
                    if (lhs.width > rhs.width) {
                        return -1;
                    }
                }
                return -1;
            }
        });

        for (Camera.Size size : pictureSizeList) {
            if (size.width < size.height) {
                if (size.width <= width && size.height <= heigth) {
                    result = size;
                    break;
                }
            } else {
                if (size.width <= heigth && size.height <= width) {
                    result = size;
                    break;
                }
            }
        }
        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }
        return result;
    }

    private Camera.Size getPicSize(List<Camera.Size> pictureSizeList) {
        if (null == pictureSizeList || pictureSizeList.size() == 0)
            return null;
        Collections.sort(pictureSizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.height < rhs.height) {
                    return 1;
                }
                if (lhs.height == rhs.height) {
                    return 0;
                }
                return -1;
            }
        });
//        for (Camera.Size size : pictureSizeList) {
//            int w = size.width;
//            int h = size.height;
//            if (unUsefulPicSize.containsKey(w) && h == unUsefulPicSize.get(w)) {
//                continue;
//            }
//            result = size;
//            break;
//        }
        return pictureSizeList.get(0);
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
    }

    /**
     * 创建jpeg图片回调数据对象
     */
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            picData = data;
            //拍完照片之后，停止预览
            mCamera.stopPreview();
        }
    };


    /**
     * 获取屏幕的宽高
     *
     * @param context
     */
    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        mCameraHeight = mScreenHeight - DensityUtil.dip2px(mContext, 100);
    }

    /**
     * 相机预览
     */
    public void cameraPreview() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.startPreview();
        } else {
            cameraInit();
        }
    }

    /**
     * 相机销毁
     */
    public void cameraDestory() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
        }
        mCamera = null;
        holder = null;
    }

    /**
     * 相机初始化
     */
    public void cameraInit() {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
        holder.setKeepScreenOn(true);
    }

    /**
     * 外部调用拍照
     */
    public void takePhoto() {
        if (mCamera == null) return;
        mCamera.takePicture(null, null, jpeg);
    }

    /**
     * 保存图片
     *
     * @param cameraCallback 文件写入回调
     */
    public void savePhoto(CameraWriteCallback cameraCallback) {
        threads.execute(new CameraTask(picData, cameraCallback));
    }


    /**
     * 设置相机初始化回调
     *
     * @param mInitCallBack
     */
    public void setInitCallBack(CameraInitCallBack mInitCallBack) {
        this.mInitCallBack = mInitCallBack;
    }

    /**
     * 图片写入本地Callback
     */
    public interface CameraWriteCallback {
        /**
         * 写入成功
         */
        public void writeSuccess(String filePath);

        /**
         * 写入失败,需要重新开启预览
         */
        public void writeFail();
    }

    /**
     * 相机初始化Callback
     */
    public interface CameraInitCallBack {
        /**
         * 初始化失败
         */
        public void initFail();

    }

}