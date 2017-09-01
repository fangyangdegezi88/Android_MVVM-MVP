package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxu on 2015/7/6.
 */
public class InviteJoinToGroupData {
    private String groupId;
    private List<String> userIds = new ArrayList<>();

    public InviteJoinToGroupData() {
    }

    public InviteJoinToGroupData(String groupId) {
        this.groupId = groupId;
    }

    public void addUserId(String userId) {
        userIds.add(userId);
    }

    public void removeUserId(String userId) {
        userIds.remove(userId);
    }

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
}
