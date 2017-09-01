package com.focustech.android.commonlibs.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.capability.request.OkHttpClientHelper;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * <判断当前网络状态>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/24]
 * @see [相关类/方法]
 * @since [V1]
 */
public class NetworkUtil {

    /**
     * 判断当前网络是否连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null == info) return false;
        return info.isConnected();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null == info) return false;
        return info.isConnected();
    }
    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null == info) return false;
        if (info.isConnected())
            return info.getType() == ConnectivityManager.TYPE_WIFI;
        return false;
    }

    /**
     * 判断手机数据是否连接
     *
     * @return
     */
    public static boolean isMobileConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null == info) return false;
        if (info.isConnected())
            return info.getType() == ConnectivityManager.TYPE_MOBILE;
        return false;
    }

    /**
     * 检查是否能用get方式请求url
     *
     * @return
     */
    public static boolean syncGet(String url) {
        try {
            Response response = OkHttpClientHelper
                    .getInstance()
                    .getOkHttpClient()
                    .newCall(new Request.Builder().get().url(url).build())
                    .execute();
            if (response.isSuccessful()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
