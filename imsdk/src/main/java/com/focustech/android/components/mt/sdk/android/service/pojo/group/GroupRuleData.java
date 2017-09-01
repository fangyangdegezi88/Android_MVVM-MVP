package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 设置群规则
 *
 * @author zhangxu
 */
public class GroupRuleData {
    private String groupId;
    private Messages.ValidateRule rule;

    public GroupRuleData() {
    }

    public GroupRuleData(String groupId, Messages.ValidateRule rule) {
        this.groupId = groupId;
        this.rule = rule;
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
