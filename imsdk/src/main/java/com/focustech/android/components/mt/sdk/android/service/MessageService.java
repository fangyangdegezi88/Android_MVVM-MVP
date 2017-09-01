package com.focustech.android.components.mt.sdk.android.service;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.pojo.LoginData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.core.net.MTConnection;
import com.focustech.android.components.mt.sdk.core.net.MTMessageHandler;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.HexUtil;
import com.focustech.android.components.mt.sdk.util.TMMessageCodecUtil;
import com.focustech.tm.open.sdk.messages.SyncAboutCMD;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Head;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.util.Timeout;


/**
 * 消息Service，负责消息的收发
 *
 * @author zhangxu
 */
public class MessageService extends MTMessageHandler implements Runnable, AsyncContent.TimeoutHandler {
    private static Logger logger = LoggerFactory.getLogger(MessageService.class);
    // 发送队列线程
    private static final ExecutorService threads = Executors.newSingleThreadExecutor();
    // 发送消息队列
    private static BlockingQueue<TMMessage> toSendQueue = new LinkedBlockingQueue<>();
    private MTConnection connection;

    // 业务无关的心跳
    private static final byte[] WITHOUT_BIZ_HEARTBEAT = new byte[9];

    // 是否在同步中，在同步中不再发送同步请求
    private boolean syncing = false;
    // 同步的seqId，根据这个ID去服务器同步消息
    private long lastSyncSeqId = 0L;
    // 服务器通知的lastSyncSeqId
    private long serverNotifiedSyncSeqId = 0;
    private int cliSeqId = 0;
    private static final TMMessage EXIT = new TMMessage();

    private static final String HEART_KEY = "heartbeat";

    // 业务回调
    private IBizInvokeCallback bizInvokeCallback;

    public MessageService() {
        clean();
        threads.execute(this);
    }

    @Override
    public void onRead_IDLE(Channel channel) {
        sendHeartbeat(); // 发送心跳出去
    }

    @Override
    public void onHeartbeat() {
        // 收到心跳消息
        AsyncContent.cleanContent(HEART_KEY);
    }

