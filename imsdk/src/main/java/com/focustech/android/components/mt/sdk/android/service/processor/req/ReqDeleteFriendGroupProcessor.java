package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友分组
 *
 * @author zhangxu
 */
public class ReqDeleteFriendGroupProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String friendGroupId) {
        byte[] send = Messages.DeleteFriendGroupReq.newBuilder().setFriendGroupId(friendGroupId).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DELETE_FRIEND_GROUP, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DELETE_FRIEND_GROUP);
        return null;
    }
}
