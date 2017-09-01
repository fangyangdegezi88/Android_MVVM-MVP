package com.focustech.android.components.mt.sdk.android.service.processor.msg;


import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 自己的讨论组同步消息
 *
 * @author zhangxu
 */
public class DiscussionMessageSyncProcessor extends AbstractMessageProcessor<MessageData, Void, Messages.MessageSync> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionMessageSync sync = Messages.DiscussionMessageSync.parseFrom(message.getBody());
        Messages.DiscussionMessage msg = sync.getSource();

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), msg);
        }

        processDiscussionMessage(msg, true);
    }
}
