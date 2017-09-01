package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupSettingData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 更新群用户设置
 *
 * @author zhangxu
 */
public class ReqUpdateGroupUserSettingProcessor extends AbstractProcessor<GroupSettingData, Void, Void> {
    @Override
    public Void request(GroupSettingData setting) {
        byte[] send = Messages.UpdateGroupUserSettingReq.newBuilder()
                .setGroupId(setting.getGroupId())
                .setMessageSetting(setting.getMessageSetting())
                .setNewSetting(setting.getSetting())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_UPDATE_USER_SETTING, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_GROUP_USER_SETTING, JSONObject.toJSONString(setting));
        return null;
    }
}
