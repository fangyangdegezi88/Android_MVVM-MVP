package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 创建群成功
 *
 * @author zhangxu
 */
public class RspCreateGroupSuccessfulProcessor extends AbstractProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.CreateGroupSuccessfulRsp groupRsp = Messages.CreateGroupSuccessfulRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), groupRsp);
        }

        MTModel model = getSessionManager().getCurrent();

        if (null == model) {
            return;
        }

        final String userId = getSessionManager().getUserId();

        MTGroups groups = getSessionManager().getCurrent().getGroups();
        final MTGroup group = new MTGroup(groupRsp);
        groups.addOrUpdateGroup(group);

        // 通知客户端
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupChanged(JSONObject.toJSONString(group));
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().addOrUpdate(userId, group, IGroupService.FEATURE_FOREVER);
            }
        });
    }
}
