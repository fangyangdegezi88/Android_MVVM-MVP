package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import java.util.List;

/**
 * @author zhangxu
 */
public class DeleteGroupUserData {
    private String groupId;
    private List<String> groupUserIds;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getGroupUserIds() {
        return groupUserIds;
    }

    public void setGroupUserIds(List<String> groupUserIds) {
        this.groupUserIds = groupUserIds;
    }
}
