package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 拉取消息最后阅读时间
 *
 * @author zhangxu
 */
public class ReqGetLastChatTimeProcessor extends AbstractMessageProcessor<Void, Void, Void> {
    private static int reqId = 0;

    @Override
    public Void request(Void data) {
        long lastTimestamp = getLastTimestampService().getDataTimestamp(getSessionManager().getUserId(), LastTimestampType.DATA_LAST_CHAT_TIME);

        byte[] send = Messages.GetLastChatTimeReq.newBuilder()
                .setLastTimestamp(lastTimestamp)
                .setReqId(reqId++)
                .build().toByteArray();
        getMessageService().sendMessage(CMD.REQ_GET_LAST_CHAT_TIME, send);

        return null;
    }
}
