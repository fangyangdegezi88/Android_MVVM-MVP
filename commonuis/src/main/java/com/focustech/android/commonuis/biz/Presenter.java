package com.focustech.android.commonuis.biz;

/**
 * <基础业务类>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface Presenter<V> {
    /**
     * activity/fragment通过view实现跟presenter的绑定
     *
     * @param view
     */
    void attachView(V view);

    /**
     * activity/fragment通过view实现跟presenter的解绑
     */
    void detachView();

    /**
     * 获取当前请求的activityName传入网络层
     *
     * @return
     */
    String getName();
}
