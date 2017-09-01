package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 修改讨论组名称
 *
 * @author zhangxu
 */
public class ReqDiscussionUpdateNameProcessor extends AbstractProcessor<MTGroup, Void, Void> {
    @Override
    public Void request(MTGroup group) {
        byte[] send = Messages.UpdateDiscussionNameReq.newBuilder()
                .setDiscussionId(group.getGroupId())
                .setDiscussionName(group.getGroupName())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_DISCUSSION_UPDATE_NAME, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DISCUSSION_UPDATE_NAME, JSONObject.toJSONString(group));

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
