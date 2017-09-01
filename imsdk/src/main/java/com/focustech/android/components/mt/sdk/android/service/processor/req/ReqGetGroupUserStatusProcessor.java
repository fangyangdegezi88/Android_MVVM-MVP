package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友
 *
 * @author zhangxu
 */
public class ReqGetGroupUserStatusProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String groupId) {
        byte[] send = Messages.GetGroupUserStatusReq.newBuilder()
                .setGroupId(groupId).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GET_GROUP_USER_STATUS, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_GROUP_USER_STATUS, groupId);

        return null;
    }
}
