package com.focustech.android.components.mt.sdk.android.service.processor.nty;

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
public class NtyDeleteFriendProcessor extends AbstractUserProcessor<Void, Void, Void> {
    private Logger logger = LoggerFactory.getLogger(NtyDeleteFriendProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.DeleteFriendNty rsp = Messages.DeleteFriendNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        CMD.RSP_DELETE_FRIEND.getProcessor().internalCycle(rsp.getFriendUserId());
    }
}
