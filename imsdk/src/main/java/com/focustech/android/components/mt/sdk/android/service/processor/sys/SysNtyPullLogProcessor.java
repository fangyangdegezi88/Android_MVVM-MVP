package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * Created by zhangzeyu on 2015/11/23.
 */
public class SysNtyPullLogProcessor extends AbstractProcessor {

    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.PullLogSysNty nty = Messages.PullLogSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privatePullLog(nty.getLogId());
        }
    }

}