package com.focustech.android.components.mt.sdk.android.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MESSAGE.
 */
public class Message {

    private Long id;
    private String userId;
    private String localMsgId;
    private String svrMsgId;
    /**
     * Not-null value.
     */
    private String fromUserId;
    /** Not-null value. */
    private String fromUserName;
    /** Not-null value. */
    private String contactId;
    private long contactType;
    /** Not-null value. */
    private String msg;
    private long msgType;
    private String msgMeta;
    private String fileIds;
    private long sendStatus;
    private Long status;
    private long timestamp;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, String userId, String localMsgId, String svrMsgId, String fromUserId, String fromUserName, String contactId, long contactType, String msg, long msgType, String msgMeta, String fileIds, long sendStatus, Long status, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.localMsgId = localMsgId;
        this.svrMsgId = svrMsgId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.contactId = contactId;
        this.contactType = contactType;
        this.msg = msg;
        this.msgType = msgType;
        this.msgMeta = msgMeta;
        this.fileIds = fileIds;
        this.sendStatus = sendStatus;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocalMsgId() {
        return localMsgId;
    }

    public void setLocalMsgId(String localMsgId) {
        this.localMsgId = localMsgId;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }

    /** Not-null value. */
    public String getFromUserId() {
        return fromUserId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    /** Not-null value. */
    public String getFromUserName() {
        return fromUserName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    /** Not-null value. */
    public String getContactId() {
        return contactId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public long getContactType() {
        return contactType;
    }

    public void setContactType(long contactType) {
        this.contactType = contactType;
    }

    /** Not-null value. */
    public String getMsg() {
        return msg;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getMsgType() {
        return msgType;
    }

    public void setMsgType(long msgType) {
        this.msgType = msgType;
    }

    public String getMsgMeta() {
        return msgMeta;
    }

    public void setMsgMeta(String msgMeta) {
        this.msgMeta = msgMeta;
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public long getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(long sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
