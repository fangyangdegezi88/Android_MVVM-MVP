package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取群规则响应
 *
 * @author zhangxu
 */
public class RspExitGroupProcessor extends AbstractGroupProcessor {
    private Logger logger = LoggerFactory.getLogger(RspExitGroupProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        final Messages.ExitGroupRsp rsp = Messages.ExitGroupRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().deleteGroupUser(userId, rsp.getGroupId(), rsp.getUserId(), IGroupService.FEATURE_FOREVER);
                updateLastTimestamp(userId, rsp.getGroupId(), LastTimestampType.DATA_GROUP_USER_INFO);
            }
        });

        // 通知用户有人退出群了
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateUserExitGroup(rsp.getGroupId(), rsp.getUserId());
        }
    }
}
