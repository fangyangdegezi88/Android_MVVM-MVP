package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * @author zhangxu
 */
public class ReqGetUserSettingProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        byte[] send = Messages.UserSettingReq.newBuilder().setTimestamp(0).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GET_USER_SETTING, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_USER_SETTING);

        return null;
    }
}
