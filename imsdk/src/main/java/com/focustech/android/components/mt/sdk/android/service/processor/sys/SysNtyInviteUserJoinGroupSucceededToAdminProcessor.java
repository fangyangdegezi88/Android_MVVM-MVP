package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToAdminNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户被邀请加入群成功,给所有管理员(包括群主)
 *
 * @author zhangxu
 */
public class SysNtyInviteUserJoinGroupSucceededToAdminProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.InviteUserJoinGroupSucceededSysNty nty = Messages.InviteUserJoinGroupSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_INVITE_USER_JOIN_GROUP_SUCCEEDED, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.InviteUserJoinGroupSucceededSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.InviteUserJoinGroupSucceededSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                for (String groupUserId : nty.getInvitedUserIdsList()) {
                    getGroupService().addOrUpdateGroupUser(userId, new MTGroupUser(groupUserId, nty.getGroupId()), IGroupService.FEATURE_FOREVER);
                }
            }
        });

        InviteUserJoinGroupSucceededToAdminNotify notify = new InviteUserJoinGroupSucceededToAdminNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setInvitedUserIds(nty.getInvitedUserIdsList());
        notify.setInvitedUserNames(nty.getInvitedUserNamesList());
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserInviteSucceededToAdmin(JSONObject.toJSONString(notify));
        }
    }
}
