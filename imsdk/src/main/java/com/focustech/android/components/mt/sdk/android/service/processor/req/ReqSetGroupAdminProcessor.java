package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.SetGroupAdminData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群管理员
 *
 * @author zhangxu
 */
public class ReqSetGroupAdminProcessor extends AbstractGroupProcessor<SetGroupAdminData, Void, Void> {
    @Override
    public Void request(SetGroupAdminData data) {
        byte[] send = Messages.SetGroupAdminReq.newBuilder()
                .setGroupId(data.getGroupId())
                .setEnable(data.getEnable())
                .addAllUserIds(data.getUserIds())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_SET_ADMIN, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.DISABLE_GROUP, JSONObject.toJSONString(data));

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }
}
