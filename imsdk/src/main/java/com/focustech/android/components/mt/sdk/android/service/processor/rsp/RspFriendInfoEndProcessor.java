package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息响应
 *
 * @author zhangxu
 */
public class RspFriendInfoEndProcessor extends AbstractUserProcessor {
    private Logger logger = LoggerFactory.getLogger(RspFriendInfoEndProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.FriendInfoEndRsp rsp = Messages.FriendInfoEndRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateUsersPoolChanged(JSONObject.toJSONString(getSessionManager().getCurrent().getUsers()));
        }
    }
}
