package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 群停用响应
 *
 * @author zhangxu
 */
public class RspGroupDisabledProcessor extends AbstractProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GroupDisableRsp rsp = Messages.GroupDisableRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateGroupDisabled(rsp.getGroupId());
    }
}
