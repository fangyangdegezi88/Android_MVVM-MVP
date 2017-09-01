package com.focustech.android.components.mt.sdk.android.service.async;

import com.focustech.android.components.mt.sdk.android.service.Operation;

/**
 * 抽象异步上下文
 *
 * @author zhangxu
 */
public abstract class AbstractAsyncContext {
    public abstract Operation getOperation();
    public abstract String getOperationData();
}
