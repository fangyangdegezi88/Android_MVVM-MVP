package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 请求拉取好友
 *
 * @author zhangxu
 */
public class ReqGetUsersInfoProcessor extends AbstractProcessor<List<String>, Void, Void> {
    @Override
    public Void request(List<String> data) {
        byte[] send = Messages.UsersInfoReq.newBuilder().addAllTargetUserId(data).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_USERS_INFO, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_USERS_INFO);

        return null;
    }
}
