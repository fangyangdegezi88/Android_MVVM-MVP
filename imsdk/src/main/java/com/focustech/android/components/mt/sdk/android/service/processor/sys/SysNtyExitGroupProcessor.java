package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.ExitGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户退出群的系统通知, system -> target或者system-admin|owner
 *
 * @author zhangxu
 */
public class SysNtyExitGroupProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.ExitGroupSysNty nty = Messages.ExitGroupSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }


        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_EXIT_GROUP, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.ExitGroupSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.ExitGroupSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();
        final String exitGroupUserId = nty.getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 是自己，删除群
                if (exitGroupUserId.equals(userId)) {
                    getGroupService().delete(userId, nty.getGroupId(), IGroupService.FEATURE_FOREVER);
                    updateLastTimestamp(userId, LastTimestampType.DATA_GROUP_LIST);
                } else {
                    // 其他人，删除群用户
                    getGroupService().deleteGroupUser(userId, nty.getGroupId(), exitGroupUserId, IGroupService.FEATURE_FOREVER);
                    updateLastTimestamp(userId, nty.getGroupId(), LastTimestampType.DATA_GROUP_USER_INFO);
                }
            }
        });

        ExitGroupNotify notify = new ExitGroupNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setTimestamp(nty.getTimestamp());
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserExit(JSONObject.toJSONString(notify));
        }
    }
}
