package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RECENT_CONTACT.
 */
public class RecentContact {

    private Long id;
    /**
     * Not-null value.
     */
    private String userId;
    private long contactType;
    /** Not-null value. */
    private String contactId;
    private long timestamp;

    public RecentContact() {
    }

    public RecentContact(Long id) {
        this.id = id;
    }

    public RecentContact(Long id, String userId, long contactType, String contactId, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.contactType = contactType;
        this.contactId = contactId;
        this.timestamp = timestamp;
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

    public long getContactType() {
        return contactType;
    }

    public void setContactType(long contactType) {
        this.contactType = contactType;
    }

    /** Not-null value. */
    public String getContactId() {
        return contactId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
