package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 好友为提醒设置响应
 *
 * @author zhangxu
 */
public class RspUpdateFriendNoDisturbProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.UpdateFriendNoDisturbRsp rsp = Messages.UpdateFriendNoDisturbRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateUpdateFriendNoDisturbChanged(rsp.getFriendUserId(), rsp.getNoDisturbSetting().name());
    }
}
