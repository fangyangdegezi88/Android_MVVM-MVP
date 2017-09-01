package com.focustech.android.commonlibs.capability.request.http;

/**
 * <功能详细描述>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/8]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ITRequestResult<T> {

    /**
     * 网络请求成功回调
     *
     * @param entity
     */
    public void onSuccessful(T entity);

    /**
     * 网络请求失败回调
     *
     * @param errorMsg  错误信息
     * @param errorCode 错误码
     * @param t         具体的业务对象
     */
    public void onFailure(String errorMsg, int errorCode, T t);

    /**
     * 网络请求完成回调
     */
    public void onCompleted();
}
