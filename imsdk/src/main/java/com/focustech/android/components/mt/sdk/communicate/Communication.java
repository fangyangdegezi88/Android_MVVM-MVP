package com.focustech.android.components.mt.sdk.communicate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <通讯类>
 *
 * @author yanguozhu
 * @version [版本号, 2016/10/24]
 * @see [相关类/方法]
 * @since [V1]
 */

public class Communication implements Parcelable {
    /**
     * 消息类型，如：通知类型，作业类型，文字类型...
     */
    private CommunicationType type;
    /**
     * 通知消息体
     */
    private CommunicationContent content;

    private String userId;

    private String processName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeSerializable(this.content);
        dest.writeString(this.userId);
        dest.writeString(this.processName);
    }

    public void readFromParcel(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CommunicationType.values()[tmpType];
        this.content = (CommunicationContent) in.readSerializable();
        this.userId = in.readString();
        this.processName = in.readString();
    }

    public Communication() {
    }

    public Communication(CommunicationType type, CommunicationContent content) {
        this.type = type;
        this.content = content;
    }

    protected Communication(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CommunicationType.values()[tmpType];
        this.content = (CommunicationContent) in.readSerializable();
        this.userId = in.readString();
        this.processName = in.readString();
    }

    public static final Parcelable.Creator<Communication> CREATOR = new Parcelable.Creator<Communication>() {
        @Override
        public Communication createFromParcel(Parcel source) {
            return new Communication(source);
        }

        @Override
        public Communication[] newArray(int size) {
            return new Communication[size];
        }
    };

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

    public CommunicationContent getContent() {
        return content;
    }

    public void setContent(CommunicationContent content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
