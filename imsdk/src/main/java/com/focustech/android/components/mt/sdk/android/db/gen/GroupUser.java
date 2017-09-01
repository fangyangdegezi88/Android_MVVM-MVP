package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GROUP_USER.
 */
public class GroupUser {

    private Long id;
    /**
     * Not-null value.
     */
    private String userId;
    /** Not-null value. */
    private String groupId;
    /** Not-null value. */
    private String groupUserId;
    private Long userType;
    private Long groupNickName;
    private Long lastChatTimestamp;
    private Boolean nickNameSetting;
    private Long userHeadType;
    private String userHeadId;
    private Long feature;

    public GroupUser() {
    }

    public GroupUser(Long id) {
        this.id = id;
    }

    public GroupUser(Long id, String userId, String groupId, String groupUserId, Long userType, Long groupNickName, Long lastChatTimestamp, Boolean nickNameSetting, Long userHeadType, String userHeadId, Long feature) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.groupUserId = groupUserId;
        this.userType = userType;
        this.groupNickName = groupNickName;
        this.lastChatTimestamp = lastChatTimestamp;
        this.nickNameSetting = nickNameSetting;
        this.userHeadType = userHeadType;
        this.userHeadId = userHeadId;
        this.feature = feature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getUserId() {
        return userId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Not-null value. */
    public String getGroupId() {
        return groupId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** Not-null value. */
    public String getGroupUserId() {
        return groupUserId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getGroupNickName() {
        return groupNickName;
    }

    public void setGroupNickName(Long groupNickName) {
        this.groupNickName = groupNickName;
    }

    public Long getLastChatTimestamp() {
        return lastChatTimestamp;
    }

    public void setLastChatTimestamp(Long lastChatTimestamp) {
        this.lastChatTimestamp = lastChatTimestamp;
    }

    public Boolean getNickNameSetting() {
        return nickNameSetting;
    }

    public void setNickNameSetting(Boolean nickNameSetting) {
        this.nickNameSetting = nickNameSetting;
    }

    public Long getUserHeadType() {
        return userHeadType;
    }

    public void setUserHeadType(Long userHeadType) {
        this.userHeadType = userHeadType;
    }

    public String getUserHeadId() {
        return userHeadId;
    }

    public void setUserHeadId(String userHeadId) {
        this.userHeadId = userHeadId;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

}