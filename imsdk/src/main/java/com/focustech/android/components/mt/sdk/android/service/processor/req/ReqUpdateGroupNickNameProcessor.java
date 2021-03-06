package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 更新在群中的昵称设置
 *
 * @author zhangxu
 */
public class ReqUpdateGroupNickNameProcessor extends AbstractProcessor<MTGroupUser, Void, Void> {
    @Override
    public Void request(MTGroupUser setting) {
        byte[] send = Messages.UpdateGroupNickNameReq.newBuilder()
                .setGroupId(setting.getGroupId())
                .setUserId(setting.getGroupUserId())
                .setNewGroupNickName(setting.getGroupNickName())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_UPDATE_NICKNAME, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_GROUP_NICKNAME, JSONObject.toJSONString(setting));
        return null;
    }
}
