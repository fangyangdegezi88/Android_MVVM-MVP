package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DeletedFromGroupToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 管理员删除群成员成功通知。system->被删除的群成员
 *
 * @author zhangxu
 */
public class SysNtyDeleteGroupUserToTargetProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DeleteGroupUserSysNty nty = Messages.DeleteGroupUserSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_DELETE_GROUP_USER_TO_TARGET, Messages.RecentContactType.GROUP, nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.DeleteGroupUserSysNty.parseFrom(data));
    }

    private void doBiz(final Messages.DeleteGroupUserSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().delete(userId, nty.getGroupId(), IGroupService.FEATURE_FOREVER);
            }
        });

        DeletedFromGroupToTargetNotify notify = new DeletedFromGroupToTargetNotify();
        ReflectionUtil.copyProperties(nty, notify);

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserDeletedToTarget(JSONObject.toJSONString(notify));
        }
    }
}
