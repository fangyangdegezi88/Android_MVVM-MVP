package com.focustech.android.components.mt.sdk.android.service;

/**
 * IntentAction
 *
 * @author zhangxu
 */
public interface IntentAction {
    /**
     * 网络链接状态发生变更
     */
    String ANDROID_NET_CONN_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * 系统启动完成：需要 android.permission.RECEIVE_BOOT_COMPLETED 权限
     */
    String ANDROID_SYSTEM_BOOT = "android.intent.action.BOOT_COMPLETED";
    /**
     * 时间广播
     */
    String ANDROID_TIMER_TICK = "android.intent.action.TIME_TICK";

    /**
     * MT 核心 Service 启动
     */
    String MT_SERVICE_CORE_BOOT = "mt.service.core.boot";
    /**
     * 网络可能正常，当应用能进行http请求时，但发现RUNTIME.NETWORK==NULL
     */
    String ANDROID_NET_MAYBE_CONN_CONNECTIVITY = "android.net.maybe.connectivity";

}
