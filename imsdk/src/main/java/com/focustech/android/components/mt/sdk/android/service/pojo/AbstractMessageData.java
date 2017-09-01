package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息
 *
 * @author zhangxu
 */
public abstract class AbstractMessageData implements Serializable{
    protected static final String TAG_PIC = MTRuntime.getPicTag();
    protected static final String PARSER;

    static {
        String[] faceTags = MTRuntime.getFaceTags();
        StringBuilder value = new StringBuilder();
        value.append("(");
        value.append(TAG_PIC);

        for (String faceTag : faceTags) {
            value.append("|");
            value.append(faceTag);
        }

        value.append(")");

        String base = value.toString();

        PARSER = "(?<=" + base + ")|(?=" + base + ")";
    }

    private String localMessageId = IDGenerator.getLocalMessageId();
    private MessageMeta msgMeta;
    private Messages.MessageType msgType;
    private String fromUserId;
    private String fromUserName;
    private String contactId;
    private String contactName;
    private long contactType;
    private long timestamp;
    private String svrMsgId;
    private Messages.Enable sync;
    private Messages.Enable resend;
    private boolean prompt;
    private boolean read;
    private String msg;
    private Status sendStatus = Status.SEND_SUCCESSFUL;
    private List<Part> parts = new ArrayList<>();

    public AbstractMessageData() {
    }

    public AbstractMessageData(Message message, boolean parse) {
        ReflectionUtil.copyProperties(message, this);
        setMsgType(Messages.MessageType.valueOf((int) message.getMsgType()));
        setMsgMeta(JSONObject.parseObject(message.getMsgMeta(), MessageMeta.class));
        setSendStatus(Status.valueOf(message.getSendStatus()));

        if (parse) {
            parse();
        }
    }

    public String getContactName() {
        return null == contactName ? "" : contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public MessageMeta getMsgMeta() {
        return msgMeta;
    }

    public void setMsgMeta(MessageMeta msgMeta) {
        this.msgMeta = msgMeta;
    }

    public Messages.MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(Messages.MessageType msgType) {
        this.msgType = msgType;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }

    public Messages.Enable getSync() {
        return sync;
    }

    public void setSync(Messages.Enable sync) {
        this.sync = sync;
    }

    public Messages.Enable getResend() {
        return resend;
    }

    public void setResend(Messages.Enable resend) {
        this.resend = resend;
    }

    public String getLocalMessageId() {
        return localMessageId;
    }

    public void setLocalMessageId(String localMessageId) {
        this.localMessageId = localMessageId;
    }

    public boolean isPrompt() {
        return prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isMedia() {
        return null != getMsgMeta() && null != getMsgMeta().getCustomMeta() && null != getMsgMeta().getCustomMeta().getMultiMedias() && getMsgMeta().getCustomMeta().getMultiMedias().size() > 0;
    }

    public Status getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Status sendStatus) {
        this.sendStatus = sendStatus;
    }

    @JSONField(serialize = false, deserialize = false)
    public String[] getFileIds() {
        String[] value = ArrayUtils.EMPTY_STRING_ARRAY;
        int index = 0;

        if (isMedia()) {
            value = new String[getMsgMeta().getCustomMeta().getMultiMedias().size()];

            for (MessageMeta.MultiMediaDescriptor md : getMsgMeta().getCustomMeta().getMultiMedias()) {
                value[index++] = md.getFileId();
            }
        }

        return value;
    }

    public void addMultiMedia(Integer type, String file) {
        addMultiMedia(type, file, null, null);
    }

    public void addMultiMedia(Integer type, String file, Integer second) {
        addMultiMedia(type, file, second, null);
    }

    public void addMultiMedia(Integer type, String file, Integer second, Integer extend) {
        if (null == msgMeta.getCustomMeta().getMultiMedias()) {
            msgMeta.getCustomMeta().setMultiMedias(new ArrayList<MessageMeta.MultiMediaDescriptor>());
        }

        msgMeta.getCustomMeta().getMultiMedias().add(new MessageMeta.MultiMediaDescriptor(type, file, second, extend));
    }

    public void uploadComplete(long taskId, String fileId) {
        if (isMedia()) {
            for (MessageMeta.MultiMediaDescriptor md : msgMeta.getCustomMeta().getMultiMedias()) {
                if (md.getUploadTaskId() == taskId) {
                    md.setFileId(fileId);
                    return;
                }
            }
        }
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public void parse() {
        String[] fileIds = getFileIds();
        int index = 0;

        if (fileIds.length == 0)
            return;

        for (String info : msg.split(PARSER)) {
            if (info.length() == 0) {
                continue;
            }

            // 图片
            if (info.equals(TAG_PIC)) {
                parts.add(new Part().setFileId(fileIds[index++]));
            } else if (isFaceTag(info)) {
                parts.add(new Part().setFaceTag(info));
            } else {
                parts.add(new Part().setContent(info));
            }
        }
    }

    public long getContactType() {
        return contactType;
    }

    public void setContactType(long contactType) {
        this.contactType = contactType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMessageData that = (AbstractMessageData) o;

        if (localMessageId != null ? !localMessageId.equals(that.localMessageId) : that.localMessageId != null)
            return false;

        return !(svrMsgId != null ? !svrMsgId.equals(that.svrMsgId) : that.svrMsgId != null);
    }

    @Override
    public int hashCode() {
        int result = localMessageId != null ? localMessageId.hashCode() : 0;
        result = 31 * result + (svrMsgId != null ? svrMsgId.hashCode() : 0);
        return result;
    }

    private static boolean isFaceTag(String tag) {
        return ArrayUtils.contains(MTRuntime.getFaceTags(), tag);
    }

    public static class Part implements Serializable{
        private String faceTag;
        private String content;
        private String fileId;

        public boolean isText() {
            return null != content;
        }

        public boolean isPic() {
            return null != fileId;
        }

        public boolean isFace() {
            return null != faceTag;
        }

        public String getContent() {
            return content;
        }

        public Part setContent(String content) {
            this.content = content;
            return this;
        }

        public String getFileId() {
            return fileId;
        }

        public Part setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public String getFaceTag() {
            return faceTag;
        }

        public Part setFaceTag(String faceTag) {
            this.faceTag = faceTag;
            return this;
        }
    }

    public enum Status {
        PRE_SEND(0), SEND_SUCCESSFUL(1), SEND_FAIL(2);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status valueOf(int value) {
            Status status = PRE_SEND;

            for (Status s : values()) {
                if (s.value == value) {
                    status = s;
                    break;
                }
            }

            return status;
        }

        public static Status valueOf(long value) {
            return valueOf(Long.valueOf(value).intValue());
        }
    }
}
