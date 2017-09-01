package com.focustech.android.commonuis.view.overscroll.adapters;

import android.view.View;

import com.focustech.android.commonuis.view.overscroll.ViewPagerFixed;


/**
 * Created by liuzaibing on 2016/1/11.
 */
public class ViewPagerOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    private ViewPagerFixed mViewPager;

    public ViewPagerOverScrollDecorAdapter(ViewPagerFixed viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public View getView() {
        return mViewPager;
    }

    @Override
    public boolean isInAbsoluteStart() {
        if (!mViewPager.IS_CAN_OVER_SCROLL)
            return false;
        if (mViewPager.getCurrentItem() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        if (!mViewPager.IS_CAN_OVER_SCROLL)
            return false;
        if (mViewPager.getAdapter().getCount() - 1 == mViewPager.getCurrentItem())
            return true;
        return false;
    }
}
