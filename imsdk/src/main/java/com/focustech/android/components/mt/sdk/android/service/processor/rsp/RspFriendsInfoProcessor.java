package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.CMD;
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
public class RspFriendsInfoProcessor extends AbstractUserProcessor {
    private Logger logger = LoggerFactory.getLogger(RspFriendsInfoProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.FriendsInfoRsp rsp = Messages.FriendsInfoRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        for (Messages.FriendInfoRsp friendInfoRsp : rsp.getFriendInfoRspList()) {
            CMD.RSP_FRIEND_INFO.getProcessor().internalCycle(friendInfoRsp);
        }
    }
}
