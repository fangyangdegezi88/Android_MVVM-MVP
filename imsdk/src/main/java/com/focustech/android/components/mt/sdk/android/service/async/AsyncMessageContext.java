package com.focustech.android.components.mt.sdk.android.service.async;

import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;

/**
 * 消息异步上下文
 *
 * @author zhangxu
 */
public class AsyncMessageContext extends AbstractAsyncContext {
    private MessageData data;
    private Operation operation;

    public AsyncMessageContext(MessageData data, Operation operation) {
        this.data = data;
        this.operation = operation;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    public MessageData getData() {
        return data;
    }

    @Override
    public String getOperationData() {
        return data.getContactId();
    }
}
