package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
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
public class RspUpdateGroupRemarkProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUpdateGroupRemarkProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.UpdateGroupRemarkRsp rsp = Messages.UpdateGroupRemarkRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        final String userId = getSessionManager().getUserId();
        final MTGroup group = new MTGroup();
        group.setGroupRemark(rsp.getNewGroupRemark());
        group.setGroupId(rsp.getGroupId());


        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().addOrUpdate(userId, group, IGroupService.FEATURE_FOREVER);
                updateLastTimestamp(userId, group.getGroupId(), LastTimestampType.DATA_GROUP_LIST);
            }
        });
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateUpdateGroupRemarkSuccessful(rsp.getGroupId());
    }
}
