package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;

/**
 * 被邀请加入群的通知
 *
 * @author zhangxu
 */
public class InviteJoinToGroupNotify extends AbstractSystemNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群名称
    private String inviteUserId;          // 邀请人
    private String inviteUserName;        // 邀请人名称
    private long timestamp;             // 系统接收到消息请求的时间
    private String svrMsgId;              // 系统消息唯一标示

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

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_INVITE_USER_JOIN_GROUP;
    }
}
