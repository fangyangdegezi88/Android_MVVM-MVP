package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.JoinToGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户申请加入群，给所有的群管理员发送的验证系统通知
 *
 * @author zhangxu
 */
public class SysNtyJoinGroupValidateProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.JoinGroupValidateSysNty nty = Messages.JoinGroupValidateSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        addSystemNotify(CMD.SYS_NTY_JOIN_GROUP_VALIDATE, Messages.RecentContactType.GROUP, nty.getGroupId(), nty.getUserId(), message.getBody());

        doBiz(nty);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.JoinGroupValidateSysNty.parseFrom(data));
    }

    private void doBiz(Messages.JoinGroupValidateSysNty nty) throws Throwable {
        JoinToGroupNotify notify = new JoinToGroupNotify();
        ReflectionUtil.copyProperties(nty, notify);
        createConversation(null, notify, true);
    }
}
