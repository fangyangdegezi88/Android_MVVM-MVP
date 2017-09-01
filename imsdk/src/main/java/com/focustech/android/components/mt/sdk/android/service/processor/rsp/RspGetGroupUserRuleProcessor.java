package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取群用户规则响应
 *
 * @author zhangxu
 */
public class RspGetGroupUserRuleProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspGetGroupUserRuleProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.GetGroupUserRuleRsp rsp = Messages.GetGroupUserRuleRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateGetGroupUserRuleSuccessful(JSONObject.toJSONString(new GroupUserRuleData(rsp.getUserId(), rsp.getGroupId(), rsp.getGroupRule())));
    }
}
