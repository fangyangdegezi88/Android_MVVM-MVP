package com.focustech.android.photo.camera;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.crash.FocusPackage;
import com.focustech.android.commonuis.base.BaseMVVMPermissionActivity;
import com.focustech.android.commonuis.bean.PermissionCode;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.focustech.android.photo.PhotoConstants;
import com.focustech.android.photo.R;

import java.io.File;

import static com.focustech.android.photo.PhotoConstants.BundleKey.BROADCAST_CAMERA_PATH;
import static com.focustech.android.photo.PhotoConstants.BundleKey.OPEN_CAMERA_ACTION;

/**
 * <相机拍照页面>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/12]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CameraActivity extends BaseMVVMPermissionActivity implements View.OnClickListener, SFAlertDialog.SFAlertDialogListener {

    /**
     * 相机view
     */
    CameraSurfaceView mPhotoView;

    /**
     * 拍照container
     */
    RelativeLayout mCameraC;
    /**
     * 拍照取消
     */
    TextView mCameraCancel;
    /**
     * 拍照按钮
     */
    Button mCameraBtn;
    /**
     * 拍照完成Container
     */
    LinearLayout mCameraResultC;
    /**
     * 重拍
     */
    Button mCameraRetry;
    /**
     * 完成按钮
     */
    Button mCameraFinish;

    /**
     * 是否需要重新布局
     */
    private boolean needReLayout = true;

    private boolean showRationale = false;

    /**
     * 应用是否正在运行
     */
    private boolean mActivityIsStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean checkHasLoading(AppCompatActivity activity) {
        return true;
    }

    @Override
    public int getSfHeaderId() {
        return 0;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

    String app_name;
    String camera_action;

    @Override
    public void initData(Bundle bundle) {
        if (null != bundle) {
            app_name = FocusPackage.newBuilder(this).getAppName();
            camera_action = bundle.getString(OPEN_CAMERA_ACTION);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera_nopermission;
    }

    @Override
    public String getName() {
        return "相机拍摄";
    }

    @Override
    protected void onStart() {
        super.onStart();
        reqPermission(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
    }

    @Override
    public void hasPermission() {
        if (needReLayout) {
            setContentView(R.layout.activity_camera);
            mPhotoView = (CameraSurfaceView) findViewById(R.id.camera_view);
            getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mActivityIsStart = true;
                }
            });
            //设置初始化回调
            mPhotoView.setInitCallBack(new CameraSurfaceView.CameraInitCallBack() {
                @Override
                public void initFail() {
                    //提示用户初始化失败，没有权限
                    if (!mActivityIsStart) {
                        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                showPermissionRationale(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
                            }
                        });
                    } else {
                        showPermissionRationale(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
                    }
                }
            });
            mCameraC = (RelativeLayout) findViewById(R.id.take_photo_container);
            mCameraCancel = (TextView) findViewById(R.id.take_photo_cancel);
            mCameraBtn = (Button) findViewById(R.id.take_photo);
            mCameraResultC = (LinearLayout) findViewById(R.id.take_photo_result);
            mCameraRetry = (Button) findViewById(R.id.take_photo_retry);
            mCameraFinish = (Button) findViewById(R.id.take_photo_finish);
            mCameraCancel.setOnClickListener(this);
            mCameraBtn.setOnClickListener(this);
            mCameraRetry.setOnClickListener(this);
            mCameraFinish.setOnClickListener(this);
            needReLayout = false;
        }
        mPhotoView.cameraInit();
    }


    @Override
    public void showPermissionRationale(String permission, int code) {
        if (isFinishing())
            return;
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
        mLayerHelper.getAlertDialog().show();
    }

    @Override
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //用户不同意，向用户展示该权限作用
        if (requestCode == PermissionCode.PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            hasPermission();
            return;
        }
        showPermissionRationale(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.take_photo_cancel) {
//取消
            finish();
        } else if (id == R.id.take_photo) {
//拍照
            mPhotoView.takePhoto();
            mCameraC.setVisibility(View.GONE);
            mCameraResultC.setVisibility(View.VISIBLE);
        } else if (id == R.id.take_photo_retry) {
//重拍
            mPhotoView.cameraPreview();
            mCameraC.setVisibility(View.VISIBLE);
            mCameraResultC.setVisibility(View.GONE);
        } else if (id == R.id.take_photo_finish) {
            //显示图片保存对话框
            mLayerHelper.showProgressDialog(R.string.pic_save);
            savePhoto();
        }
    }

    /**
     * 完成拍照
     */
    private void savePhoto() {
        mPhotoView.savePhoto(new CameraSurfaceView.CameraWriteCallback() {
            @Override
            public void writeSuccess(final String filePath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //隐藏图片保存对话框
                        mLayerHelper.hideProgressDialog();
                        //存储到系统相册
                        insertPhotoIntoSystemDB(filePath);
                        //文件地址通过广播来传递
                        sendBroadcast(filePath);
                        //释放camera
                        mPhotoView.cameraDestory();
                        finish();
                        overridePendingTransition(R.anim.fk_slide_in_from_left, R.anim.fk_slide_out_to_left);
                    }
                });
            }

            @Override
            public void writeFail() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //隐藏图片保存对话框
                        mLayerHelper.hideProgressDialog();

                        mPhotoView.cameraPreview();
                        //按钮恢复到初始状态
                        mCameraC.setVisibility(View.VISIBLE);
                        mCameraResultC.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * 图片信息通过ContentResolver插入到系统相册
     *
     * @param filePath
     */
    private void insertPhotoIntoSystemDB(String filePath) {
        File file = new File(filePath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, file.getName());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.SIZE, file.length());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 发送广播
     *
     * @param filePath
     */
    private void sendBroadcast(String filePath) {
        Intent intent = new Intent();
        intent.setAction((String) BuildConfigUtil.getBuildConfigValue(getBaseContext(), "APPLICATION_ID") + ".camera_action");
        intent.putExtra(OPEN_CAMERA_ACTION, camera_action);
        intent.putExtra(BROADCAST_CAMERA_PATH, filePath);
        sendBroadcast(intent);
    }

    @Override
    public void clickCancel(int tag) {
        finish();
    }

    @Override
    public void clickOk(String input, int tag) {
        if (showRationale) {
            reqPermission(Manifest.permission.CAMERA, PermissionCode.PERMISSION_CAMERA);
        } else {
            jumpToPermissionSetting(this, (String) BuildConfigUtil.getBuildConfigValue(getBaseContext(), "APPLICATION_ID"));
            finish();
        }
    }

    @Override
    public void singleOk(String input, int tag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
