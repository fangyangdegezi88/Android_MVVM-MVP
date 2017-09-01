package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 群信息
 *
 * @author zhangxu
 */
public class MTGroups {
    private String userId;
    private Map<String, MTGroup> groups = new HashMap<>();

    public void addOrUpdateGroup(MTGroup group) {
        if (groups.containsKey(group.getGroupId())) {
            // 更新属性
            ReflectionUtil.copyProperties(group, groups.get(group.getGroupId()));
        } else {
            groups.put(group.getGroupId(), group);
        }
    }

    public void clear() {
        groups.clear();
    }

    @JSONField(serialize = false, deserialize = false)
    public MTGroup getGroup(String groupId) {
        return groups.get(groupId);
    }

    @JSONField(serialize = false, deserialize = false)
    public Set<String> getGroupIds() {
        return groups.keySet();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, MTGroup> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, MTGroup> groups) {
        this.groups = groups;
    }
}
