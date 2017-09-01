package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求更新用户头像
 *
 * @author zhangxu
 */
public class ReqUpdateUserSettingProcessor extends AbstractProcessor<UserSettingData, Void, Void> {
    @Override
    public Void request(UserSettingData data) {
        Messages.UpdateUserSettingReq.Builder builder = Messages.UpdateUserSettingReq.newBuilder();

        if (null != data.getAllowChatRecordOnServer()) {
            builder.setAllowChatRecordOnServer(data.getAllowChatRecordOnServer());
        }

        if (null != data.getAllowStrangerChatToMe()) {
            builder.setAllowStrangerChatToMe(data.getAllowStrangerChatToMe());
        }

        if (null != data.getFriendRule()) {
            builder.setFriendRule(data.getFriendRule());
        }

        if (null != data.getGroupRule()) {
            builder.setGroupRule(data.getGroupRule());
        }

        if (null != data.getCustomerSettings()) {
            builder.setCustomerSettings(JSONObject.toJSONString(data.getCustomerSettings()));
        }

        byte[] send = builder.build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_UPDATE_USER_SETTING, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_USER_SETTING);

        return null;
    }
}
