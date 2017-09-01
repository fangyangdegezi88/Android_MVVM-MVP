package com.focustech.android.components.mt.sdk.android.service.processor.local;

import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

import java.util.concurrent.TimeUnit;

/**
 * 登陆成功后的本地操作
 */
public class LocalAfterLoginSuccessfulProcessor extends AbstractMessageProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        // 加载最后聊天时间戳
        loadLastChatTimeFromDB();

        // 拉取本地好友分组
        if (MTRuntime.optionsLocalFriends()) {
            CMD.LOCAL_GET_FRIEND_GROUPS.getProcessor().request(null);
        }

        // 拉取本地群组
        if (MTRuntime.optionsLocalGroups()) {
            CMD.LOCAL_GET_GROUPS.getProcessor().request(null);
        }

        // 拉取本地讨论组
        if (MTRuntime.optionsLocalDiscussions()) {
            CMD.LOCAL_GET_DISCUSSIONS.getProcessor().request(null);
        }

        // 拉取本地会话列表
        if (MTRuntime.optionsLocalConversations()) {
            CMD.LOCAL_GET_CONVERSATION_LIST.getProcessor().request(null);
        }

        // 拉取设置
        if (MTRuntime.optionsRemoteSettings()) {
            CMD.REQ_GET_USER_SETTING.getProcessor().request(null);
        }

        if (MTRuntime.optionsRemoteFriends()) {
            // 拉取好友分组列表
            CMD.REQ_FRIEND_GROUPS.getProcessor().request(null);
            // 拉取好友详情
            CMD.REQ_FRIENDS.getProcessor().request(null);
        }

        if (MTRuntime.optionsRemoteGroups()) {
            // 拉取群
            CMD.REQ_GROUP_LIST.getProcessor().request(null);
        }

        if (MTRuntime.optionsRemoteDiscussions()) {
            // 拉取讨论组
            CMD.REQ_DISCUSSION_LIST.getProcessor().request(null);
        }

        if (MTRuntime.optionsRemoteSys()) {
            // 拉取系统消息
            CMD.REQ_GET_SYS_NTY.getProcessor().request(null);
        }

        if (MTRuntime.optionsRemoteMessages()) {
            asyncExecute(MTRuntime.optionsRemoteMessagesDelay(), TimeUnit.SECONDS, new Runnable() {
                @Override
                public void run() {
                    CMD.REQ_FETCH_MESSAGE.getProcessor().request(null);
                }
            });
        }

        return null;
    }
}
