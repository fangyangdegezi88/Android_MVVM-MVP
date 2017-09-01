package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取群规则响应
 *
 * @author zhangxu
 */
public class RspUpdateGroupNickNameProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUpdateGroupNickNameProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.UpdateGroupNickNameRsp rsp = Messages.UpdateGroupNickNameRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();
        final MTGroupUser user = new MTGroupUser();
        user.setGroupUserId(rsp.getUserId());
        user.setGroupId(rsp.getGroupId());
        user.setGroupNickName(rsp.getNewGroupNickName());

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().addOrUpdateGroupUser(userId, user, IGroupService.FEATURE_FOREVER);
                updateLastTimestamp(userId, user.getGroupId(), LastTimestampType.DATA_GROUP_USER_INFO);
            }
        });
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateGroupNickNameChanged(rsp.getGroupId(), rsp.getUserId(), rsp.getNewGroupNickName());
    }
}
