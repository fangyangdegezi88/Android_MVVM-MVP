package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 更新用户数据
 *
 * @author zhangxu
 */
public class ReqUpdateUserInfoProcessor extends AbstractProcessor<UserBase, Void, Void> {
    @Override
    public Void request(UserBase data) {
        Messages.UpdateUserInfoReq.Builder builder = Messages.UpdateUserInfoReq.newBuilder();

        if (null != data.getUserHeadId()) {
            builder.setUserHeadId(data.getUserHeadId());
        }

        if (null != data.getUserNickName()) {
            builder.setUserNickName(data.getUserNickName());
        }

        if (null != data.getUserSignature()) {
            builder.setUserSignature(data.getUserSignature());
        }

        int cliSeqId = getMessageService().sendMessage(CMD.REQ_UPDATE_USER_INFO, builder.build().toByteArray());
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_USER_INFO);

        return null;
    }
}
