package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * @author zhangxu
 */
public class GetExtMessageListData {
    private Messages.MessageType type;
    private long before = Long.MAX_VALUE;
    private int count;

    public GetExtMessageListData() {

    }

    public Messages.MessageType getType() {
        return type;
    }

    public void setType(Messages.MessageType type) {
        this.type = type;
    }

    public long getBefore() {
        return before;
    }

    public void setBefore(long before) {
        this.before = before;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
