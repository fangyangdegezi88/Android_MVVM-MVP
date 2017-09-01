package com.focustech.android.components.mt.sdk.android.service.processor.local;

import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

/**
 * 删除本地一个类型的扩展消息
 *
 * @author zhangxu
 */
public class LocalDeleteExtMessageListProcessor extends AbstractMessageProcessor<Integer, Void, Void> {
    @Override
    public Void request(final Integer type) {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getDBMessageService().delete(userId, type);
            }
        });

        return null;
    }
}
