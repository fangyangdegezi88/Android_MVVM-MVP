package com.focustech.android.components.mt.sdk.android.service.pojo.conversation;

import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 会话设置
 *
 * @author zhangxu
 */
public class ConversationSetting {
    @JSONField(deserialize = false, serialize = false)
    private Messages.RecentContactType type;
    @JSONField(deserialize = false, serialize = false)
    private String contactId;
    @JSONField(name = "t")
    private boolean top = false;

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

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
}
