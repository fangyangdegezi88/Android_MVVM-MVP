package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 状态更新通知
 *
 * @author zhangxu
 */
public class NtyUpdateUserStatusProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.UpdateUserStatusNty nty = Messages.UpdateUserStatusNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        String userId = nty.getUserId();
        Messages.Status status = nty.getStatus().getStatus();
        Messages.Equipment equipment = nty.getStatus().getEquipment();

        if (getSessionManager().isSelfEquipment(userId, equipment)) {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyStatusChanged(status.name());
            }
        } else if (getSessionManager().isSelfOtherEquipment(userId, equipment)) {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyEquipmentStatusChanged(equipment.name(), status.name());
            }
        } else {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateStatusChanged(userId, equipment.name(), status.name());
            }
        }
    }
}
