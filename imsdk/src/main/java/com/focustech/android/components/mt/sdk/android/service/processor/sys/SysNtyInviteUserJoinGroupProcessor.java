package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteJoinToGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户被邀请加入群
 *
 * @author zhangxu
 */
public class SysNtyInviteUserJoinGroupProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.InviteUserJoinGroupSysNty nty = Messages.InviteUserJoinGroupSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_INVITE_USER_JOIN_GROUP, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.InviteUserJoinGroupSysNty.parseFrom(data));
    }

    private void doBiz(Messages.InviteUserJoinGroupSysNty nty) throws Throwable {
        InviteJoinToGroupNotify notify = new InviteJoinToGroupNotify();
        ReflectionUtil.copyProperties(nty, notify);
        createConversation(null, notify, true);
    }
}
