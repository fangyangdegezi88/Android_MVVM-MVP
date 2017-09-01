package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 创建群
 *
 * @author zhangxu
 */
public class ReqCreateGroupProcessor extends AbstractProcessor<MTGroup, Void, Void> {
    @Override
    public Void request(MTGroup group) {
        Messages.AddGroupReq.Builder builder = Messages.AddGroupReq.newBuilder();
        ReflectionUtil.copyProperties(group, builder);
        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_CREATE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GROUP_CREATE, JSONObject.toJSONString(group));
        return null;
    }
}
