package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求更新用户签名
 *
 * @author zhangxu
 */
public class ReqUpdateUserSignatureProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String data) {
        byte[] send = Messages.UpdateUserSignatureReq.newBuilder().setUserSignature(data).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_UPDATE_USER_SIGNATURE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_USER_SIGNATURE);

        return null;
    }
}
