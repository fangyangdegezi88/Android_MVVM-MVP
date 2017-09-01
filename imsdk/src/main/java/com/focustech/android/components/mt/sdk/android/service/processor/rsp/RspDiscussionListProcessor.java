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
public class RspDiscussionListProcessor extends AbstractMessageProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionsRsp rsp = Messages.DiscussionsRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final MTModel model = getSessionManager().getCurrent();
        final String userId = model.getCurrent().getUserId();

        model.getDiscussions().setUserId(userId);
        List<String> userIds = new ArrayList<>();

        for (Messages.DiscussionRsp discussionRsp : rsp.getDiscussionsList()) {
            final String discussionId = discussionRsp.getDiscussionId();

            if (MTRuntime.optionsRemoteMessagesDiscussions()) {
                // 加载聊天记录
                asyncExecute(MTRuntime.optionsRemoteMessagesDelay(), TimeUnit.SECONDS, new Runnable() {
                    @Override
                    public void run() {
                        FetchData data = new FetchData();
                        data.setType(Messages.RecentContactType.DISCUSSION);
                        data.setContactId(discussionId);
                        data.setFromTimestamp(getFromTimestamp(LastTimestampType.MESSAGE_DISCUSSION));
                        data.setToTimestamp(getToTimestamp(LastTimestampType.MESSAGE_DISCUSSION));

                        CMD.REQ_FETCH_MESSAGE.getProcessor().request(data);
                    }
                });
            }

            if (null != getBizInvokeCallback())
                model.getDiscussions().addOrUpdateGroup(new MTGroup(discussionRsp));

            userIds.clear();
            for (Messages.FriendStatusRsp statusRsp : discussionRsp.getFriendsList()) {
                model.reset(statusRsp.getFriendUserId(), statusRsp.getStatusList());
                userIds.add(statusRsp.getFriendUserId());
            }
            if (null != getBizInvokeCallback())
                getBizInvokeCallback().privateUsersStatusChanged(JSONObject.toJSONString(model.getUsersStatus(userIds)));
        }
        if (null != getBizInvokeCallback())
            getBizInvokeCallback().privateDiscussionsChanged(JSONObject.toJSONString(model.getDiscussions()));

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // 发送拉取变更的群用户信息
                for (String groupId : model.getDiscussions().getGroupIds()) {
                    CMD.REQ_DISCUSSION_USERS_INFO_LIST.getProcessor().request(groupId);
                }

                getGroupService().reset(userId, model.getDiscussions(), IGroupService.FEATURE_TEMP);
                // 变更最后更新时间
                updateLastTimestamp(userId, LastTimestampType.DATA_DISCUSSION_LIST);
            }
        });
    }
}
