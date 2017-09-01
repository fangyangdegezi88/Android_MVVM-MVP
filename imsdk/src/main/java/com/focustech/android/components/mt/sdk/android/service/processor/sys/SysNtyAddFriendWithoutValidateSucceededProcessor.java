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
 * 不需要验证，添加好友成功
 *
 * @author zhangxu
 */
public class SysNtyAddFriendWithoutValidateSucceededProcessor extends AbstractUserProcessor<Void, Void, Messages.AddFriendWithoutValidateSucceededSysNty> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.AddFriendWithoutValidateSucceededSysNty nty = Messages.AddFriendWithoutValidateSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 存消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_ADD_FRIEND_WITHOUT_VALIDATE_SUCCEEDED, Messages.RecentContactType.PERSON, nty.getTargetFriendUserId(), message.getBody());

        internalCycle(nty);

        getSystemNotifyService().processed(sys);

    }

    @Override
    public void internalCycle(Messages.AddFriendWithoutValidateSucceededSysNty nty) throws Throwable {
        AddFriendSucceededToSrcNotify notify = new AddFriendSucceededToSrcNotify();
        ReflectionUtil.copyProperties(nty, notify);

        // 通知前台加好友成功
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateAddFriendSucceededToSrcNotify(JSONObject.toJSONString(notify));
        }
    }
}
