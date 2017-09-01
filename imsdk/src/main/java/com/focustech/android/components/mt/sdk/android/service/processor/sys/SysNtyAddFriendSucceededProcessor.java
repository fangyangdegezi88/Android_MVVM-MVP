package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendSucceededToSrcNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 加好友成功通知, system -> src
 *
 * @author zhangxu
 */
public class SysNtyAddFriendSucceededProcessor extends AbstractUserProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.AddFriendSucceededSysNty nty = Messages.AddFriendSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 存消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_ADD_FRIEND_SUCCEEDED, Messages.RecentContactType.PERSON, nty.getTargetFriendUserId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.AddFriendSucceededSysNty.parseFrom(data));
    }

    private void doBiz(Messages.AddFriendSucceededSysNty nty) throws Throwable {
        AddFriendSucceededToSrcNotify notify = new AddFriendSucceededToSrcNotify();
        ReflectionUtil.copyProperties(nty, notify);

        // 通知前台加好友成功
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateAddFriendSucceededToSrcNotify(JSONObject.toJSONString(notify));
        }
    }
}
