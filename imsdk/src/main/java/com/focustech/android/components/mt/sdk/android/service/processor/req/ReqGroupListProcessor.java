package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取群组
 *
 * @author zhangxu
 */
public class ReqGroupListProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        long timestamp = getLastTimestampService().getDataTimestamp(getSessionManager().getUserId(), LastTimestampType.DATA_GROUP_LIST);

        byte[] send = Messages.GroupsReq.newBuilder().setTimestamp(timestamp).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_LIST, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_GROUP_LIST);
        return null;
    }
}
