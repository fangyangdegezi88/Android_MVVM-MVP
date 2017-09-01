package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 用户退出讨论组，响应给所有在线的成员
 *
 * @author zhangxu
 */
public class RspExitDiscussionProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.ExitDiscussionRsp rsp = Messages.ExitDiscussionRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();

        if (userId.equals(rsp.getUserId())) {
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getGroupService().delete(userId, rsp.getDiscussionId(), IGroupService.FEATURE_TEMP);
                }
            });
            if (null != getBizInvokeCallback())
                getBizInvokeCallback().privateDiscussionExitSuccessful(rsp.getDiscussionId());
        } else {
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getGroupService().deleteGroupUser(userId, rsp.getDiscussionId(), rsp.getUserId(), IGroupService.FEATURE_TEMP);
                }
            });
            if (null != getBizInvokeCallback())
                getBizInvokeCallback().privateDiscussionUserExit(rsp.getDiscussionId(), rsp.getUserId());
        }
    }
}
