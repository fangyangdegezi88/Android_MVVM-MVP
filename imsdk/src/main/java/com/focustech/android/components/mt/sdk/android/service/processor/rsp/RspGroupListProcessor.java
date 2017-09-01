package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.FetchData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 拉取响应
 *
 * @author zhangxu
 */
public class RspGroupListProcessor extends AbstractMessageProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.GroupsRsp rsp = Messages.GroupsRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final MTModel model = getSessionManager().getCurrent();
        final String userId = model.getCurrent().getUserId();

        model.getGroups().setUserId(userId);

        List<String> userIds = new ArrayList<>();

        for (Messages.GroupRsp groupRsp : rsp.getGroupsList()) {
            final String groupId = groupRsp.getGroupId();

            if (MTRuntime.optionsRemoteMessagesGroups()) {
                // 加载聊天记录
                asyncExecute(MTRuntime.optionsRemoteMessagesDelay(), TimeUnit.SECONDS, new Runnable() {
                    @Override
                    public void run() {
                        FetchData data = new FetchData();
                        data.setType(Messages.RecentContactType.GROUP);
                        data.setContactId(groupId);
                        data.setFromTimestamp(getFromTimestamp(LastTimestampType.MESSAGE_GROUP));
                        data.setToTimestamp(getToTimestamp(LastTimestampType.MESSAGE_GROUP));

                        CMD.REQ_FETCH_MESSAGE.getProcessor().request(data);
                    }
                });
            }

            model.getGroups().addOrUpdateGroup(new MTGroup(groupRsp));

            userIds.clear();
            for (Messages.FriendStatusRsp statusRsp : groupRsp.getFriendsList()) {
                model.reset(statusRsp.getFriendUserId(), statusRsp.getStatusList());
                userIds.add(statusRsp.getFriendUserId());
            }

            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateUsersStatusChanged(JSONObject.toJSONString(model.getUsersStatus(userIds)));
            }
        }

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateGroupsChanged(JSONObject.toJSONString(model.getGroups()));
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 发送拉取变更的群用户信息
                for (String groupId : model.getGroups().getGroupIds()) {
                    CMD.REQ_GROUP_USER_INFO_LIST.getProcessor().request(groupId);
                }

                getGroupService().reset(userId, model.getGroups(), IGroupService.FEATURE_FOREVER);
                // 变更最后更新时间
                updateLastTimestamp(userId, LastTimestampType.DATA_GROUP_LIST);
            }
        });
    }
}
