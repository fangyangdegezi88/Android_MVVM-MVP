package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户被邀请加入群成功,给被邀请人
 *
 * @author zhangxu
 */
public class SysNtyInviteUserJoinGroupSucceededToTargetProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.InviteUserJoinGroupSucceededToUserSysNty nty = Messages.InviteUserJoinGroupSucceededToUserSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_INVITE_USER_JOIN_GROUP_SUCCEEDED_TO_USER, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.InviteUserJoinGroupSucceededToUserSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.InviteUserJoinGroupSucceededToUserSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                MTGroup group = new MTGroup();
                group.setGroupId(nty.getGroupId());
                group.setGroupName(nty.getGroupName());

                // 创建群
                getGroupService().addOrUpdate(userId, group, IGroupService.FEATURE_FOREVER);
            }
        });


        InviteUserJoinGroupSucceededToTargetNotify notify = new InviteUserJoinGroupSucceededToTargetNotify();
        ReflectionUtil.copyProperties(nty, notify);

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserInviteSucceededToTarget(JSONObject.toJSONString(notify));
        }
    }
}
