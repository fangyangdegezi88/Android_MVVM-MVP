package com.focustech.android.components.mt.sdk.android.service.pojo.group;

/**
 * 申请加入群
 *
 * @author zhangxu
 */
public class JoinGroupData {
    private String groupId;               // 群ID
    private long timestamp;             // 系统接收到消息请求的时间
    private String validateMessage;       // 验证消息

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValidateMessage() {
        return validateMessage;
    }

    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage;
    }
}
