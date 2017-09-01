package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeInviteUserJoinGroupSucceededNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 被邀请用户同意加入群的成功通知。system->群主或管理员
 *
 * @author zhangxu
 */
public class SysNtyAgreeInviteUserJoinGroupSucceededProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.AgreeInviteUserJoinGroupSucceededSysNty nty = Messages.AgreeInviteUserJoinGroupSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_AGREE_INVITE_USER_JOIN_GROUP_SUCCEEDED, Messages.RecentContactType.GROUP
                , nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);

    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.AgreeInviteUserJoinGroupSucceededSysNty.parseFrom(data));
    }

    private void doBiz(Messages.AgreeInviteUserJoinGroupSucceededSysNty nty) throws Throwable {
        AgreeInviteUserJoinGroupSucceededNotify notify = new AgreeInviteUserJoinGroupSucceededNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setInvitedUserIds(nty.getInvitedUserIdsList());
        notify.setInvitedUserNames(nty.getInvitedUserNamesList());

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserInviteAgreedToAdmin(JSONObject.toJSONString(notify));
        }
    }
}
