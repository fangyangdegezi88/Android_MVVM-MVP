package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 其他设备激活状态通知
 *
 * @author zhangxu
 */
public class NtyActiveStatusChangeProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.ActiveStatusChangeNty nty = Messages.ActiveStatusChangeNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 其他设备在激活状态，消息不提醒
        resetPrompt(!nty.getIsActive());
    }
}
