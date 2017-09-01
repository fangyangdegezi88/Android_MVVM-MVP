package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 讨论组失效
 *
 * @author zhangxu
 */
public class RspDiscussionInvalidProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.DiscussionInvalidRsp rsp = Messages.DiscussionInvalidRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().delete(userId, rsp.getDiscussionId(), IGroupService.FEATURE_TEMP);
            }
        });

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateDiscussionExpired(rsp.getDiscussionId());
        }
    }
}
