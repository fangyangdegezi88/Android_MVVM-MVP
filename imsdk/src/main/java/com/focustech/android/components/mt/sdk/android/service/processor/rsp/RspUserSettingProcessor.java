package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.Settings;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.BeanConverter;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息响应
 *
 * @author zhangxu
 */
public class RspUserSettingProcessor extends AbstractUserProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUserSettingProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        final Messages.UserSettingRsp rsp = Messages.UserSettingRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        AbstractAsyncContext content = AsyncContent.getContent(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));

        if (null == content) {
            return;
        }

        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 更新设置

                try {
                    Settings settings = BeanConverter.toSettings(rsp);
                    settings.setUserId(userId);
                    Settings newestSettings = getSettingService().addOrUpdate(userId, settings);

                    UserSettingData data = new UserSettingData();
                    data.setAllowChatRecordOnServer(Messages.Enable.valueOf(newestSettings.getAllowChatRecordOnServer().intValue()));
                    data.setAllowStrangerChatToMe(Messages.Enable.valueOf(newestSettings.getAllowStrangerChatToMe().intValue()));
                    data.setFriendRule(Messages.ValidateRule.valueOf(newestSettings.getFriendRule().intValue()));
                    data.setGroupRule(Messages.ValidateRule.valueOf(newestSettings.getGroupRule().intValue()));
                    data.setCustomerSettings(JSONObject.parseObject(newestSettings.getCustomerSettings(), UserSettingData.CustomerSettingData.class));

                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateSettingChanged(JSONObject.toJSONString(data));
                    }
                } catch (Throwable e) {
                    Log.e("setting", "error", e);
                }
            }
        });
    }
}
