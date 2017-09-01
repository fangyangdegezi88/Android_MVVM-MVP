package com.focustech.android.components.mt.sdk.android.service.processor.rsp;


import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉取消息最后阅读时间响应
 *
 * @author zhangxu
 */
public class RspGetLastChatTimeProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GetLastChatTimeRsp rsp = Messages.GetLastChatTimeRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        String userId = getSessionManager().getUserId();
        List<LastTimestamp> data = new ArrayList<>();

        // 更新最后阅读时间
        for (Messages.LastChatTime chatTime : rsp.getLastChatTimeList()) {
            data.add(new LastTimestamp(null, userId, LastTimestampType.READ_MAPPING.get(chatTime.getType()), chatTime.getTimestamp(), chatTime.getTargetId()));
            resetLastChatTime(chatTime);
        }

        // 最后时间
        data.add(new LastTimestamp(null, userId, LastTimestampType.DATA_LAST_CHAT_TIME, NTPTime.now(), null));

        updateLastChatTimestampToDB(data);
    }
}
