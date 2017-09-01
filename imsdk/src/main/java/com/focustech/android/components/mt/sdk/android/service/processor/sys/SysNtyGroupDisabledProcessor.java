package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 管理员删除群成员成功通知。system->管理员和群主
 *
 * @author zhangxu
 */
public class SysNtyGroupDisabledProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DisableGroupSysNty nty = Messages.DisableGroupSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_GROUP_DISABLED, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.DisableGroupSysNty.parseFrom(data));
    }

    private void doBiz(Messages.DisableGroupSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();
        final String groupId = nty.getGroupId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().delete(userId, groupId, IGroupService.FEATURE_FOREVER);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupDisabled(nty.getGroupId());
        }
    }
}
