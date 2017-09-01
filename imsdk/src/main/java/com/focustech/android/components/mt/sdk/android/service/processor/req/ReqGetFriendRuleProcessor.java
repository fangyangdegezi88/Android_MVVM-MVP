package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 获取用户的好友验证规则
 *
 * @author zhangxu
 */
public class ReqGetFriendRuleProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String data) {
        byte[] send = Messages.GetFriendRuleReq.newBuilder().setUserId(data).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GET_FRIEND_RULE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_FRIEND_RULE, data);

        getMessageService().sendMessage(CMD.REQ_GET_FRIEND_RULE, send);

        return null;
    }
}
