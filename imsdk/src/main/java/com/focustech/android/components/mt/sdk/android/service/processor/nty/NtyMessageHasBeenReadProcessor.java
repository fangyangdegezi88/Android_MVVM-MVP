package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 消息已读通知
 *
 * @author zhangxu
 */
public class NtyMessageHasBeenReadProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.MessageHasBeenReadNty nty = Messages.MessageHasBeenReadNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        Messages.LastChatTime lastChatTime = nty.getLastChatTime();

        // 其他设备在激活状态，消息不提醒
        resetLastChatTime(lastChatTime);

        updateLastChatTimestampToDB(new LastTimestamp(null, getSessionManager().getUserId(), LastTimestampType.READ_MAPPING.get(lastChatTime.getType()), lastChatTime.getTimestamp(), lastChatTime.getTargetId()));
    }
}
