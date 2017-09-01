package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 拉取响应
 *
 * @author zhangxu
 */
public class RspDiscussionInfoListProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionsInfoRsp groupRsp = Messages.DiscussionsInfoRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), groupRsp);
        }

        for (Messages.DiscussionInfoRsp rsp : groupRsp.getDiscussionInfoRspList()) {
            updateDiscussionInfo(rsp);
        }
    }
}
