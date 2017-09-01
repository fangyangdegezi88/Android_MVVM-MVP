package com.focustech.android.components.mt.sdk.android.service.async;

import com.focustech.android.components.mt.sdk.android.service.Operation;

/**
 * <页面基础公共功能实现>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/13]
 * @see [相关类/方法]
 * @since [V1]
 */
public class AsyncLoginControlContext extends AbstractAsyncContext{

    @Override
    public Operation getOperation() {
        return Operation.LOGIN_CONTROL;
    }

    @Override
    public String getOperationData() {
        return null;
    }
}
