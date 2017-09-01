package com.focustech.android.components.mt.sdk.communicate.sysnty;

import java.io.Serializable;

/**
 * <透传模型>
 *
 * @author yanguozhu
 * @version [版本号, 2016/10/25]
 * @see [相关类/方法]
 * @since [V1]
 */
public class Transmission implements Serializable {

    private String noticeType;

    private String meta;

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
