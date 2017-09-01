package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 需要验证的系统通知, system -> target
 *
 * @author zhangxu
 */
public class SysNtyAddFriendProcessor extends AbstractMessageProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(final TMMessage message) throws Throwable {
        final Messages.AddFriendSysNty nty = Messages.AddFriendSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        addSystemNotify(CMD.SYS_NTY_ADD_FRIEND, Messages.RecentContactType.PERSON, nty.getSrcUserId(), null, message.getBody());

        doBiz(nty);
    }


    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.AddFriendSysNty.parseFrom(data));
    }

    private void doBiz(Messages.AddFriendSysNty nty) throws Throwable {
        AddFriendNotify notify = new AddFriendNotify();
        ReflectionUtil.copyProperties(nty, notify);
        notify.setTimestamp(NTPTime.now());
        createConversation(null, notify, true);
    }
}
