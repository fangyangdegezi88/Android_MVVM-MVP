package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import android.os.RemoteException;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 服务器回执
 *
 * @author zhangxu
 */
public class NtyReceiptProcessor extends AbstractProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.ReceptNty nty = Messages.ReceptNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 通知界面，网络恢复
        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateNetworkChanged(MTRuntime.getNetwork().name());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        AbstractAsyncContext context = AsyncContent.getContent(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));

        if (null != context) {
            Operation operation = context.getOperation();

            if (null != operation.getValue()) {
                boolean execComplete = operation.getValue().getProcessor().onReceipt(message, context);

                if (execComplete) {
                    operationComplete(message);
                }
            }
        }
    }
}
