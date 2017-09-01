package com.focustech.android.commonuis.view.overscroll.adapters;

import android.view.View;

/**
 * Created by liuzaibing on 2016/1/12.
 */
public class ViewLeftOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {
    private View view;
    public ViewLeftOverScrollDecorAdapter(View view) {
        this.view=view;
    }

    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return false;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return true;
    }
}
