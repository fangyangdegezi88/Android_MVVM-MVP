package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * @author zhangxu
 */
public class GetConversationMessageListData {
    private Messages.RecentContactType contactType;
    private String contactId;
    private long earliestTimestamp;
    private int count;

    public GetConversationMessageListData() {

    }

    public long getEarliestTimestamp() {
        return earliestTimestamp;
    }

    public void setEarliestTimestamp(long earliestTimestamp) {
        this.earliestTimestamp = earliestTimestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Messages.RecentContactType getContactType() {
        return contactType;
    }

    public void setContactType(Messages.RecentContactType contactType) {
        this.contactType = contactType;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public long getLastTimestampType() {
        return LastTimestampType.MESSAGE_MAPPING.get(getContactType());
    }
}
