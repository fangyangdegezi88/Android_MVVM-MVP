package com.focustech.android.commonuis.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonuis.R;

/**
 * <提示公共类>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public final class ToastUtil {

    /**
     * 显示带对勾的toast
     *
     * @param context
     * @param msg     显示内容
     */
    public static void showOkToast(Context context, String msg) {
        if (ActivityManager.currentActivityName.equals(context.getClass().getName())) {
            View focusToastView = LayoutInflater.from(context).inflate(R.layout.toast_ok, null);
            ((TextView) focusToastView.findViewById(R.id.toast_msg_tv)).setText(msg);
            ((ImageView) focusToastView.findViewById(R.id.toast_msg_iv)).setImageResource(R.drawable.common_icon_success_toast);
            Toast toast = new Toast(context);
            toast.setView(focusToastView);
            toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(54));
            toast.show();
        }
    }

    /**
     * 显示带对勾的toast
     *
     * @param context
     * @param id      显示内容
     */
    public static void showOkToast(Context context, int id) {
        showOkToast(context, context.getString(id));
    }

    public static void showOkToastWithoutActivity(Context context, int id) {
        showOkToastWithoutActivity(context, context.getString(id));
    }

    public static void showOkToastWithoutActivity(Context context, String msg) {
        View focusToastView = LayoutInflater.from(context).inflate(R.layout.toast_ok, null);
        ((TextView) focusToastView.findViewById(R.id.toast_msg_tv)).setText(msg);
        ((ImageView) focusToastView.findViewById(R.id.toast_msg_iv)).setImageResource(R.drawable.common_icon_success_toast);
        Toast toast = new Toast(context);
        toast.setView(focusToastView);
        toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(54));
        toast.show();
    }


    /**
     * 显示带感叹号的toast
     *
     * @param context
     * @param msg     显示内容
     */
    public static void showFocusToast(Context context, String msg) {
        if (ActivityManager.currentActivityName.equals(context.getClass().getName())) {
            View focusToastView = LayoutInflater.from(context).inflate(R.layout.toast_ok, null);
            ((TextView) focusToastView.findViewById(R.id.toast_msg_tv)).setText(msg);
            ((ImageView) focusToastView.findViewById(R.id.toast_msg_iv)).setImageResource(R.drawable.common_toast_icon_error);
            Toast toast = new Toast(context);
            toast.setView(focusToastView);
            toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(54));
            toast.show();
        }
    }

    /**
     * 显示带感叹号的toast  不处理activity是否被销毁
     *
     * @param context
     * @param msg     显示内容
     */
    public static void showFocusToastWithoutActivity(Context context, String msg) {
        View focusToastView = LayoutInflater.from(context).inflate(R.layout.toast_ok, null);
        ((TextView) focusToastView.findViewById(R.id.toast_msg_tv)).setText(msg);
        ((ImageView) focusToastView.findViewById(R.id.toast_msg_iv)).setImageResource(R.drawable.common_toast_icon_error);
        Toast toast = new Toast(context);
        toast.setView(focusToastView);
        toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(54));
        toast.show();
    }

    /**
     * 显示带感叹号的toast  不处理activity是否被销毁
     *
     * @param context
     * @param id      显示内容
     */
    public static void showFocusToastWithoutActivity(Context context, @StringRes int id) {
        showFocusToastWithoutActivity(context, context.getString(id));
    }

    /**
     * 显示带感叹号的toast
     *
     * @param context
     * @param id      显示内容
     */
    public static void showFocusToast(Context context, @StringRes int id) {
        showFocusToast(context, context.getString(id));
    }


    private static Toast toast;
    /**
     * <显示toast提示>
     *
     * @param context
     * @param id
     * @see [类、类#方法、类#成员]
     */
    public static void makeText(Context context, int id) {
        if (ActivityManager.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT);
            } else {
                toast.setText(context.getString(id));
            }
            toast.show();
        }
    }

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextLong(Context context, String msg) {
        if (ActivityManager.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    public static void destory() {
        toast = null;
    }

    public static void showCleanCacheSuccessToast(Context context) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.view_toast_clean_cache, null);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
    }
}
