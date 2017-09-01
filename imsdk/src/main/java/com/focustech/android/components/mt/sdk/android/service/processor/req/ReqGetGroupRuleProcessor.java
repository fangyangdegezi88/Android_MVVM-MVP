package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群规则
 *
 * @author zhangxu
 */
public class ReqGetGroupRuleProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String groupId) {
        byte[] send = Messages.GetGroupRuleReq.newBuilder()
                .setGroupId(groupId)
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GET_GROUP_RULE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_GROUP_RULE, groupId);
        return null;
    }
}
