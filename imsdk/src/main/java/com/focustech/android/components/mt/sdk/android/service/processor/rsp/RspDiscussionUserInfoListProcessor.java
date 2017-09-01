package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 拉取群用户信息响应
 *
 * @author zhangxu
 */
public class RspDiscussionUserInfoListProcessor extends AbstractGroupProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionUserInfosRsp rspList = Messages.DiscussionUserInfosRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rspList);
        }

        for (Messages.DiscussionUserInfoRsp rsp : rspList.getDiscussionUserInfosList()) {
            updateDiscussionUser(rsp);
        }

        rspList.getDiscussionId();
    }
}
