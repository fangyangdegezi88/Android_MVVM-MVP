package com.focustech.android.commonuis.view.overscroll.adapters;

import android.view.View;

/**
 * Created by liuzaibing on 2016/1/12.
 */
public class ViewRightOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {
    private View view;

    public ViewRightOverScrollDecorAdapter(View view) {
        this.view=view;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return true;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return false;
    }
}
