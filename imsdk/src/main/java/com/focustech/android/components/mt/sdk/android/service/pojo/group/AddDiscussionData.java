package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxu on 2015/7/8.
 */
public class AddDiscussionData {
    private String discussionName;        // 讨论组名称
    private List<String> userIds = new ArrayList<>();               // 加入讨论组成员的ID集合

    public void addUser(String userId) {
        userIds.add(userId);
    }

    public void removeUser(String userId) {
        userIds.remove(userId);
    }

    public String getDiscussionName() {
        return discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
