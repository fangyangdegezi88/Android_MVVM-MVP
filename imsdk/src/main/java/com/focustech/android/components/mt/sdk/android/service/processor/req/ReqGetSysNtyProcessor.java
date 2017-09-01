package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 拉取系统消息
 *
 * @author zhangxu
 */
public class ReqGetSysNtyProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        byte[] send = Messages.GetSysNtyReq.newBuilder().setTimestamp(0).setEquipment(Messages.Equipment.MOBILE_ANDROID).build().toByteArray();
        getMessageService().sendMessage(CMD.REQ_GET_SYS_NTY, send);
        return null;
    }
}
