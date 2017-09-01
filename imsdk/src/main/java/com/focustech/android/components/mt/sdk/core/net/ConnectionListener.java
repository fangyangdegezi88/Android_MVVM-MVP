package com.focustech.android.components.mt.sdk.core.net;

/**
 * 链接状态监听器，给需要关心链接状态的相关组件做一些事情。<br/>
 * 例如：
 * <pre>
 *     1. 掉线：清除发送队列的待发送消息
 *     2.
 * </pre>
 *
 * @author zhangxu
 */
public interface ConnectionListener {
    /**
     * 与服务器链接建立
     */
    void onReconnected();

    /**
     * 与服务器断开链接
     */
    void onDisconnected();
}
