package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友
 *
 * @author zhangxu
 */
public class ReqFriendListProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        long timestamp = getLastTimestampService().getDataTimestamp(getSessionManager().getUserId(), LastTimestampType.DATA_FRIEND_GROUPS);

        byte[] send = Messages.FriendsReq.newBuilder().setTimestamp(timestamp).setNeedEndRsp(Messages.Enable.ENABLE).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_FRIENDS, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_FRIEND_INFO_LIST);

        return null;
    }
}
