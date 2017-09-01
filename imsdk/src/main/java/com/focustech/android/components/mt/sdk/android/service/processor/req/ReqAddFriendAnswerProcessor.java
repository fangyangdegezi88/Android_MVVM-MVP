package com.focustech.android.components.mt.sdk.android.service.processor.req;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAnswer;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 加好友响应
 *
 * @author zhangxu
 */
public class ReqAddFriendAnswerProcessor extends AbstractProcessor<AddFriendAnswer, Void, Void> {
    @Override
    public Void request(AddFriendAnswer data) {
        byte[] send = Messages.AddFriendAnswerReq.newBuilder()
                .setExt(data.getExt())
                .setSrcFriendGroupId(data.getSrcFriendGroupId())
                .setAnswer(data.getAnswer())
                .setSelfFriendGroupId(data.getSelfFriendGroupId())
                .setSvrMsgId(data.getSvrMsgId())
                .setSrcFriendUserId(data.getSrcFriendUserId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_ADD_FRIEND_ANSWER, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.ADD_FRIEND_ANSWER, data.getSrcFriendUserId());

        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        final String userId = getSessionManager().getUserId();
        final String targetFriendUserId = context.getOperationData();

        // 标记已处理
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getSystemNotifyService().processed(userId, CMD.SYS_NTY_ADD_FRIEND.getValue(), Long.valueOf(Messages.RecentContactType.PERSON_VALUE), targetFriendUserId);
            }
        });

        return true;
    }
}
