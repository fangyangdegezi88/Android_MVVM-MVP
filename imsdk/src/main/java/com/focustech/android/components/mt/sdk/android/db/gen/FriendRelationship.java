package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FRIEND_RELATIONSHIP.
 */
public class FriendRelationship {

    private Long id;
    /**
     * Not-null value.
     */
    private String userId;
    /** Not-null value. */
    private String friendGroupId;
    /** Not-null value. */
    private String friendUserId;

    public FriendRelationship() {
    }

    public FriendRelationship(Long id) {
        this.id = id;
    }

    public FriendRelationship(Long id, String userId, String friendGroupId, String friendUserId) {
        this.id = id;
        this.userId = userId;
        this.friendGroupId = friendGroupId;
        this.friendUserId = friendUserId;
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
    public String getFriendUserId() {
        return friendUserId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

}
