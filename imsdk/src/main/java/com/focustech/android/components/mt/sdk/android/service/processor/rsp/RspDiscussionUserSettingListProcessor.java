package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupSetting;
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
public class RspDiscussionUserSettingListProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspDiscussionUserSettingListProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        final Messages.DiscussionUserSettingRsp rspList = Messages.DiscussionUserSettingRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rspList);
        }

        for (Messages.UpdateDiscussionUserSettingRsp rsp : rspList.getNewSettingList()) {
            String userId = getSessionManager().getUserId();
            final GroupSetting setting = new GroupSetting();
            setting.setUserId(userId);
            setting.setFeature(IGroupService.FEATURE_TEMP);
            setting.setGroupId(rsp.getDiscussionId());
            setting.setMessageSetting(rsp.getMessageSetting().getNumber());
            setting.setSetting(rsp.getNewSetting());

            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getGroupService().addOrUpdateGroupSetting(setting.getUserId(), setting, IGroupService.FEATURE_TEMP);
                }
            });

            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateDiscussionUserSettingChanged(JSONObject.toJSONString(setting));
            }
        }
    }
}
