package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 修改群名称响应
 *
 * @author zhangxu
 */
public class RspUpdateDiscussionNameProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.UpdateDiscussionNameRsp rsp = Messages.UpdateDiscussionNameRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                MTGroup group = new MTGroup();
                group.setGroupId(rsp.getDiscussionId());
                group.setGroupName(rsp.getDiscussionName());
                getGroupService().addOrUpdate(userId, group, IGroupService.FEATURE_TEMP);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateDiscussionNameChanged(rsp.getDiscussionId(), rsp.getDiscussionName());
        }
    }
}
