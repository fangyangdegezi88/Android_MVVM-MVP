package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 拉取群用户信息响应
 *
 * @author zhangxu
 */
public class RspDeleteGroupUserProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.DeleteGroupUserRsp rsp = Messages.DeleteGroupUserRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().deleteGroupUser(userId, rsp.getGroupId(), rsp.getUserIdsList(), IGroupService.FEATURE_FOREVER);
            }
        });
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateGroupUserDeleted(rsp.getGroupId(), JSONObject.toJSONString(rsp.getUserIdsList()));
    }
}
