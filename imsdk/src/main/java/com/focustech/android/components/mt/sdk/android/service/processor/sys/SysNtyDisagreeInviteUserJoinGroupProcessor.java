package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeInviteUserJoinGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 被邀请用户拒绝加入群的成功通知。system->邀请人
 *
 * @author zhangxu
 */
public class SysNtyDisagreeInviteUserJoinGroupProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DisagreeInviteUserJoinGroupSysNty nty = Messages.DisagreeInviteUserJoinGroupSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_DISAGREE_INVITE_USER_JOIN_GROUP_SUCCEEDED, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.DisagreeInviteUserJoinGroupSysNty.parseFrom(data));
    }

    private void doBiz(Messages.DisagreeInviteUserJoinGroupSysNty nty) throws Throwable {
        DisagreeInviteUserJoinGroupNotify notify = new DisagreeInviteUserJoinGroupNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setInvitedUserIds(nty.getInvitedUserIdsList());
        notify.setInvitedUserNames(nty.getInvitedUserNamesList());
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserInviteDisagreedToAdmin(JSONObject.toJSONString(notify));
        }
    }
}
