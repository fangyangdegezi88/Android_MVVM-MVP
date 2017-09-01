package com.focustech.android.components.mt.sdk.android.service.processor.msg;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncMessageContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 个人聊天消息处理器
 *
 * @author zhangxu
 */
public class FriendMessageProcessor extends AbstractChatMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.Message msg = Messages.Message.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), msg);
        }

        processFriendMessage(msg, true);

        // 更新最后时间
        updateLastChatTimestampToDB(new LastTimestamp(null, getSessionManager().getUserId()
                , LastTimestampType.MESSAGE_FRIEND, msg.getTimestamp(), msg.getUserId()));
    }

    protected void doSend(MessageData data) {
        Messages.Message.Builder builder = Messages.Message.newBuilder();
        ReflectionUtil.copyProperties(data, builder);
        builder.setUserId(data.getContactId());
        builder.setMsgMeta(JSONObject.toJSONString(data.getMsgMeta()));

        int cliSeqId = getMessageService().sendMessage(CMD.MESSAGE, builder.build().toByteArray());
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), new AsyncMessageContext(data, Operation.SEND_MESSAGE));

        createConversation(Messages.RecentContactType.PERSON, data, true);
    }
}
