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
 * 被他人添加为好友通知
 *
 * @author zhangxu
 */
public class SysNtyAddedFriendSucceededProcessor extends AbstractUserProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.AddedFriendSucceededSysNty nty = Messages.AddedFriendSucceededSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 存消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_ADDED_FRIEND_SUCCEEDED, Messages.RecentContactType.PERSON, nty.getSrcFriendUserId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.AddedFriendSucceededSysNty.parseFrom(data));
    }

    private void doBiz(Messages.AddedFriendSucceededSysNty nty) throws Throwable {
        AddFriendSucceededToSrcNotify notify = new AddFriendSucceededToSrcNotify();
        ReflectionUtil.copyProperties(nty, notify);
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateAddFriendSucceededToSrcNotify(JSONObject.toJSONString(notify));
        }
    }
}
