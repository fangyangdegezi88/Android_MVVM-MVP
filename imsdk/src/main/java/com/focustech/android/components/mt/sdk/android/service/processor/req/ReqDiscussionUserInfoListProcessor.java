package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友，针对讨论组
 *
 * @author zhangxu
 */
public class ReqDiscussionUserInfoListProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String groupId) {
        String userId = getSessionManager().getUserId();
        long timestamp = getLastTimestampService().getDataTimestamp(userId, groupId, LastTimestampType.DATA_DISCUSSION_USER_INFO);

        byte[] send = Messages.DiscussionUserInfosReq.newBuilder()
                .setDiscussionId(groupId).setTimestamp(timestamp).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_USERS_INFO_LIST, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_DISCUSSION_USERS_INFO_LIST, groupId);

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
