package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.NoDisturbData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置免打扰
 *
 * @author zhangxu
 */
public class ReqUpdateFriendNoDisturbProcessor extends AbstractProcessor<NoDisturbData, Void, Void> {
    @Override
    public Void request(NoDisturbData data) {
        byte[] send = Messages.UpdateFriendNoDisturbReq.newBuilder()
                .setFriendUserId(data.getUserId())
                .setNoDisturbSetting(data.getNoDisturb())
                .build().toByteArray();

        int cliSeqId = ContextHolder.getMessageService().sendMessage(CMD.REQ_UPDATE_FRIEND_NODISTURB, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_NO_DISTURB, data.getUserId());

        return null;
    }
}
