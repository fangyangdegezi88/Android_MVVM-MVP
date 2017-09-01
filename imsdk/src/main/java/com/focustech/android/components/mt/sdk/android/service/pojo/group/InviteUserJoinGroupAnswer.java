package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 被邀请人应答邀请加入群
 *
 * @author zhangxu
 */
public class InviteUserJoinGroupAnswer {
    private String groupId;         // 群ID
    private Messages.Enable result;          // 结果
    private String inviteUserId;    // 邀请人
    private String inviteUserName;  // 邀请人名称
    private long timestamp;       // 系统接收到消息请求的时间
    private String svrMsgId;        //系统消息唯一标示

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Messages.Enable getResult() {
        return result;
    }

    public void setResult(Messages.Enable result) {
        this.result = result;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getInviteUserName() {
        return inviteUserName;
    }

    public void setInviteUserName(String inviteUserName) {
        this.inviteUserName = inviteUserName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }
}
