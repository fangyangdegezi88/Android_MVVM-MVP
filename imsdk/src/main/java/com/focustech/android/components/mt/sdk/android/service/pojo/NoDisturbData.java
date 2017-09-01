package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 免打扰设置
 *
 * @author zhangxu
 */
public class NoDisturbData {
    private String userId;
    private Messages.Enable noDisturb;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Messages.Enable getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(Messages.Enable noDisturb) {
        this.noDisturb = noDisturb;
    }
}
