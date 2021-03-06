package com.focustech.android.commonlibs.bean;

import java.util.Map;

/**
 * 用户信息扩展
 *
 * @author zhangxu
 */
public interface IUserExt {
    void create(Map<String, String> values);

    Map<String, String> getEntry();

    String[] getNames();
}
