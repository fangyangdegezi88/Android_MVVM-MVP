package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.communicate.Communication;
import com.focustech.android.components.mt.sdk.communicate.CommunicationContent;
import com.focustech.android.components.mt.sdk.communicate.CommunicationType;
import com.focustech.android.components.mt.sdk.communicate.sysnty.Transmission;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.lang.StringUtils;

/**
 * @version : V1.0.0
 * @描述 :  透传系统通知
 * 由云端推送，mt转发的系统通知类型
 * @user : zhangzeyu
 * @date : 2016/4/5
 */
public class SysNtyTransmissionProcessor extends AbstractProcessor {

    @Override
    public void onMessage(TMMessage message) throws Throwable {
        super.onMessage(message);
        Messages.TransmissionSysNty nty = Messages.TransmissionSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }
        if (null != nty && StringUtils.isNotEmpty(nty.getContext())) {
            Transmission transmission = JSONObject.parseObject(nty.getContext(), Transmission.class);
            if (null != transmission)
                addMessage(new Communication(CommunicationType.SYSNTY, new CommunicationContent<Transmission>(transmission, NTPTime.now())));
        }
    }


}
