package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群规则
 *
 * @author zhangxu
 */
public class ReqGetGroupUserRuleProcessor extends AbstractProcessor<GroupUserRuleData, Void, Void> {
    @Override
    public Void request(GroupUserRuleData data) {
        byte[] send = Messages.GetGroupUserRuleReq.newBuilder()
                .setGroupId(data.getGroupId())
                .setUserId(data.getUserId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GET_GROUP_USER_RULE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_GROUP_USER_RULE, JSONObject.toJSONString(data));
        return null;
    }
}
