package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除好友分组响应
 *
 * @author zhangxu
 */
public class RspDeleteFriendGroupProcessor extends AbstractUserProcessor<Void, Void, String> {
    private Logger logger = LoggerFactory.getLogger(RspDeleteFriendGroupProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.DeleteFriendGroupRsp rsp = Messages.DeleteFriendGroupRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        FriendGroups friendGroups = getSessionManager().getCurrent().getFriendGroups();

        final String userId = getSessionManager().getUserId();
        final String friendGroupId = rsp.getFriendGroupId();
        final String defaultFriendGroupId = friendGroups.getDefaultFriendGroup().getFriendGroupId();

        friendGroups.deleteFriendGroup(friendGroupId);

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getFriendGroupService().delete(userId, friendGroupId, defaultFriendGroupId);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateFriendGroupsChanged(JSONObject.toJSONString(friendGroups));
        }
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateDeleteFriendGroupSuccessful(friendGroupId);
        }
    }
}
