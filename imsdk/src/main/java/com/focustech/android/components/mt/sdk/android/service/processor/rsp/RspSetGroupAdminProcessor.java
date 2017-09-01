package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 设置群管理员，system->给在线所有群用户
 *
 * @author zhangxu
 */
public class RspSetGroupAdminProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.SetGroupAdminRsp rsp = Messages.SetGroupAdminRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final String userId = getSessionManager().getUserId();
        final Messages.UserType userType = rsp.getEnable() == Messages.Enable.ENABLE ? Messages.UserType.ADMIN : Messages.UserType.NORMAL;

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().updateGroupUserUserType(userId, rsp.getGroupId(), rsp.getUserIdsList(), userType, IGroupService.FEATURE_FOREVER);
            }
        });

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserTypeChanged(rsp.getGroupId(), JSONObject.toJSONString(rsp.getUserIdsList()), userType.getNumber());
        }
    }
}
