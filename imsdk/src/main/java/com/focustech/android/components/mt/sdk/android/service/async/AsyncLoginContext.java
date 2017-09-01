package com.focustech.android.components.mt.sdk.android.service.async;

import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.LoginData;

/**
 * 登陆异步上下文
 *
 * @author zhangxu
 */
public class AsyncLoginContext extends AbstractAsyncContext {
    private LoginData data;

    public AsyncLoginContext(LoginData data) {
        this.data = data;
    }

    @Override
    public Operation getOperation() {
        return Operation.LOGIN;
    }

    public LoginData getData() {
        return data;
    }

    @Override
    public String getOperationData() {
        return data.getUserName();
    }
}
