package com.focustech.android.commonuis.view.overscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <主要解决viewPager和photoview共用问题>
 *
 * @author liuzaibing
 * @version [版本号, 2016/10/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ViewPagerFixed extends android.support.v4.view.ViewPager {

    public boolean IS_CAN_OVER_SCROLL = true;

    public ViewPagerFixed(Context context) {
        super(context);
    }

    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}