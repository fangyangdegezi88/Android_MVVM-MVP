package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * Created by zhangxu on 2015/5/12.
 */
public class FetchData {
    private Messages.RecentContactType type;
    private String contactId;
    private long fromTimestamp;
    private long toTimestamp;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Messages.RecentContactType getType() {
        return type;
    }

    public void setType(Messages.RecentContactType type) {
        this.type = type;
    }

    public long getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public long getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(long toTimestamp) {
        this.toTimestamp = toTimestamp;
    }
}
