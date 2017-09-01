package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupAnswer;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 被邀请用户应答
 *
 * @author zhangxu
 */
public class ReqInviteUserJoinGroupAnswerProcessor extends AbstractGroupProcessor<InviteUserJoinGroupAnswer, Void, Void> {
    @Override
    public Void request(InviteUserJoinGroupAnswer data) {
        byte[] send = Messages.InviteUserJoinGroupRsp.newBuilder()
                .setGroupId(data.getGroupId())
                .setTimestamp(NTPTime.now())
                .setInviteUserId(data.getInviteUserId())
                .setInviteUserName(data.getInviteUserName())
                .setResult(data.getResult())
                .setSvrMsgId(data.getSvrMsgId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_INVITE_USER_JOIN_ANSWER, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.INVITE_USER_JOIN_GROUP_ANSWER, JSONObject.toJSONString(data));

        final String userId = getSessionManager().getUserId();
        final String groupId = data.getGroupId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 该群的邀请已经处理
                getSystemNotifyService().processed(userId, CMD.SYS_NTY_INVITE_USER_JOIN_GROUP.getValue(), Long.valueOf(Messages.RecentContactType.GROUP_VALUE), groupId);
            }
        });

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
