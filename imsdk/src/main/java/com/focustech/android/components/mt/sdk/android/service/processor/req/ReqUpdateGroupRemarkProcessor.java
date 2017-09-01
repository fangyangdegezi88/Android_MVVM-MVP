package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 更新在群中的昵称设置
 *
 * @author zhangxu
 */
public class ReqUpdateGroupRemarkProcessor extends AbstractProcessor<MTGroup, Void, Void> {
    @Override
    public Void request(MTGroup group) {
        byte[] send = Messages.UpdateGroupRemarkReq.newBuilder()
                .setGroupId(group.getGroupId())
                .setNewGroupRemark(group.getGroupRemark())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_UPDATE_REMARK, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_GROUP_REMARK, group.getGroupId());
        return null;
    }
}
