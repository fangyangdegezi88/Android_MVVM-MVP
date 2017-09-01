package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友，针对群
 *
 * @author zhangxu
 */
public class ReqGroupUserInfoListProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String groupId) {
        String userId = getSessionManager().getUserId();
        long timestamp = getLastTimestampService().getDataTimestamp(userId, groupId, LastTimestampType.DATA_GROUP_USER_INFO);

        byte[] send = Messages.GroupUserInfoReq.newBuilder()
                .setGroupId(groupId).setTimestamp(timestamp).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_USER_INFO_LIST, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GROUP_USER_INFO_LIST, groupId);

        return null;
    }
}
