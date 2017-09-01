package com.focustech.android.commonlibs.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.focustech.android.commonlibs.application.BaseApplication;

import java.lang.reflect.Field;


/**
 * <像素转换>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class DensityUtil {
    /**
     * <将px值转换为dip或dp值，保证尺寸大小不变>
     *
     * @param pxValue DisplayMetrics类中属性density
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * <将dip或dp值转换为px值，保证尺寸大小不变>
     *
     * @param dipValue DisplayMetrics类中属性density
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * <将dip或dp值转换为px值，保证尺寸大小不变>
     *
     * @param dipValue DisplayMetrics类中属性density
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * <将px值转换为sp值，保证文字大小不变>
     * <功能详细描述>
     *
     * @param pxValue DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * <将sp值转换为px值，保证文字大小不变>
     * <功能详细描述>
     *
     * @param spValue DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int sp2px(float spValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * <将sp值转换为px值，保证文字大小不变>
     * <功能详细描述>
     *
     * @param spValue DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getWidth() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = BaseApplication.getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * <获取屏幕像素x>
     *
     * @param activity
     * @return
     */
    public static int getXScreenpx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * <获取屏幕像素y>
     *
     * @param activity
     * @return
     */
    public static int getYScreenpx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
