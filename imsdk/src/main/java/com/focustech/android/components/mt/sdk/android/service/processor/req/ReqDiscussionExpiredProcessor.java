package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 检查讨论组是否过期
 *
 * @author zhangxu
 */
public class ReqDiscussionExpiredProcessor extends AbstractProcessor<List<String>, Void, Void> {
    @Override
    public Void request(List<String> data) {
        Messages.DiscussionExpiredReq.Builder builder = Messages.DiscussionExpiredReq.newBuilder();
        builder.addAllDiscussionIds(data);
        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_EXPIRED, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DISCUSSION_EXPIRED, JSONObject.toJSONString(data));
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
