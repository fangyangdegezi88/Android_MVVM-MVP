package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddAndAgreeFriendSucceededToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 同意并且添加好友成功
 *
 * @author zhangxu
 */
public class SysNtyAddAndAgreeFriendSucceededProcessor extends AbstractUserProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.AddAndAgreeFriendSucceededSysNty nty = Messages.AddAndAgreeFriendSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 存消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_ADDED_AND_AGREE_FRIEND_SUCCEEDED_TO_TARGET, Messages.RecentContactType.PERSON, nty.getSrcFriendUserId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.AddAndAgreeFriendSucceededSysNty.parseFrom(data));
    }

    private void doBiz(Messages.AddAndAgreeFriendSucceededSysNty nty) throws Throwable {
        AddAndAgreeFriendSucceededToTargetNotify notify = new AddAndAgreeFriendSucceededToTargetNotify();
        ReflectionUtil.copyProperties(nty, notify);
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateAddFriendSucceededToTargetNotify(JSONObject.toJSONString(notify));
        }
    }
}
