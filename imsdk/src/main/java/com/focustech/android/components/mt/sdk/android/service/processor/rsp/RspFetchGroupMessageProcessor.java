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
public class RspFetchGroupMessageProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.FetchGroupMessageRsp rsp = Messages.FetchGroupMessageRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        for (Messages.GroupMessage msg : rsp.getGroupMessageList()) {
            processGroupMessage(msg, false);
        }

        if (rsp.getGroupMessageCount() > 0) {
            if (rsp.hasHasMore() && rsp.getHasMore()) {
                // 获取最早的消息时间戳
                long toTimestamp = rsp.getGroupMessage((rsp.getGroupMessageCount() - 1)).getTimestamp();
                resetToTimestamp(LastTimestampType.MESSAGE_GROUP, toTimestamp);

                // 继续拉取
                continueFetch(LastTimestampType.MESSAGE_GROUP, Messages.RecentContactType.GROUP);
            } else {
                // 更新消息最后时间
                getLastTimestampService().addOrUpdate(new LastTimestamp(null, getSessionManager().getUserId(), LastTimestampType.MESSAGE_GROUP, rsp.getGroupMessage(0).getTimestamp(), rsp.getGroupId()));

                // 完成fetch
                completeFetch(LastTimestampType.MESSAGE_GROUP, rsp.getGroupId());
            }
        } else {
            // 完成fetch
            completeFetch(LastTimestampType.MESSAGE_GROUP, rsp.getGroupId());
        }
    }
}
