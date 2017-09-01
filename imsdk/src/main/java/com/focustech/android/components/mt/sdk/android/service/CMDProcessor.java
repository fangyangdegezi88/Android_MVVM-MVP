package com.focustech.android.components.mt.sdk.android.service;

import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.tm.open.sdk.messages.TMMessage;

/**
 * CMD处理器
 *
 * @author zhangxu
 */
public interface CMDProcessor<PARAM, RETURN, INNER> {
    long DEFAULT_TIMEOUT = 30 * 1000;

    /**
     * 服务器来消息
     *
     * @param message
     * @throws Throwable
     */
    void onMessage(TMMessage message) throws Throwable;

    /**
     * 请求
     *
     * @throws Throwable
     */
    RETURN request(PARAM data);

    /**
     * 收到回执
     *
     * @param message
     * @param context
     * @return 是否执行完成操作
     */
    boolean onReceipt(TMMessage message, AbstractAsyncContext context);

    /**
     * 处理器内循环
     *
     * @param message
     */
    void internalCycle(INNER message) throws Throwable;
}
