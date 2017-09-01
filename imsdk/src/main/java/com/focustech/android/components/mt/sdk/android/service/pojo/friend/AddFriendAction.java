package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

/**
 * 增加好友动作
 *
 * @author zhangxu
 */
public class AddFriendAction {
    /**
     * 目标好友ID
     */
    private String targetFriendUserId;
    /**
     * 加入到自己的好友分组ID
     */
    private String srcFriendGroupId;
    /**
     * 附带信息
     */
    private String ext = "";

    public String getTargetFriendUserId() {
        return targetFriendUserId;
    }

    public void setTargetFriendUserId(String targetFriendUserId) {
        this.targetFriendUserId = targetFriendUserId;
    }

    public String getSrcFriendGroupId() {
        return srcFriendGroupId;
    }

    public void setSrcFriendGroupId(String srcFriendGroupId) {
        this.srcFriendGroupId = srcFriendGroupId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
