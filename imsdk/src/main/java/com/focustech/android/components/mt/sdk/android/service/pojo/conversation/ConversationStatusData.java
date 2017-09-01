package com.focustech.android.components.mt.sdk.android.service.pojo.conversation;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 会话窗口
 *
 * @author zhangxu
 */
public class ConversationStatusData {
    private Messages.RecentContactType type;
    private String contactId;
    private boolean active = false;

    public Messages.RecentContactType getType() {
        return type;
    }

    public void setType(Messages.RecentContactType type) {
        this.type = type;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
