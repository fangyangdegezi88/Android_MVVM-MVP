package com.focustech.android.components.mt.sdk.android.service.processor.rsp;


import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 拉取消息响应
 *
 * @author zhangxu
 */
public class RspFetchMessageProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.FetchMessageRsp rsp = Messages.FetchMessageRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        for (Messages.Message msg : rsp.getMessageList()) {
            processFriendMessage(msg, false);
        }

        if (rsp.getMessageCount() > 0) {
            if (rsp.hasHasMore() && rsp.getHasMore()) {
                // 获取最早的消息时间戳
                long toTimestamp = rsp.getMessage(rsp.getMessageCount() - 1).getTimestamp();
                resetToTimestamp(LastTimestampType.MESSAGE_FRIEND, toTimestamp);

                // 继续拉取
                continueFetch(LastTimestampType.MESSAGE_FRIEND, Messages.RecentContactType.PERSON);
            } else {
                // 更新消息最后时间
                getLastTimestampService().addOrUpdate(new LastTimestamp(null, getSessionManager().getUserId(), LastTimestampType.MESSAGE_FRIEND, rsp.getMessage(0).getTimestamp(), null));

                // 完成fetch
                completeFetch(LastTimestampType.MESSAGE_FRIEND);
            }
        } else {
            // 完成fetch
            completeFetch(LastTimestampType.MESSAGE_FRIEND);
        }
    }
}
