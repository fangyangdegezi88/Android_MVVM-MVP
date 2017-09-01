package com.focusteach.android.update.upgrade;

import java.io.Serializable;

/**
 * @version : V1.0.0
 * @描述 :  checkUpgradeAdvise（升级建议）返回的Response
 * @user : zhangzeyu
 * @date : 2016/3/11
 */
public class CheckUpgradeAdviseResponse implements Serializable{

    private String policy;   // 下载策略
    private String download; // 下载地址

    @Override
    public String toString() {
        return "policy=" + policy + "\n"
              +"download=" + download + "\n";
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
