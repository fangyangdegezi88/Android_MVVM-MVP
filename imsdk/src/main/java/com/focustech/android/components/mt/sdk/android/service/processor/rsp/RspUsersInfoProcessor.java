package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息响应
 *
 * @author zhangxu
 */
public class RspUsersInfoProcessor extends AbstractUserProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUsersInfoProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.UsersInfoRsp rsp = Messages.UsersInfoRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        for (Messages.UserInfoRsp user : rsp.getUserInfoRspList()) {
            MTModel model = getSessionManager().getCurrent();
            refreshUserBase(getSessionManager().getUserId(), model.addOrUpdateUserBase(new UserBase(user), true));
        }
    }
}
