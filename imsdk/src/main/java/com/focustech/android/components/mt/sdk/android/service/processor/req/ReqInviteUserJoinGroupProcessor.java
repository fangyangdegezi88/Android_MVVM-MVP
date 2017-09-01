package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteJoinToGroupData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 添加群成员
 *
 * @author zhangxu
 */
public class ReqInviteUserJoinGroupProcessor extends AbstractProcessor<InviteJoinToGroupData, Void, Void> {
    @Override
    public Void request(InviteJoinToGroupData data) {
        byte[] send = Messages.InviteUserJoinGroupReq.newBuilder()
                .setGroupId(data.getGroupId())
                .addAllInvitedUserIds(data.getUserIds())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_INVITE_JOIN_TO, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.INVITE_JOIN_TO_GROUP, JSONObject.toJSONString(data));
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
