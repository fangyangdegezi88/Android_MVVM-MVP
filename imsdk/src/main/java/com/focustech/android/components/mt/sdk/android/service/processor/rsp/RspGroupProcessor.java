package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉取响应
 *
 * @author zhangxu
 */
public class RspGroupProcessor extends AbstractProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GroupRsp groupRsp = Messages.GroupRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), groupRsp);
        }

        MTModel model = getSessionManager().getCurrent();

        if (null == model) {
            return;
        }

        MTGroups groups = getSessionManager().getCurrent().getGroups();
        MTGroup group = new MTGroup(groupRsp);
        groups.addOrUpdateGroup(group);
        List<String> userIds = new ArrayList<>();

        for (Messages.FriendStatusRsp statusRsp : groupRsp.getFriendsList()) {
            model.reset(statusRsp.getFriendUserId(), statusRsp.getStatusList());
            userIds.add(statusRsp.getFriendUserId());
        }
        if (null != getBizInvokeCallback()) {
            getBizInvokeCallback().privateUsersStatusChanged(JSONObject.toJSONString(model.getUsersStatus(userIds)));
            getBizInvokeCallback().privateGroupChanged(JSONObject.toJSONString(group));
        }
    }
}
