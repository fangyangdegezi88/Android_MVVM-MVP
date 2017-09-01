package com.focustech.android.commonuis.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.focustech.android.commonuis.biz.BaseCommonPresenter;


/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/12]
 * @see [相关类/方法]
 * @since [V1]
 */
@Deprecated
public abstract class BasePermissionActivity<V extends BaseCommonPresenter> extends BaseActivity<V> implements PermissionListener {

    /**
     * 判断权限集合
     *
     * @param permissions
     * @return
     */
    private boolean lacksPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission
     * @return
     */
    public boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 权限申请
     *
     * @param permissions 权限名称
     * @param requestCode code
     */
    public void reqPermissions(String[] permissions, int requestCode) {
        //缺少权限
        if (lacksPermissions(permissions))
            ActivityCompat.requestPermissions(this, permissions, requestCode);
            //已经有对应权限
        else hasPermission();
    }

    /**
     * 权限申请
     *
     * @param permission  权限名称
     * @param requestCode code
     */
    public void reqPermission(String permission, int requestCode) {
        //缺少权限
        if (lacksPermission(permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            //已经有对应权限
        } else hasPermission();
    }

    /**
     * 跳转到权限设置页面
     *
     * @param activity
     */
    public void jumpToPermissionSetting(Activity activity, String appId) {
        if (Build.MODEL.contains("HUAWEI")) {
            if (goToHWPermissionSetting(activity))
                return;
        }
        Uri packageURI = Uri.parse("package:" + appId);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivity(intent);
    }

    /**
     * 跳转到设置页面
     *
     * @param activity
     * @return
     */
    private boolean goToHWPermissionSetting(Activity activity) {
        try {
            Toast.makeText(activity, "请到设置->隐私和安全->权限管理中打开权限", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showPermissionRationale(String permission, int code) {

    }
}
