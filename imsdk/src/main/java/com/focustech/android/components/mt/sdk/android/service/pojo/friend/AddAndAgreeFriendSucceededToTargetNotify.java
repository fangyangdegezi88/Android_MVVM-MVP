package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

/**
 * Created by zhangxu on 2015/7/7.
 */
public class AddAndAgreeFriendSucceededToTargetNotify {
    private String srcFriendUserId;        // 对方好友ID
    private String srcFriendUserName;      // 对方好友姓名
    private long timestamp;       //系统接收到消息请求的时间

    public String getSrcFriendUserId() {
        return srcFriendUserId;
    }

    public void setSrcFriendUserId(String srcFriendUserId) {
        this.srcFriendUserId = srcFriendUserId;
    }

    public String getSrcFriendUserName() {
        return srcFriendUserName;
    }

    public void setSrcFriendUserName(String srcFriendUserName) {
        this.srcFriendUserName = srcFriendUserName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
