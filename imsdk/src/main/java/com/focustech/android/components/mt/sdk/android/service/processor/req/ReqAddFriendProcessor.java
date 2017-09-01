package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAction;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 增加好友处理器
 *
 * @author zhangxu
 */
public class ReqAddFriendProcessor extends AbstractProcessor<AddFriendAction, Void, Void> {
    @Override
    public Void request(AddFriendAction data) {
        byte[] send = Messages.AddFriendReq.newBuilder()
                .setExt(data.getExt())
                .setSrcFriendGroupId(data.getSrcFriendGroupId())
                .setTargetFriendUserId(data.getTargetFriendUserId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_ADD_FRIEND, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.ADD_FRIEND);
        return null;
    }
}
