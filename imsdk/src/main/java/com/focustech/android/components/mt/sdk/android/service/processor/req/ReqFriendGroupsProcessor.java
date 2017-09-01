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
public class ReqFriendGroupsProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        byte[] send = Messages.FriendGroupsReq.newBuilder().setTimestamp(0).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_FRIEND_GROUPS, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_FRIEND_GROUPS);
        return null;
    }
}
