package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;

import java.util.List;

/**
 * Created by zhangxu on 2015/7/6.
 */
public class DisagreeInviteUserJoinGroupNotify extends AbstractSystemNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群名称
    private List<String> invitedUserIds;        // 被邀请人IDS
    private List<String> invitedUserNames;      // 被邀请人名称
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

    public List<String> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(List<String> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    public List<String> getInvitedUserNames() {
        return invitedUserNames;
    }

    public void setInvitedUserNames(List<String> invitedUserNames) {
        this.invitedUserNames = invitedUserNames;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_AGREE_INVITE_USER_JOIN_GROUP_SUCCEEDED;
    }
}