    @Override
    public void timeout(String asyncKey, Object asyncContext, Timeout timeout) {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CLOSE, " heartbeat timeout."));
        }

        connection.close();
    }

    @Override
    public void messageReceived(Channel channel, TMMessage message) {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.PACKET, LogFormat.Operation.RECEIVE, LogFormat.getPacketFormat())
                    , message.getHead(), HexUtil.asHex(message.getBody()));
        }

        String cmd = message.getHead().getCmd();

        if (isSyncNotify(cmd)) {
            updateServerNotifySyncSeqId(message);
            syncFromServer();
        } else if (isSyncRsp(cmd)) {
            processSyncRsp(message);
        } else {
            doBiz(message);
        }
    }

    @Override
    public void onChannelActive(Channel channel) {
        l.i(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CONNECT, " onChannelActive"));
        connection.initRetryCount();
        connection.notifyConnectToServer();
        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        String mtModel = sharedPrefLoginInfo.getString(MTModel.class.getSimpleName(), "");
        String data = sharedPrefLoginInfo.getString("loginInfo", "");
        boolean loginState = sharedPrefLoginInfo.getBoolean("loginState", false);
        if (loginState) {
            try {
                if (mtModel != null && mtModel.length() > 0) {
                    CMD.REQ_RECONNECT.getProcessor().request(JSONObject.parseObject(mtModel, MTModel.class));
                } else if (null != data && data.length() > 0) {
                    CMD.REQ_LOGIN.getProcessor().request(JSONObject.parseObject(data, LoginData.class));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onChannelInactive(Channel channel) {
        l.i(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CONNECT, " onChannelInactive"));
        // 迟到的通知，或者操作系统关闭了链接，不是当前链接的断开不通知
        if (!connection.isCurrentChannel(channel)) {
            return;
        }

        connection.notifyDisconnectToServer();

        syncComplete(); // 标记同步已经完成，当链接回复的时候，重新同步

        // 有网络的情况下，尝试重新激活网络，并且不是自己退出和踢下线的前提下。自动
        if (MTRuntime.getNetwork() != MTRuntime.Network.NULL && connection.isAutoConnect()) {
            connection.updateRetryCount();
            connection.onNetWorkActive();
        }
    }

    @Override
    public void onException(Channel channel, Throwable cause) {
        l.i(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CONNECT, "onException"));
        //10s延时，重新连接
        if (MTRuntime.getNetwork() != MTRuntime.Network.NULL && connection.isAutoConnect()) {
            connection.updateRetryCount();
            TaskUtil.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        connection.onNetWorkActive();
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    @Override
    public void onWrite_IDLE(Channel channel) {
        l.i(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CONNECT, "onWrite_IDLE"));

        if (inSyncing()) {
            sendHeartbeat();
        } else {
            sendSyncHeartbeat();
        }
    }

    /**
     * 发送消息到服务器，并且返回当前消息的cliSeqId
     *
     * @param cmd
     * @param body
     * @return
     */
    public int sendMessage(CMD cmd, byte[] body) {
        TMMessage message = new TMMessage();
        message.setHead(getHead(cmd.getValue(), lastSyncSeqId));
        message.setBody(body);

        toSendQueue.add(message);

        return message.getHead().getCliSeqId();
    }


    /**
     * 连接服务器
     */
    public void connectServer() {
        connection.connect();
    }

    /**
     * 当前连接的server ip信息
     *
     * @return
     */
    public String currentHostAndPort() {
        return connection.currentHostAndPort();
    }

    /**
     * 当前是否处于连接状态
     *
     * @return
     */
    public boolean isConnected() {
        return connection.isConnected();
    }

    /**
     * 恢复连接
     */
    public void resume() {
        clean();
        if (null != connection && !connection.isConnected()) {
            connection.connect();
        }
    }

    public void close() {
        clean();
        connection.close();
    }

    public void stop() {
        clean();
        toSendQueue.add(EXIT);
    }

    @Override
    public void run() {
        TMMessage message = null;

        while (true) {
            try {
                message = toSendQueue.take();

                if (message == EXIT) {
                    Log.e("error", "message service exit.");
                    break;
                }

                connection.send(message);
            } catch (Throwable e) {
                if (logger.isErrorEnabled()) {
                    logger.error(LogFormat.format(LogFormat.LogModule.PACKET, LogFormat.Operation.SEND, "error"), e);
                }
            }
        }
    }

    public void setConnection(MTConnection connection) {
        this.connection = connection;
    }

    public Head.TMHeadMessage getHead(String cmd) {
        return getHead(cmd, null, null);
    }

    public Head.TMHeadMessage getHead(String cmd, String svrMsgId) {
        return getHead(cmd, svrMsgId, null);
    }

    public Head.TMHeadMessage getHead(String cmd, long syncSeqId) {
        return getHead(cmd, null, syncSeqId);
    }

    public Head.TMHeadMessage getHead(String cmd, String svrMsgId, Long syncSeqId) {
        Head.TMHeadMessage.Builder builder = Head.TMHeadMessage.newBuilder();
        builder.setDomainName(MTRuntime.getMTSupport());
        builder.setCmd(cmd);
        builder.setVersion(MTRuntime.getCliVersion());
        builder.setCliSeqId(cliSeqId++);
        builder.setProtoVersion(MTRuntime.getMTProtocol());

        if (null != svrMsgId) {
            builder.setSvrSeqId(svrMsgId);
        }

        if (null != syncSeqId) {
            builder.setSeqId(syncSeqId);
        }

        return builder.build();
    }

    public void setIBizInvokeCallback(IBizInvokeCallback bizInvokeCallback) {
        this.bizInvokeCallback = bizInvokeCallback;
    }

    public IBizInvokeCallback getBizInvokeCallback() {
        return bizInvokeCallback;
    }

    private void updateServerNotifySyncSeqId(TMMessage message) {
        serverNotifiedSyncSeqId = message.getHead().getSeqId();
    }

    /**
     * 是sync通知消息
     *
     * @param cmd
     * @return
     */
    private static boolean isSyncNotify(String cmd) {
        return SyncAboutCMD.NTY_SYNC.getValue().equals(cmd);
    }

    /**
     * 是sync响应消息
     *
     * @param cmd
     * @return
     */
    private static boolean isSyncRsp(String cmd) {
        return SyncAboutCMD.RSP_SYNC.getValue().equals(cmd);
    }

    /**
     * 从服务器同步消息
     */
    private void syncFromServer() {
        if (inSyncing()) {
            if (logger.isInfoEnabled()) {
                logger.info(LogFormat.format(LogFormat.LogModule.PACKET, LogFormat.Operation.SYNC, "in syncing"));
            }

            return;
        }

        startSyncing();
        doSync();
    }

    /**
     * 同步
     */
    private void doSync() {
        TMMessage syncMessage = new TMMessage();
        syncMessage.setHead(getHead(SyncAboutCMD.SYNC.getValue()));
        syncMessage.setBody(Messages.Sync.newBuilder().setSeqId(lastSyncSeqId).build().toByteArray());
        connection.send(syncMessage);
    }

    /**
     * 处理同步消息
     *
     * @param message
     */
    private void processSyncRsp(TMMessage message) {
        try {
            Messages.SyncRsp syncRsp = Messages.SyncRsp.parseFrom(message.getBody());

            updateSyncSeqIdBySyncRsp(syncRsp);

            if (logger.isInfoEnabled()) {
                logger.info(LogFormat.format(LogFormat.LogModule.PACKET, LogFormat.Operation.SYNC, "last sync seqId:{}"), lastSyncSeqId);
            }

            // 解码消息
            for (Messages.SyncData data : syncRsp.getDataList()) {
                doBiz(TMMessageCodecUtil.decode(data.getData().toByteArray()));
            }

        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error(LogFormat.format(LogFormat.LogModule.PACKET, LogFormat.Operation.DECODE, "packet decode error"), e);
            }
            // 解码失败
        } finally {
            // 检查是否需要继续同步，这个时候Syncing还处于true状态
            if (syncSeqIdLessThenServer()) {
                doSync();
            } else {
                // 全部同步完了，在变更标示
                syncComplete();
            }
        }
    }

    /**
     * 正式的业务处理
     *
     * @param message
     */
    private void doBiz(TMMessage message) {
        String cmd = message.getHead().getCmd();

        CMDProcessor processor = CMD.parse(cmd).getProcessor();

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}, {}")
                    , cmd, processor.getClass().getSimpleName());
        }

        try {
            processor.onMessage(message);

            if (logger.isInfoEnabled()) {
                logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS_COMPLETE, "{}"), cmd);
            }
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "process error."), e);
            }
        }
    }

    /**
     * 服务端给的seqId重复次数
     * <p>
     * 当有三次重复时 默认本地seqId自增
     */
    private int seqRepNum = 0;

    /**
     * 更新本地的同步seqId
     *
     * @param syncRsp
     */
    private void updateSyncSeqIdBySyncRsp(Messages.SyncRsp syncRsp) {
        if (lastSyncSeqId == syncRsp.getLastSeqId()) {      //当本地的seqId跟需要更新的seqId相同时  执行三次策略  如果三次都相同 则自增
            if (seqRepNum == 3) {
                seqRepNum = 0;
                lastSyncSeqId++;
            }
            seqRepNum++;
        } else {
            seqRepNum = 0;
            lastSyncSeqId = syncRsp.getLastSeqId();
            //将最后一次sync的seqId缓存磁盘  在杀进程重连时恢复
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            sharedPrefLoginInfo.saveLong("lastSyncSeqId", lastSyncSeqId);
        }
    }

    /**
     * 恢复本地seqId
     */
    public void restoreLastSeqId() {
        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        lastSyncSeqId = sharedPrefLoginInfo.getLong("lastSyncSeqId", lastSyncSeqId);
    }

    private boolean syncSeqIdLessThenServer() {
        return lastSyncSeqId < serverNotifiedSyncSeqId;
    }

    public void clean() {
        serverNotifiedSyncSeqId = 0;
        lastSyncSeqId = 0;
        toSendQueue.clear();
        AsyncContent.clear();
        syncComplete();
    }

    private boolean inSyncing() {
        return syncing;
    }

    private void startSyncing() {
        syncing = true;
    }

    private void syncComplete() {
        syncing = false;
    }

    private void sendSyncHeartbeat() {
        // 发送Sync心跳
        TMMessage message = new TMMessage();
        message.setHead(getHead(SyncAboutCMD.HEARTBEAT.getValue(), lastSyncSeqId));
        connection.send(message);
    }

    private void sendHeartbeat() {
        AsyncContent.addContent(HEART_KEY, "", MTConnection.MAX_SECONDS_NET_DELAY * 1000, this);
        connection.send(WITHOUT_BIZ_HEARTBEAT);
    }

}
