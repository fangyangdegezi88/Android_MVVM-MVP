package com.focustech.android.photo.myalbum.myalbum.bean;

import android.os.Environment;

/**
 * <*****>
 *
 * @author liuzaibing
 * @version [版本号, 2017/6/19]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface Path {
    String weiboPhoto = Environment.getExternalStorageDirectory() + "/sina/weibo/weibo/";

    String weixinPhoto = Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin/";
}
