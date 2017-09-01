package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 用户头像数据
 *
 * @author zhangxu
 */
public class UserHeadData {
    private Messages.HeadType headType;
    private String fileName;
    private String userHeadId;

    public String getUserHeadId() {
        return userHeadId;
    }

    public void setUserHeadId(String userHeadId) {
        this.userHeadId = userHeadId;
    }

    public Messages.HeadType getHeadType() {
        return headType;
    }

    public void setHeadType(Messages.HeadType headType) {
        this.headType = headType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
