package com.focustech.android.commonuis.base;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/12]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface PermissionListener {

    /**
     * 已经有权限
     */
    public void hasPermission();

    /**
     * 用户提示
     */
    public void showPermissionRationale(String permission, int code);

    /**
     * 权限申请结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
