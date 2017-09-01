package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群规则
 *
 * @author zhangxu
 */
public class GroupUserRuleData {
    private String userId;
    private String groupId;
    private Messages.ValidateRule rule;

    public GroupUserRuleData() {
    }

    public GroupUserRuleData(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public GroupUserRuleData(String userId, String groupId, Messages.ValidateRule rule) {
        this.userId = userId;
        this.groupId = groupId;
        this.rule = rule;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Messages.ValidateRule getRule() {
        return rule;
    }

    public void setRule(Messages.ValidateRule rule) {
        this.rule = rule;
    }
}
