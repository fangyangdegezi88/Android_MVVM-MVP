package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 增加好友成功通知
 *
 * @author zhangxu
 */
public class AddFriendSucceededToSrcNotify {
    private Messages.FriendAnswer answer;
    private String targetFriendUserId;           // 对方好友ID
    private String targetFriendUserName;         // 对方好友姓名

    public Messages.FriendAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(Messages.FriendAnswer answer) {
        this.answer = answer;
    }

    public String getTargetFriendUserId() {
        return targetFriendUserId;
    }

    public void setTargetFriendUserId(String targetFriendUserId) {
        this.targetFriendUserId = targetFriendUserId;
    }

    public String getTargetFriendUserName() {
        return targetFriendUserName;
    }

    public void setTargetFriendUserName(String targetFriendUserName) {
        this.targetFriendUserName = targetFriendUserName;
    }
}
