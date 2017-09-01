package com.focustech.android.components.mt.sdk.support.cache;

import android.content.Context;

import com.focustech.android.commonlibs.capability.cache.BaseSharedPreference;


/**
 * <用户信息缓存>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SharedPrefLoginInfo extends BaseSharedPreference {
    /**
     * 登录信息
     */
    public static final String LOGIN_INFO_FILE = "login_info_file";

    /**
     * 登录态
     */
    public static final String LOGIN_INFO = "loginInfo";

    /**
     * 配置文件
     */
    public static final String conf_File = "confFile";


    public SharedPrefLoginInfo(Context context, String fileName) {
        super(context, fileName);
    }
}
