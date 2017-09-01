package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 好友信息通知
 *
 * @author zhangxu
 */
public class NtyFriendInfoProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.FriendInfoNty nty = Messages.FriendInfoNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        MTModel model = getSessionManager().getCurrent();
        String userId = getSessionManager().getUserId();

        if (null != model) {
            UserBase userBase = model.addOrUpdateUserBase(new UserBase(nty.getFriend()), true);
            refreshUserBase(userId, new FriendBase(nty), userBase);
        }
    }
}
