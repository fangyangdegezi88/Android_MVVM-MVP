package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;

/**
 * Created by liuzaibing on 2016/2/25.
 */
public class NtyStudentInfoChangeProcessor extends AbstractProcessor {

    @Override
    public void onMessage(TMMessage message) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), message.getBody());
        }

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateStudentInfoChange();
        }
    }
}
