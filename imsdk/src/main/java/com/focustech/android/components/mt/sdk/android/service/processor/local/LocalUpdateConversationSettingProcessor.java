package com.focustech.android.components.mt.sdk.android.service.processor.local;

import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationSetting;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

/**
 * 更新会话设置
 *
 * @author zhangxu
 */
public class LocalUpdateConversationSettingProcessor extends AbstractMessageProcessor<ConversationSetting, Void, Void> {
    @Override
    public Void request(final ConversationSetting data) {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getConversationService().updateSetting(userId, data);
            }
        });

        return null;
    }
}
