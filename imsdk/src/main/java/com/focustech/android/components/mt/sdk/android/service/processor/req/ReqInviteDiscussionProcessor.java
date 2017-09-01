package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteDiscussionData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 邀请讨论组
 *
 * @author zhangxu
 */
public class ReqInviteDiscussionProcessor extends AbstractProcessor<InviteDiscussionData, Void, Void> {
    @Override
    public Void request(InviteDiscussionData data) {
        Messages.InviteDiscussionReq.Builder builder = Messages.InviteDiscussionReq.newBuilder();
        ReflectionUtil.copyProperties(data, builder);
        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_INVITE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.INVITE_DISCUSSION, JSONObject.toJSONString(data));
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
