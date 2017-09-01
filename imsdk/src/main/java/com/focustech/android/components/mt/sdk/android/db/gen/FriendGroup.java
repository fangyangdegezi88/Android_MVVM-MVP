package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FRIEND_GROUP.
 */
public class FriendGroup {

    private Long id;
    /**
     * Not-null value.
     */
    private String userId;
    /** Not-null value. */
    private String friendGroupId;
    /** Not-null value. */
    private String friendGroupName;
    private long friendGroupType;

    public FriendGroup() {
    }

    public FriendGroup(Long id) {
        this.id = id;
    }

    public FriendGroup(Long id, String userId, String friendGroupId, String friendGroupName, long friendGroupType) {
        this.id = id;
        this.userId = userId;
        this.friendGroupId = friendGroupId;
        this.friendGroupName = friendGroupName;
        this.friendGroupType = friendGroupType;
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
    public String getFriendGroupId() {
        return friendGroupId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFriendGroupId(String friendGroupId) {
        this.friendGroupId = friendGroupId;
    }

    /** Not-null value. */
    public String getFriendGroupName() {
        return friendGroupName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public long getFriendGroupType() {
        return friendGroupType;
    }

    public void setFriendGroupType(long friendGroupType) {
        this.friendGroupType = friendGroupType;
    }

}
