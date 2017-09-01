package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DeleteGroupUserData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 删除群成员
 *
 * @author zhangxu
 */
public class ReqDeleteGroupUserProcessor extends AbstractGroupProcessor<DeleteGroupUserData, Void, Void> {
    @Override
    public Void request(DeleteGroupUserData data) {
        byte[] send = Messages.DeleteGroupUserReq.newBuilder()
                .setGroupId(data.getGroupId())
                .addAllUserIds(data.getGroupUserIds())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_DELETE_USER, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DELETE_GROUP_USER, JSONObject.toJSONString(data));

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
