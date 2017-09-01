package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 获取用户验证规则响应
 *
 * @author zhangxu
 */
public class RspGetFriendRuleProcessor extends AbstractProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GetFriendRuleRsp rsp = Messages.GetFriendRuleRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }


        long code = rsp.getCode();

        // 未知用户
        if (0 != code) {
            if (null != getBizInvokeCallback())
                getBizInvokeCallback().privateAddFriendFailUnknownNotify(rsp.getUserId());
        }

        Messages.ValidateRule rule = rsp.getFriendRule();
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateFriendRuleNotify(rsp.getUserId(), rule.name());
    }
}