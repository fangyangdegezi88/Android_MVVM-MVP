package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设置群规则响应
 *
 * @author zhangxu
 */
public class RspSetGroupRuleProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspSetGroupRuleProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.SetGroupRuleRsp rsp = Messages.SetGroupRuleRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateSetGroupRuleSuccessful(JSONObject.toJSONString(new GroupRuleData(rsp.getGroupId(), rsp.getGroupRule())));
    }
}
