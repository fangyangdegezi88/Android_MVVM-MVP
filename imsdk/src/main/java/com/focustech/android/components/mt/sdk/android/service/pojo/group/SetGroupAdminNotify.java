package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 被删除出群
 *
 * @author zhangxu
 */
public class SetGroupAdminNotify extends AbstractSystemNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群名称
    private List<String> userIds;               // 用户ID
    private Messages.UserType userType;                // 启用还是禁用
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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Messages.UserType getUserType() {
        return userType;
    }

    public void setUserType(Messages.UserType userType) {
        this.userType = userType;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_SET_GROUP_ADMIN;
    }
}
