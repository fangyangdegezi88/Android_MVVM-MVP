package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationStatusData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 发送窗体激活报文通知已读未读
 *
 * @author zhangxu
 */
public class ReqMessageHasBeenReadProcessor extends AbstractMessageProcessor<ConversationStatusData, Void, Void> {
    private static int reqId = 0;

    @Override
    public Void request(ConversationStatusData data) {
        byte[] send = Messages.MessageHasBeenReadReq.newBuilder()
                .setReqId(reqId++)
                .setIsOpen(data.isActive())
                .setTargetId(data.getContactId())
                .setType(data.getType())
                .build().toByteArray();
        getMessageService().sendMessage(CMD.REQ_MESSAGE_HAS_BEEN_READ, send);

        // 处于激活状态，表示已读时间为当前时间
        if (data.isActive()) {
            resetLastChatTime(LastTimestampType.READ_MAPPING.get(data.getType()), data.getContactId(), NTPTime.now());
        }

        return null;
    }
}
