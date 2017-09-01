package com.focustech.android.commonuis.base;

import android.view.View;

import com.focustech.android.commonuis.view.dialog.SFAlertDialog;


/**
 * <页面基础公共功能抽象>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface PresentationLayerFunc {
    /**
     * 弹出消息
     *
     * @param msg
     */
//    public void showToast(String msg);

    /**
     * 弹出成功Toast
     * @param msg
     */
    public void showToastOk(String msg);

    /**
     * 弹出成功Toast
     * @param id
     */
    public void showToastOk(int id);

    /**
     * 弹出失败Toast
     * @param msg
     */
    public void showToastFail(String msg);

    /**
     * 弹出失败Toast
     * @param id
     */
    public void showToastFail(int id);

    /**
     * 网络请求加载框
     */
    public void showProgressDialog(String msg);

    /**
     * 网络请求加载框
     */
    public void showProgressDialog(int id);

    /**
     * 隐藏网络请求加载框
     */
    public void hideProgressDialog();

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    public void showSoftKeyboard(View focusView);
    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(View focusView);
    
    public void hideSoftKeyboard();

    /**
     * @param title   标题
     * @param message 内容
     */
    public void showAlert(String title, String message);

    /**
     * @param content 内容
     * @param theme   主题
     */
    public void showAlert(String content, SFAlertDialog.MTDIALOG_THEME theme);

    /**
     * 单独给清除缓存的时候显示百分比使用
     * */
    public void showCleanCachePercentAlert(String content, String singleBtnText, int percent);

    /**
     * 隐藏交互对话框
     */
    public void hideAlert();

    /**
     * @return 是否有正在显示的对话框，包含加载动画和alert
     */
    public boolean isShowing();

    /**
     * 展示 提交中  的菊花view
     * */
    void showTurningView(int id);

    /**
     * 展示 提交中  的菊花view
     * */
    void showTurningView(String text);

    /**
     * 取消 提交中  的菊花view
     * */
    void hideTurningView();

}
