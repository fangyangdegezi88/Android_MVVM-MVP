package com.focustech.android.components.mt.sdk.android.service.pojo.conversation;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Conversation;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 会话数据
 *
 * @author zhangxu
 */
public class ConversationData {
    private static final ConversationSetting DEFAULT = new ConversationSetting();

    private Messages.RecentContactType contactType;
    private String contactId;
    private String contactName;
    private MessageData msg;
    private String notify;
    private ConversationSetting setting = DEFAULT;
    private CMD cmd;
    private long timestamp;

    public ConversationData() {
    }

    public ConversationData(Conversation conversation) {
        ReflectionUtil.copyProperties(conversation, this);
        setType(Messages.RecentContactType.valueOf((int) conversation.getContactType()));

        if (null != conversation.getSetting()) {
            setSetting(JSONObject.parseObject(conversation.getSetting(), ConversationSetting.class));
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Messages.RecentContactType getType() {
        return contactType;
    }

    public void setType(Messages.RecentContactType type) {
        this.contactType = type;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ConversationSetting getSetting() {
        return setting;
    }

    public void setSetting(ConversationSetting setting) {
        this.setting = setting;
    }

    public boolean isSystem() {
        return null == contactType;
    }

    public boolean isPerson() {
        return contactType == Messages.RecentContactType.PERSON;
    }

    public boolean isGroup() {
        return contactType == Messages.RecentContactType.GROUP;
    }

    public boolean isDiscussion() {
        return contactType == Messages.RecentContactType.DISCUSSION;
    }

    public MessageData getMsg() {
        return msg;
    }

    public void setMsg(MessageData msg) {
        this.msg = msg;
    }

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }
}
