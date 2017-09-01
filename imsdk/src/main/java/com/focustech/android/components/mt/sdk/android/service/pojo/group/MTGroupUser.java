package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 群用户
 *
 * @author zhangxu
 */
public class MTGroupUser {
    private String groupUserId;
    private String userName;
    private String groupId;
    private Messages.UserType userType;
    private String groupNickName;         // 用户群昵称
    private long lastChatTimestamp;     // 最后聊天时间
    private Messages.Enable nickNameSetting;       // 用户是否允许管理员修改群昵称设置
    private Messages.HeadType userHeadType;        // 用户头像类型
    private String userHeadId;            // 用户头像ID

    public MTGroupUser() {
    }

    public MTGroupUser(String groupUserId, String groupId) {
        this.groupUserId = groupUserId;
        this.groupId = groupId;
    }

    public MTGroupUser(Messages.GroupUserInfoRsp rsp) {
        ReflectionUtil.copyProperties(rsp, this);
        setGroupUserId(rsp.getUserId());
    }

    public MTGroupUser(Messages.DiscussionUserInfoRsp rsp) {
        ReflectionUtil.copyProperties(rsp, this);
        setGroupUserId(rsp.getUserId());
        setGroupId(rsp.getDiscussionId());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Messages.UserType getUserType() {
        return userType;
    }

    public void setUserType(Messages.UserType userType) {
        this.userType = userType;
    }

    public String getGroupNickName() {
        return groupNickName;
    }

    public void setGroupNickName(String groupNickName) {
        this.groupNickName = groupNickName;
    }

    public long getLastChatTimestamp() {
        return lastChatTimestamp;
    }

    public void setLastChatTimestamp(long lastChatTimestamp) {
        this.lastChatTimestamp = lastChatTimestamp;
    }

    public Messages.Enable getNickNameSetting() {
        return nickNameSetting;
    }

    public void setNickNameSetting(Messages.Enable nickNameSetting) {
        this.nickNameSetting = nickNameSetting;
    }

    public Messages.HeadType getUserHeadType() {
        return userHeadType;
    }

    public void setUserHeadType(Messages.HeadType userHeadType) {
        this.userHeadType = userHeadType;
    }

    public String getUserHeadId() {
        return userHeadId;
    }

    public void setUserHeadId(String userHeadId) {
        this.userHeadId = userHeadId;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
