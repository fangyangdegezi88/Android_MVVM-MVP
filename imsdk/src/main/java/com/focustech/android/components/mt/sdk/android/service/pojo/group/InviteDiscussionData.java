package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxu on 2015/7/8.
 */
public class InviteDiscussionData {
    private String discussionId;        // 讨论组Id
    private List<String> userIds = new ArrayList<>();               // 加入讨论组成员的ID集合

    public void addUser(String userId) {
        userIds.add(userId);
    }

    public void removeUser(String userId) {
        userIds.remove(userId);
    }

    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
