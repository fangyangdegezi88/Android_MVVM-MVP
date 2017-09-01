package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

/**
 * Created by zhangxu on 2015/5/27.
 */
public class AddFriendFailNotify {
    private String targetFriendUserId;           // 目标用户ID
    private String targetFriendUserName;         // 目标好友姓名
    private String ext;                          // 额外的拒绝信息

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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
