package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.SetGroupAdminNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 群主更新用户的类型（设置和取消管理员），给全群成员的系统通知
 *
 * @author zhangxu
 */
public class SysNtySetGroupAdminProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.SetGroupAdminSysNty nty = Messages.SetGroupAdminSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }


        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_SET_GROUP_ADMIN, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.SetGroupAdminSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.SetGroupAdminSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();
        final Messages.UserType userType = nty.getEnable() == Messages.Enable.ENABLE ? Messages.UserType.ADMIN : Messages.UserType.NORMAL;

        SetGroupAdminNotify notify = new SetGroupAdminNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setUserIds(nty.getUserIdsList());
        notify.setUserType(userType);

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().updateGroupUserUserType(userId, nty.getGroupId(), nty.getUserIdsList(), userType, IGroupService.FEATURE_FOREVER);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserTypeChanged(nty.getGroupId(), JSONObject.toJSONString(nty.getUserIdsList()), userType.getNumber());
        }
    }
}
