package com.focustech.android.components.mt.sdk.core.net;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.MessageService;
import com.focustech.android.components.mt.sdk.core.codec.MTMessageCodec;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.HexUtil;
import com.focustech.tm.components.oneway.Configuration;
import com.focustech.tm.components.oneway.Formatter;
import com.focustech.tm.components.oneway.Handler;
import com.focustech.tm.components.oneway.net.Connector;
import com.focustech.tm.components.oneway.net.Server;
import com.focustech.tm.open.sdk.messages.TMMessage;

import java.util.LinkedList;
import java.util.List;

import io.netty.channel.Channel;

/**
 * MT connection，负责解决网络层的各种问题
 *
 * @author zhangxu
 */
public class MTConnection {
    private L l = new L(MTConnection.class.getSimpleName());

    // 物理连接
    private Connector connector;
    public static final int MAX_SECONDS_NET_DELAY = 15;
    // 链接配置
    private Configuration configuration;
    private List<ConnectionListener> listeners = new LinkedList<>();
    private static final long RETRY_INTERVAL = 20000L;

    /**
     * 与服务器断开之后，是否自动连接
     */
    private boolean autoConnect = true;

    /**
     * 重连次数
     */
    private int retryCount = 0;

    /**
     * 构建一个MT的链接
     *
     * @param servers           通信服务器
     * @param heartbeatInterval 心跳间隔
     */
    public MTConnection(String[] servers, long heartbeatInterval, Handler handler) {
        configuration = new Configuration();
        // 设置包编码格式
        configuration.setFormatter(Formatter.HEAD_BODY_BASED);
        // 设置允许自动重链
        configuration.setAllowFailedRetry(true);
        configuration.setFailedRetryInterval(RETRY_INTERVAL);

        // 开启心跳
        configuration.setAutoSendHeartbeat(false);
        configuration.setActiveHeartbeat(true);
        configuration.setHeartbeatInterval(heartbeatInterval);

        // 设置都超时，心跳+10秒，超过10秒没有返回则
        configuration.setReadTimeout((int) heartbeatInterval / 1000 + MAX_SECONDS_NET_DELAY);

        configuration.setCustomCodec(MTMessageCodec.getInstance());

        configuration.setMessageHandler(handler);

        Server[] value = new Server[servers.length];
        int index = 0;
        for (String server : servers) {
            value[index++] = new Server(server);
        }
        connector = new Connector(configuration, value);
    }

    /**
     * 链接
     *
     * @return
     */
    public void connect() {
        l.i("---connect()---");
        AsyncLoginControlContent.clear();
        setAutoConnect(true);
        connector.connect();
    }

    /**
     * 关闭链接，调用之后，不会发起重连
     */
    public void close() {
        l.i("---close()---");
        setAutoConnect(false);
        connector.close(null);
    }

    public void send(TMMessage message) {
        l.i("---send-TMMessage--head is:" + message.getHead() + "\n body is:" + HexUtil.asHex(message.getBody()));
        if (!connector.send(message)) {
            l.i("---send-TMMessage--disconnect--");
            notifyDisconnectToServer();
        }
    }

    public void send(byte[] data) {
        l.i("---send-byte[]--data is:" + HexUtil.asHex(data));
        if (!connector.send(data)) {
            l.i("---send-byte[]--disconnect--");
            notifyDisconnectToServer();
        }
    }

    /**
     * 网络激活后，触发重新链接
     */
    public void onNetWorkActive() {
        l.e(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.NET_STAT, "onNetWorkActive"));
        if (isConnected()) {
            notifyConnectToServer();
            return;
        }
        connect();
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public boolean isConnected() {
        return connector.isConnected();
    }

    public void notifyDisconnectToServer() {
        // 通知客户端，网络异常
        try {
            if (ContextHolder.getMessageService().getBizInvokeCallback() != null) {
                ContextHolder.getMessageService().getBizInvokeCallback().privateDisconnected();
            }
        } catch (Throwable e) {

        }
    }

    public boolean isCurrentChannel(Channel channel) {
        return connector.isCurrentChannel(channel);
    }

    public void notifyConnectToServer() {
        try {
            MessageService service = ContextHolder.getMessageService();
            if (service != null) {
                IBizInvokeCallback iBizInvokeCallback = service.getBizInvokeCallback();
                if (iBizInvokeCallback != null) {
                    iBizInvokeCallback.privateNetworkChanged(MTRuntime.getNetwork().name());
                    iBizInvokeCallback.privateConnected();
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 当前连接的服务器ip信息
     *
     * @return
     */
    public String currentHostAndPort() {
        String[] server = connector.currentHostAndPort();
        return server == null ? "" : "host:" + server[0] + "--port:" + server[1];
    }

    public boolean isAutoConnect() {
        if (autoConnect && configuration.getFailedRetryTimes() >= retryCount) {
            return autoConnect;
        }
        return false;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 重连次数递增使用
     */
    public void updateRetryCount() {
        retryCount++;
    }

    /**
     * 重连次数归零
     */
    public void initRetryCount() {
        retryCount = 0;
    }
}
