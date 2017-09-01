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
 * 获取群规则响应
 *
 * @author zhangxu
 */
public class RspGetGroupRuleProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspGetGroupRuleProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.GetGroupRuleRsp rsp = Messages.GetGroupRuleRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateGetGroupRuleSuccessful(JSONObject.toJSONString(new GroupRuleData(rsp.getGroupId(), rsp.getGroupRule())));
    }
}
