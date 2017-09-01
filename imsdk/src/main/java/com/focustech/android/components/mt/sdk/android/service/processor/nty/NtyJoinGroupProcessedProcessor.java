package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.JoinGroupProcessedNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 其他管理员处理了加入群的请求通知
 *
 * @author zhangxu
 */
public class NtyJoinGroupProcessedProcessor extends AbstractGroupProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.JoinGroupProcessedNty nty = Messages.JoinGroupProcessedNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 该群的邀请已经处理
                getSystemNotifyService().processed(userId, CMD.SYS_NTY_JOIN_GROUP_VALIDATE.getValue()
                        , Long.valueOf(Messages.RecentContactType.GROUP_VALUE), nty.getGroupId(), nty.getUserId());
            }
        });

        JoinGroupProcessedNotify notify = new JoinGroupProcessedNotify();
        ReflectionUtil.copyProperties(nty, notify);
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateJoinGroupProcessed(JSONObject.toJSONString(notify));
        }
    }
}
