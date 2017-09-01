package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 申请群已经处理过了
 *
 * @author zhangxu
 */
public class JoinGroupProcessedNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群名称
    private String userId;                // 申请人ID
    private String userName;              // 申请人名称
    private String operateUserId;         // 管理员ID
    private String operateUserName;       // 管理员名称
    private Messages.Enable result;                // 操作结果
    private long timestamp;             // 系统接收到消息请求的时间

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public Messages.Enable getResult() {
        return result;
    }

    public void setResult(Messages.Enable result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
