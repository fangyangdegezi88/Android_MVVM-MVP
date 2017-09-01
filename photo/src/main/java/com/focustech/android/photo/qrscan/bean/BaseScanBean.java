package com.focustech.android.photo.qrscan.bean;

import java.io.Serializable;

/**
 *扫码对象基类，meta为扩展对象
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/3/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public class BaseScanBean implements Serializable {
    /**
     * ScanType枚举
     * 扫码业务类型
     * */
    private int scanType;
    /**
     * 扫码扩展信息
     * */
    private String meta;

    public BaseScanBean(){}

    public BaseScanBean(int scanType, String meta) {
        this.scanType = scanType;
        this.meta = meta;
    }

    public int getScanType() {
        return scanType;
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
