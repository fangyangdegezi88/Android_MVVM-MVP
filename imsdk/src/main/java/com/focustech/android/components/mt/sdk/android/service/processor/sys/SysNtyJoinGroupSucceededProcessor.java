package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 群设置为不需要验证，system -> 群申请人
 *
 * @author zhangxu
 */
public class SysNtyJoinGroupSucceededProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.JoinGroupSucceededSysNty nty = Messages.JoinGroupSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_JOIN_GROUP_SUCCEEDED, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.JoinGroupSucceededSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.JoinGroupSucceededSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                MTGroup group = new MTGroup();
                group.setGroupId(nty.getGroupId());
                group.setGroupName(nty.getGroupName());

                getGroupService().addOrUpdate(userId, group, IGroupService.FEATURE_FOREVER);
            }
        });

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateJoinGroupSuccessful(nty.getGroupId(), nty.getGroupName());
        }
    }
}
