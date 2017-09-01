package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 创建讨论组
 *
 * @author zhangxu
 */
public class ReqExitDiscussionProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String discussionId) {
        Messages.ExitDiscussionReq.Builder builder = Messages.ExitDiscussionReq.newBuilder();
        builder.setDiscussionId(discussionId);
        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_EXIT, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DISCUSSION_EXIT, discussionId);
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
