package com.focustech.android.commonlibs.bean;

import java.io.Serializable;

/**
 * <登录信息 历史登录账号>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/6/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class HistoryLoginInfo implements Serializable {

    private static final long serialVersionUID = 5328944772833800916L;

    public HistoryLoginInfo() {

    }

    public HistoryLoginInfo(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String username;
    private String userId;

}
