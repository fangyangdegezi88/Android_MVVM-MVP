package com.focustech.android.components.mt.sdk.communicate;

import java.io.Serializable;

/**
 * <消息类中用到的>
 *
 * @author yanguozhu
 * @version [版本号, 2016/10/24]
 * @see [相关类/方法]
 * @since [V1]
 */

public class CommunicationContent<MSG_BEAN> implements Serializable {
    private MSG_BEAN message;

    private long ntpTime;

    public CommunicationContent(MSG_BEAN message, long ntpTime) {
        this.message = message;
        this.ntpTime = ntpTime;
    }

    public MSG_BEAN getMessage() {
        return message;
    }

    public void setMessage(MSG_BEAN message) {
        this.message = message;
    }

    public long getNtpTime() {
        return ntpTime;
    }

    public void setNtpTime(long ntpTime) {
        this.ntpTime = ntpTime;
    }
}
