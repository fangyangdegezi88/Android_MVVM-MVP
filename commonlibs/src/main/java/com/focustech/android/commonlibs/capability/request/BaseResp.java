package com.focustech.android.commonlibs.capability.request;


import java.io.Serializable;

/**
 * <网络请求基础返回体>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class BaseResp implements Serializable {

    private static final long serialVersionUID = -2223844927849928409L;

    /**
     * 返回状态码
     */
    protected String code;
    private Object value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


}
