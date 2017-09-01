package com.focustech.android.commonuis.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.focustech.android.commonuis.view.dialog.SFProgressDialog;
import com.focustech.android.commonuis.view.turningview.TurningView;


/**
 * <页面基础公共功能实现>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PresentationLayerFuncHelper implements PresentationLayerFunc {

    private Context context;
    /**
     * 加载中
     */
    protected SFProgressDialog mProgressDialog;

    public SFAlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    /**
     * 提示对话框
     */
    protected SFAlertDialog mAlertDialog;

    protected TurningView mTurnView;

    public PresentationLayerFuncHelper(Context context) {
        this.context = context;
    }

    /*@Override
    public void showToast(String msg) {
        ToastUtil.makeText(context, msg);
    }*/

    @Override
    public void showToastOk(String msg) {
        ToastUtil.showOkToast(context, msg);
    }

    @Override
    public void showToastOk(int id) {
        ToastUtil.showOkToast(context, id);
    }

    @Override
    public void showToastFail(String msg) {
        ToastUtil.showFocusToast(context, msg);
    }

    @Override
    public void showToastFail(int id) {
        ToastUtil.showFocusToast(context, id);
    }

    public void showProgressDialog(String msg) {
        if (null == mProgressDialog) {
            mProgressDialog = new SFProgressDialog(context, msg);
        } else {
            mProgressDialog.setLoadingMsg(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public void showProgressDialog(int id) {
        showProgressDialog(context.getString(id));
    }

    @Override
    public void hideProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        focusView.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * OnClick事件下有效
     *
     * @param focusView
     */
    @Override
    public void hideSoftKeyboard(@NonNull View focusView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != inputMethodManager) {

            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            focusView.clearFocus();
        }
    }

    @Override
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    public void showAlert(String title, String message) {
        if (null == mAlertDialog) {
            mAlertDialog = new SFAlertDialog(context, title, message);
        } else {
            mAlertDialog.setTitle(title);
            mAlertDialog.setDialogTheme(SFAlertDialog.MTDIALOG_THEME.HAS_TITLE_TWO);
            mAlertDialog.setContent(message);

        }
        if (!mAlertDialog.isShowing())
            mAlertDialog.show();
    }

    @Override
    public void showAlert(String content, SFAlertDialog.MTDIALOG_THEME theme) {
        if (null == mAlertDialog) {
            mAlertDialog = new SFAlertDialog(context, content, theme);
        } else {
            mAlertDialog.setDialogTheme(theme);
            mAlertDialog.setContent(content);
        }
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    @Override
    public void showCleanCachePercentAlert(String content, String singleBtnText, int percent) {
        if (null == mAlertDialog) {
            mAlertDialog = new SFAlertDialog(context, content, SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE_PROGRESS);
        } else {
            mAlertDialog.setDialogTheme(SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE_PROGRESS);
            mAlertDialog.setContent(content);

        }
        mAlertDialog.setProgress(percent);
        mAlertDialog.setPercentTv(percent + "%");
        mAlertDialog.setSingleBtnText(singleBtnText);
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }


    @Override
    public void hideAlert() {
        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        if (null != mAlertDialog && mAlertDialog.isShowing()) return true;
        if (null != mProgressDialog && mProgressDialog.isShowing()) return true;
        return false;
    }

    @Override
    public void showTurningView(int id) {
        if (mTurnView == null) {
            mTurnView = new TurningView(context);
        }
        mTurnView.setText(id);
    }

    @Override
    public void showTurningView(String text) {
        if (mTurnView == null) {
            mTurnView = new TurningView(context);
        }
        mTurnView.setText(text);
    }

    @Override
    public void hideTurningView() {
        if (mTurnView != null) {
            if ((mTurnView.getParent()) != null) {
                ((ViewGroup) mTurnView.getParent()).removeView(mTurnView);
            }
            mTurnView = null;
        }
    }

    /**
     * 对话框是否可取消
     *
     * @param cancelable
     */
    public void setAlertDialogCancelable(boolean cancelable) {
        if (null != mAlertDialog) {
            mAlertDialog.setCancelable(cancelable);
            mAlertDialog.setCanceledOnTouchOutside(cancelable);
        }
    }

    /**
     * 加载框是否可取消
     *
     * @param cancelable
     */
    public void setProgressDialogCancelable(boolean cancelable) {
        if (null != mProgressDialog) {
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(cancelable);
        }
    }
}
