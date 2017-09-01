package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 修改群信息
 *
 * @author zhangxu
 */
public class ReqUpdateGroupInfoProcessor extends AbstractProcessor<MTGroup, Void, Void> {
    @Override
    public Void request(MTGroup group) {
        Messages.UpdateGroupInfoReq.Builder builder = Messages.UpdateGroupInfoReq.newBuilder();
        ReflectionUtil.copyProperties(group, builder);
        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_UPDATE_INFO, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_GROUP_INFO, group.getGroupId());
        return null;
    }
}
