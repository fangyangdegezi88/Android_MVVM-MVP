package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SYSTEM_NOTIFY.
 */
public class SystemNotify {

    private Long id;
    /**
     * Not-null value.
     */
    private String userId;
    /** Not-null value. */
    private String cmd;
    private Long contactType;
    private String contactId;
    private String relatedId;
    private Boolean processed;
    /** Not-null value. */
    private byte[] data;
    private long addTime;
    private Long updateTime;

    public SystemNotify() {
    }

    public SystemNotify(Long id) {
        this.id = id;
    }

    public SystemNotify(Long id, String userId, String cmd, Long contactType, String contactId, String relatedId, Boolean processed, byte[] data, long addTime, Long updateTime) {
        this.id = id;
        this.userId = userId;
        this.cmd = cmd;
        this.contactType = contactType;
        this.contactId = contactId;
        this.relatedId = relatedId;
        this.processed = processed;
        this.data = data;
        this.addTime = addTime;
        this.updateTime = updateTime;
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
    public String getCmd() {
        return cmd;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Long getContactType() {
        return contactType;
    }

    public void setContactType(Long contactType) {
        this.contactType = contactType;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    /** Not-null value. */
    public byte[] getData() {
        return data;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setData(byte[] data) {
        this.data = data;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}
