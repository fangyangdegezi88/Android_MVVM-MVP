package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 更新用户状态
 *
 * @author zhangxu
 */
public class ReqUpdateUserStatusProcessor extends AbstractProcessor<Messages.Status, Void, Void> {
    @Override
    public Void request(Messages.Status data) {
        byte[] send = Messages.UpdateUserStatusReq.newBuilder().setIsNotice(Messages.Enable.ENABLE).setStatus(data).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_UPDATE_USER_STATUS, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_USER_STATUS);

        return null;
    }
}