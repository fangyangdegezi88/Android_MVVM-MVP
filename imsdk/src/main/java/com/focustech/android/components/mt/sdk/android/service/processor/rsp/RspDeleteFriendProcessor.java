package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import android.os.RemoteException;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友信息响应
 *
 * @author zhangxu
 */
public class RspDeleteFriendProcessor extends AbstractUserProcessor<Void, Void, String> {
    private Logger logger = LoggerFactory.getLogger(RspDeleteFriendProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.DeleteFriendRsp rsp = Messages.DeleteFriendRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        internalCycle(rsp.getFriendUserId());
    }

    @Override
    public void internalCycle(final String friendUserId) {
        MTModel model = getSessionManager().getCurrent();

        if (model != null) {
            final String userId = model.getCurrent().getUserId();
            FriendBase friendBase = model.getFriendGroups().deleteFriend(friendUserId);
            final String friendGroupId = friendBase.getFriendGroupId();

            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getFriendService().deleteFriend(userId, friendUserId, friendGroupId);
                }
            });
        }

        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateFriendDeleteSuccessful(friendUserId);
            }
        } catch (RemoteException e) {
            // TODO 日志
        }
    }
}
