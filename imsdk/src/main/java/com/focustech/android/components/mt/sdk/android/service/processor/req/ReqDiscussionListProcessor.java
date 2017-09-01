package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求讨论组列表
 *
 * @author zhangxu
 */
public class ReqDiscussionListProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        long timestamp = getLastTimestampService().getDataTimestamp(getSessionManager().getUserId(), LastTimestampType.DATA_DISCUSSION_LIST);

        byte[] send = Messages.DiscussionsReq.newBuilder().setTimestamp(timestamp).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_LIST, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GET_DISCUSSION_LIST);
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
