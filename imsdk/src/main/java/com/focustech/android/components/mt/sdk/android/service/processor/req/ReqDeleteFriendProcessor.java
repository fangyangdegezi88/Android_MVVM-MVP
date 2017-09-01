package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 删除联系人
 *
 * @author zhangxu
 */
public class ReqDeleteFriendProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String friendUserId) {
        byte[] send = Messages.DeleteFriendReq.newBuilder().setFriendUserId(friendUserId).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DELETE_FRIEND, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DELETE_FRIEND, friendUserId);
        return null;
    }
}
