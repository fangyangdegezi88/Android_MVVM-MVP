package com.focustech.android.commonuis.view.recycler.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <基于mvvm实现的viewHolder>
 *
 * @author yanguozhu
 * @version [版本号, 2017-06-29]
 * @see [相关类/方法]
 * @since [V1]
 */

public class BaseMvvmViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected T binding;

    public BaseMvvmViewHolder(View itemView) {
        super(itemView);
    }

    public T getBinding() {
        return binding;
    }

    public void setBinding(T bind) {
        this.binding = bind;
    }

    public void onClick(View view) {

    }

}
