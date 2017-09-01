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
public class RspUpdateGroupUserSettingProcessor extends AbstractProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUpdateGroupUserSettingProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        final Messages.UpdateGroupUserSettingRsp rsp = Messages.UpdateGroupUserSettingRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }
        String userId = getSessionManager().getUserId();
        final GroupSetting setting = new GroupSetting();
        setting.setUserId(userId);
        setting.setFeature(IGroupService.FEATURE_FOREVER);
        setting.setGroupId(rsp.getGroupId());
        setting.setMessageSetting(rsp.getMessageSetting().getNumber());
        setting.setSetting(rsp.getNewSetting());

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().addOrUpdateGroupSetting(setting.getUserId(), setting, IGroupService.FEATURE_FOREVER);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupUserSettingChanged(JSONObject.toJSONString(setting));
        }
    }
}
