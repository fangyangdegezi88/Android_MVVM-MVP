package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendFailNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 添加好友失败
 * <pre>
 *     1. 对方拒绝
 *     2. 对方设置为决绝所有人
 * </pre>
 *
 * @author zhangxu
 */
public class SysNtyAddFriendFailProcessor extends AbstractUserProcessor<Void, Void, Messages.AddFriendFailSysNty> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.AddFriendFailSysNty nty = Messages.AddFriendFailSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 存消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_ADD_FRIEND_FAIL, Messages.RecentContactType.PERSON, nty.getTargetFriendUserId(), message.getBody());

        internalCycle(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(Messages.AddFriendFailSysNty nty) throws Throwable {
        AddFriendFailNotify notify = new AddFriendFailNotify();
        ReflectionUtil.copyProperties(nty, notify);
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateAddFriendFailNotify(JSONObject.toJSONString(notify));
        }
    }
}
