package com.focustech.android.components.mt.sdk.android.service.processor.msg;


import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 自己的群同步消息
 *
 * @author zhangxu
 */
public class GroupMessageSyncProcessor extends AbstractMessageProcessor<MessageData, Void, Messages.MessageSync> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GroupMessageSync sync = Messages.GroupMessageSync.parseFrom(message.getBody());
        Messages.GroupMessage msg = sync.getSource();

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), msg);
        }

        processGroupMessage(msg, true);
    }
}
