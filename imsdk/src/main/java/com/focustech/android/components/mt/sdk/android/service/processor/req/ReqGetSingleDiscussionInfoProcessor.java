package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 拉取单个讨论组信息
 *
 * @author zhangxu
 */
public class ReqGetSingleDiscussionInfoProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(String discussionId) {
        byte[] send = Messages.GetSingleDiscussionInfoReq.newBuilder()
                .setDiscussionId(discussionId).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_GET_INFO, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DISCUSSION_GET_INFO, discussionId);

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
