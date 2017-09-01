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
 * 群聊天消息处理器
 *
 * @author zhangxu
 */
public class GroupMessageProcessor extends AbstractChatMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GroupMessage msg = Messages.GroupMessage.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), msg);
        }

        processGroupMessage(msg, true);

        // 更新最后时间
        updateLastChatTimestampToDB(new LastTimestamp(null, getSessionManager().getUserId(), LastTimestampType.MESSAGE_GROUP, msg.getTimestamp(), msg.getGroupId()));
    }

    protected void doSend(MessageData data) {
        Messages.GroupMessage.Builder builder = Messages.GroupMessage.newBuilder();
        ReflectionUtil.copyProperties(data, builder);
        builder.setGroupId(data.getContactId());
        builder.setGroupName(data.getContactName());
        builder.setUserId(getSessionManager().getUserId());
        builder.setFromUserName(getSessionManager().getCurrent().getCurrent().getUserName());
        builder.setMsgMeta(JSONObject.toJSONString(data.getMsgMeta()));

        int cliSeqId = getMessageService().sendMessage(CMD.GROUP_MESSAGE, builder.build().toByteArray());
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), new AsyncMessageContext(data, Operation.SEND_GROUP_MESSAGE));

        createConversation(Messages.RecentContactType.GROUP, data, true);
    }
}
