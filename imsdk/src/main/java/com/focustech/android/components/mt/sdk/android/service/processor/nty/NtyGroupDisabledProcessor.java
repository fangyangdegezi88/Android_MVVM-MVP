package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 群主停用群通知。system->所有群成员
 *
 * @author zhangxu
 */
public class NtyGroupDisabledProcessor extends AbstractGroupProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DisableGroupNty nty = Messages.DisableGroupNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        final String userId = getSessionManager().getUserId();
        final String groupId = nty.getGroupId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().delete(userId, groupId, IGroupService.FEATURE_FOREVER);
            }
        });

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupDisabled(groupId);
        }
    }
}
