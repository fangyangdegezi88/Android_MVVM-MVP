package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群规则
 *
 * @author zhangxu
 */
public class ReqSetGroupRuleProcessor extends AbstractProcessor<GroupRuleData, Void, Void> {
    @Override
    public Void request(GroupRuleData data) {
        byte[] send = Messages.SetGroupRuleReq.newBuilder()
                .setGroupId(data.getGroupId())
                .setGroupRule(data.getRule())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_SET_GROUP_RULE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.SET_GROUP_RULE, data.getGroupId());
        return null;
    }
}
