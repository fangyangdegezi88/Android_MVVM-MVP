package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
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
public class RspFriendInfoProcessor extends AbstractUserProcessor<Void, Void, Messages.FriendInfoRsp> {
    private Logger logger = LoggerFactory.getLogger(RspFriendInfoProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.FriendInfoRsp rsp = Messages.FriendInfoRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        internalCycle(rsp);
    }

    @Override
    public void internalCycle(Messages.FriendInfoRsp message) {
        MTModel model = getSessionManager().getCurrent();
        UserBase userBase = model.addOrUpdateUserBase(new UserBase(message.getFriend()), true);
        refreshUserBase(getSessionManager().getUserId(), new FriendBase(message), userBase);
    }
}
