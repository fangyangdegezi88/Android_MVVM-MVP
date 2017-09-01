package com.focustech.android.commonuis.base;

import android.os.Bundle;
import android.view.View;

/**
 * <基于MVVM的页面初始化>
 *
 * @author yanguozhu
 * @version [版本号, 2017-06-21]
 * @see [相关类/方法]
 * @since [V1]
 */

public interface CreateMvvmInit {

    /**
     * 设置DataBinding的绑定数据
     */
    void initData(Bundle bundle);

    /**
     * 点击事件
     *
     * @param view
     */
    void onClick(View view);

    /**
     * 初始化页面DataBinding
     */
    int getLayoutId();

    /**
     * 返回当前页面的名称 用于统计分析
     */
    String getName();
}
