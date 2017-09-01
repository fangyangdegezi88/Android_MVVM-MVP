package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.android.components.mt.sdk.android.db.gen.Message;

/**
 * @author zhangxu
 */
public class MessageData extends AbstractMessageData {
    private String fromSvrMsgId;

    public MessageData() {

    }

    public MessageData(Message message, boolean parse) {
        super(message, parse);
    }

    public String getFromSvrMsgId() {
        return fromSvrMsgId;
    }

    public void setFromSvrMsgId(String fromSvrMsgId) {
        this.fromSvrMsgId = fromSvrMsgId;
    }
}
