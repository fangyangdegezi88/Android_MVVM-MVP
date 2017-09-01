package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.JoinGroupAnswer;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 用户申请加入群，管理员应答
 *
 * @author zhangxu
 */
public class ReqJoinGroupAnswerProcessor extends AbstractGroupProcessor<JoinGroupAnswer, Void, Void> {
    @Override
    public Void request(JoinGroupAnswer data) {
        byte[] send = Messages.JoinGroupRsp.newBuilder()
                .setGroupId(data.getGroupId())
                .setUserId(data.getUserId())
                .setTimestamp(NTPTime.now())
                .setRefuseMessage(data.getRefuseMessage())
                .setResult(data.getResult())
                .setSvrMsgId(data.getSvrMsgId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_JOIN_ANSWER, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.JOIN_GROUP_ANSWER, JSONObject.toJSONString(data));

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        final JoinGroupAnswer data = JSONObject.parseObject(context.getOperationData(), JoinGroupAnswer.class);
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 该群的邀请已经处理
                getSystemNotifyService().processed(userId, CMD.SYS_NTY_JOIN_GROUP_VALIDATE.getValue()
                        , Long.valueOf(Messages.RecentContactType.GROUP_VALUE), data.getGroupId(), data.getUserId());
            }
        });
        return true;
    }
}
