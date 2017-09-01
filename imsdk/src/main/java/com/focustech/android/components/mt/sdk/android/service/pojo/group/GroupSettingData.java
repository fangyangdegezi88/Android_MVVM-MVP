package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * Created by zhangxu on 2015/7/6.
 */
public class GroupSettingData {
    private String groupId;
    private String setting;
    private Messages.MessageSetting messageSetting;

    public GroupSettingData() {
    }

    public GroupSettingData(String groupId, String setting, Messages.MessageSetting messageSetting) {
        this.groupId = groupId;
        this.setting = setting;
        this.messageSetting = messageSetting;
    }

    public GroupSettingData(String groupId, Messages.MessageSetting messageSetting) {
        this.groupId = groupId;
        this.messageSetting = messageSetting;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Messages.MessageSetting getMessageSetting() {
        return messageSetting;
    }

    public void setMessageSetting(Messages.MessageSetting messageSetting) {
        this.messageSetting = messageSetting;
    }
}
