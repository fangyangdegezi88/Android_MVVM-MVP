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
 * 讨论组聊天消息处理器
 *
 * @author zhangxu
 */
public class DiscussionMessageProcessor extends AbstractChatMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionMessage msg = Messages.DiscussionMessage.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), msg);
        }

        processDiscussionMessage(msg, true);

        // 更新最后时间
        updateLastChatTimestampToDB(new LastTimestamp(null, getSessionManager().getUserId(), LastTimestampType.MESSAGE_DISCUSSION, msg.getTimestamp(), msg.getDiscussionId()));
    }

    protected void doSend(MessageData data) {
        Messages.DiscussionMessage.Builder builder = Messages.DiscussionMessage.newBuilder();
        ReflectionUtil.copyProperties(data, builder);
        builder.setDiscussionId(data.getContactId());
        builder.setDiscussionName(data.getContactName());
        builder.setUserId(getSessionManager().getUserId());
        builder.setFromUserName(getSessionManager().getCurrent().getCurrent().getUserName());
        builder.setMsgMeta(JSONObject.toJSONString(data.getMsgMeta()));

        int cliSeqId = getMessageService().sendMessage(CMD.DISCUSSION_MESSAGE, builder.build().toByteArray());
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), new AsyncMessageContext(data, Operation.SEND_DISCUSSION_MESSAGE));

        createConversation(Messages.RecentContactType.DISCUSSION, data, true);
    }
}
