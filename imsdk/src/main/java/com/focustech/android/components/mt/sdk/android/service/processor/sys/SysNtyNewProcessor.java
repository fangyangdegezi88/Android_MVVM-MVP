package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 有系统消息
 *
 * @author zhangxu
 */
public class SysNtyNewProcessor extends AbstractProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.NewSysNty nty = Messages.NewSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        CMD.REQ_GET_SYS_NTY.getProcessor().request(null);
    }
}
