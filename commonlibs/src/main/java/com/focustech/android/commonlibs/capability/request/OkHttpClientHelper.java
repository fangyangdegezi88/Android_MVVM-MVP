package com.focustech.android.commonlibs.capability.request;

import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <OkHttpClient管理类>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public class OkHttpClientHelper {

    private volatile static OkHttpClientHelper manager;

    private OkHttpClient mOkHttpClient;

    private final int TIMEOUT = 30;

    private final int WRITE_TIMEOUT = 30;

    private final int READ_TIMEOUT = 30;
    /**
     * 请求url集合
     */
    private HashMap<String, Set<String>> requestMap;

    /**
     * 单例
     *
     * @return
     */
    public static OkHttpClientHelper getInstance() {
        if (manager == null) {
            synchronized (OkHttpClientHelper.class) {
                if (manager == null) {
                    manager = new OkHttpClientHelper();
                }
            }
        }
        return manager;
    }

    /**
     * 构造
     */
    public OkHttpClientHelper() {
        requestMap = new HashMap<String, Set<String>>();
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public HashMap<String, Set<String>> getRequestMap() {
        return requestMap;
    }

    /**
     * 将请求添加到map中
     *
     * @param activityName
     * @param url
     */
    public void addRequest(String activityName, String url) {
        if (requestMap.containsKey(activityName)) {
            requestMap.get(activityName).add(url);
        } else {
            Set<String> urlSet = new HashSet<String>();
            urlSet.add(url);
            requestMap.put(activityName, urlSet);
        }
    }

    /**
     * 取消请求，单个
     *
     * @param url
     */
    public void cancelRequest(String url) {
        try {
            mOkHttpClient.cancel(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 取消请求（总）
     *
     * @param activityName
     */
    public void cancelActivityRequest(String activityName) {
        try {
            if (requestMap.containsKey(activityName)) {
                Set<String> urlSet = requestMap.get(activityName);
                for (String url : urlSet) {
                    cancelRequest(url);
                }
                requestMap.remove(activityName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 网络请求失败展示错误信息
     */
    public class HttpErrorMsg {

        public static final String SERVER_ERROR = "请求失败，请稍后再试";

        public static final String NETWORK_ERROR = "您的网络状况不佳，请检查网络连接";
    }

}
