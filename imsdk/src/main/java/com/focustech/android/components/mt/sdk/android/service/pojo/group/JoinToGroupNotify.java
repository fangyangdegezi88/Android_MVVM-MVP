package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;

/**
 * 用户申请加入群通知
 *
 * @author zhangxu
 */
public class JoinToGroupNotify extends AbstractSystemNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群名称
    private String userId;                // 申请人ID
    private String userName;              // 申请人名称
    private String validateMessage;       // 验证消息
    private long timestamp;             // 系统接收到消息请求的时间
    private String svrMsgId;              //系统消息唯一标示

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

    public String getValidateMessage() {
        return validateMessage;
    }

    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_JOIN_GROUP_VALIDATE;
    }
}
