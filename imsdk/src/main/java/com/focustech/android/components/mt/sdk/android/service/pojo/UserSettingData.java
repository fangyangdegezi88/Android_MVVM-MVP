package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 用设置data
 *
 * @author zhangxu
 */
public class UserSettingData {
    private Messages.Enable allowStrangerChatToMe;
    private Messages.ValidateRule friendRule;
    private Messages.Enable allowChatRecordOnServer;
    private Messages.ValidateRule groupRule;
    private CustomerSettingData customerSettings;

    public Messages.Enable getAllowStrangerChatToMe() {
        return allowStrangerChatToMe;
    }

    public void setAllowStrangerChatToMe(Messages.Enable allowStrangerChatToMe) {
        this.allowStrangerChatToMe = allowStrangerChatToMe;
    }

    public Messages.ValidateRule getFriendRule() {
        return friendRule;
    }

    public void setFriendRule(Messages.ValidateRule friendRule) {
        this.friendRule = friendRule;
    }

    public Messages.Enable getAllowChatRecordOnServer() {
        return allowChatRecordOnServer;
    }

    public void setAllowChatRecordOnServer(Messages.Enable allowChatRecordOnServer) {
        this.allowChatRecordOnServer = allowChatRecordOnServer;
    }

    public Messages.ValidateRule getGroupRule() {
        return groupRule;
    }

    public void setGroupRule(Messages.ValidateRule groupRule) {
        this.groupRule = groupRule;
    }

    public CustomerSettingData getCustomerSettings() {
        return customerSettings;
    }

    public void setCustomerSettings(CustomerSettingData customerSettings) {
        this.customerSettings = customerSettings;
    }

    public static class CustomerSettingData {

    }
}
