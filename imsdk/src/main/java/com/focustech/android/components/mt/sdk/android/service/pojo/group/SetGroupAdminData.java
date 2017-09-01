package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * Created by zhangxu on 2015/7/7.
 */
public class SetGroupAdminData {
    private String groupId;               // 群ID
    private List<String> userIds;               // 用户ID
    private Messages.Enable enable;                // 启用还是禁用

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Messages.Enable getEnable() {
        return enable;
    }

    public void setEnable(Messages.Enable enable) {
        this.enable = enable;
    }
}
