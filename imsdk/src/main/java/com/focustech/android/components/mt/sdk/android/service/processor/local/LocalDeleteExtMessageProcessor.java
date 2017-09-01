package com.focustech.android.components.mt.sdk.android.service.processor.local;

import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

/**
 * 删除本地扩展消息
 *
 * @author zhangxu
 */
public class LocalDeleteExtMessageProcessor extends AbstractMessageProcessor<String, Void, Void> {
    @Override
    public Void request(final String svrMsgId) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getDBMessageService().delete(svrMsgId);
            }
        });

        return null;
    }
}
