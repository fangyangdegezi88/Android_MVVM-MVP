package com.focustech.android.components.mt.sdk;


import com.focustech.android.components.mt.sdk.android.service.pojo.user.EmptyUserExt;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.IUserExt;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.lang.ArrayUtils;

import ch.qos.logback.classic.Level;

/**
 * MT 运行时环境
 *
 * @author zhangxu
 */
public final class MTRuntime {
    private static final String ENV_NETWORK = "os.net";
    private static final String ENV_MT_DB_UPGRADE = "mt.db.upgrade";
    private static final String ENV_MT_SUPPORT = "mt.support";
    private static final String ENV_MT_PROTOCOL = "mt.protocol";
    private static final String ENV_CLI_VERSION = "mt.cli.version";
    private static final String ENV_MT_CONN_STATE = "mt.conn.state";
    private static final String ENV_MT_SERVERS = "mt.conn.servers";
    private static final String ENV_MT_NTP_SERVER = "mt.conn.ntp.server";
    private static final String ENV_MT_NTP_PORT = "mt.conn.ntp.port";
    private static final String ENV_MT_HTTP_SERVER = "mt.conn.http.server";
    private static final String ENV_MT_HTTP_PORT = "mt.conn.http.port";
    private static final String ENV_MT_HEARTBEAT = "mt.conn.heartbeat";
    private static final String ENV_MT_LOG_DIR_NAME = "mt.log.dir.name";
    private static final String ENV_MT_LOG_NAME = "mt.log.name";
    private static final String ENV_MT_LOG_LEVEL = "mt.log.level";
    private static final String ENV_MT_LOG_DAYS = "mt.log.days";
    private static final String ENV_MT_LOG_FORMAT = "mt.log.format";
    private static final String ENV_MT_FETCH_DAYS = "mt.fetch.days";
    private static final String ENV_MT_FETCH_COUNT = "mt.fetch.count";
    private static final String ENV_MT_FACE_TAGS = "mt.face.tags";
    private static final String ENV_MT_PIC_TAG = "mt.pic.tag";

    private static final String ENV_MT_OPTIONS_SQL = "mt.options.sql";

    private static final String ENV_MT_OPTIONS_LOCAL_CONVERSATIONS = "mt.options.local.conversations";
    private static final String ENV_MT_OPTIONS_LOCAL_FRIENDS = "mt.options.local.friends";
    private static final String ENV_MT_OPTIONS_LOCAL_GROUPS = "mt.options.local.groups";
    private static final String ENV_MT_OPTIONS_LOCAL_DISCUSSIONS = "mt.options.local.discussions";

    private static final String ENV_MT_OPTIONS_REMOTE_SETTINGS = "mt.options.remote.setting";
    private static final String ENV_MT_OPTIONS_REMOTE_GROUPS = "mt.options.remote.groups";
    private static final String ENV_MT_OPTIONS_REMOTE_DISCUSSIONS = "mt.options.remote.discussions";
    private static final String ENV_MT_OPTIONS_REMOTE_SYS = "mt.options.remote.sys";
    private static final String ENV_MT_OPTIONS_REMOTE_FRIENDS = "mt.options.remote.friends";
    private static final String ENV_MT_OPTIONS_REMOTE_MESSAGES_DELAY = "mt.options.remote.messages.delay";
    private static final String ENV_MT_OPTIONS_REMOTE_MESSAGES = "mt.options.remote.messages";
    private static final String ENV_MT_OPTIONS_REMOTE_MESSAGES_GROUPS = "mt.options.remote.messages.groups";
    private static final String ENV_MT_OPTIONS_REMOTE_MESSAGES_DISCUSSIONS = "mt.options.remote.messages.discussions";

    private static final String ENV_MT_OPTIONS_ENCODE_ENABLE = "mt.options.encode.enable";
    private static final String ENV_MT_OPTIONS_ENCODE_KEY = "mt.options.encode.key";


    // 扩展用户信息
    private static final String ENV_MT_EXT_USER = "mt.ext.user";
    // 扩展消息，客户端自己处理
    private static final String ENV_MT_EXT_MSG_TYPE = "mt.ext.msg.tye";

    private static final String URL_UPLOAD_FILE = "/tm/file/upload";
    private static final String URL_DOWNLOAD_FILE = "/tm/file/download";

    private static boolean publish = true;

    private MTRuntime() {
    }

    public static boolean isPublish() {
        return publish;
    }

    public static void setPublish(boolean publish) {
        MTRuntime.publish = publish;
    }

    /**
     * 获取表情占位符
     *
     * @return
     */
    public static String[] getFaceTags() {
        return getStringValue(ENV_MT_FACE_TAGS, "").split(",");
    }

    public static String getPicTag() {
        return getStringValue(ENV_MT_PIC_TAG, "/:b0");
    }

    public static String getLogFormat() {
        return getStringValue(ENV_MT_LOG_FORMAT, "");
    }

    public static String getLogDirName() {
        return getStringValue(ENV_MT_LOG_DIR_NAME, "");
    }

    public static String getLogName() {
        return getStringValue(ENV_MT_LOG_NAME, "");
    }

    public static Level getLogLevel() {
        return Level.toLevel(getStringValue(ENV_MT_LOG_LEVEL, ""), Level.INFO);
    }

    public static int getLogDays() {
        return getIntValue(ENV_MT_LOG_DAYS);
    }

    public static String getCliVersion() {
        return getStringValue(ENV_CLI_VERSION, "");
    }

    public static int getMTProtocol() {
        return getIntValue(ENV_MT_PROTOCOL);
    }

    public static String getMTSupport() {
        return getStringValue(ENV_MT_SUPPORT, "");
    }

    public static long getMTHeartbeat() {
        return getLongValue(ENV_MT_HEARTBEAT);
    }

    /**
     * 获取可用的服务列表
     *
     * @return
     */
    public static String[] getServers() {
        String value = getStringValue(ENV_MT_SERVERS, null);

        return null == value ? ArrayUtils.EMPTY_STRING_ARRAY : value.split(",");
    }

    /**
     * 获取可用的Http服务
     *
     * @return
     */
    public static String getHttpServer() {
        return getStringValue(ENV_MT_HTTP_SERVER, null);
    }

    /**
     * 获取可用的Http端口
     *
     * @return
     */
    public static int getHttpPort() {
        return getIntValue(ENV_MT_HTTP_PORT);
    }

    /**
     * 获取可用的Ntp服务
     *
     * @return
     */
    public static String getNTPServer() {
        return getStringValue(ENV_MT_NTP_SERVER, null);
    }

    /**
     * 获取可用的Ntp端口
     *
     * @return
     */
    public static int getNTPPort() {
        return getIntValue(ENV_MT_NTP_PORT);
    }

    public static String getFileUploadUrl() {
        return getHttpBase() + URL_UPLOAD_FILE;
    }

    public static String getFileDownloadUrl(String type, String fileId) {
        return getHttpBase() + URL_DOWNLOAD_FILE + "?type=" + type + "&fileid=" + fileId;
    }

    public static String getHttpBase() {
        return "http://" + getHttpServer() + ":" + getHttpPort();
    }

    /**
     * 设置当前的网络情况
     *
     * @param value
     */
    public static void setNetWork(Network value) {
        setENV(ENV_NETWORK, value.name());
    }

    /**
     * 获取当前网络链接类型
     *
     * @return
     */
    public static Network getNetwork() {
        return Network.valueOf(getStringValue(ENV_NETWORK, Network.NULL.name()));
    }

    /**
     * 是否和MT通信平台建立链接
     *
     * @return
     */
    public static boolean isConnected() {
        return getBooleanValue(ENV_MT_CONN_STATE);
    }

    /**
     * 拉取消息个数
     *
     * @return
     */
    public static int getMTFetchCount() {
        return getIntValue(ENV_MT_FETCH_COUNT);
    }

    /**
     * 拉取消息天数
     *
     * @return
     */
    public static int getMTFetchDays() {
        return getIntValue(ENV_MT_FETCH_DAYS);
    }


    public static boolean isDBUpgrade() {
        return Boolean.valueOf(getStringValue(ENV_MT_DB_UPGRADE, "false"));
    }

    public static boolean optionsSQL() {
        return getBooleanValue(ENV_MT_OPTIONS_SQL);
    }

    public static boolean optionsLocalConversations() {
        return getBooleanValue(ENV_MT_OPTIONS_LOCAL_CONVERSATIONS);
    }

    public static boolean optionsLocalFriends() {
        return getBooleanValue(ENV_MT_OPTIONS_LOCAL_FRIENDS);
    }

    public static boolean optionsLocalGroups() {
        return getBooleanValue(ENV_MT_OPTIONS_LOCAL_GROUPS);
    }

    public static boolean optionsLocalDiscussions() {
        return getBooleanValue(ENV_MT_OPTIONS_LOCAL_DISCUSSIONS);
    }


    public static boolean optionsRemoteSettings() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_SETTINGS);
    }

    public static boolean optionsRemoteSys() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_SYS);
    }

    public static boolean optionsRemoteGroups() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_GROUPS);
    }

    public static boolean optionsRemoteDiscussions() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_DISCUSSIONS);
    }

    public static boolean optionsRemoteFriends() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_FRIENDS);
    }

    public static int optionsRemoteMessagesDelay() {
        return getIntValue(ENV_MT_OPTIONS_REMOTE_MESSAGES_DELAY);
    }

    public static boolean optionsRemoteMessages() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_MESSAGES);
    }

    public static boolean optionsRemoteMessagesGroups() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_MESSAGES_GROUPS);
    }

    public static boolean optionsRemoteMessagesDiscussions() {
        return getBooleanValue(ENV_MT_OPTIONS_REMOTE_MESSAGES_DISCUSSIONS);
    }

    public static boolean optionsEncodeEnable() {
        return getBooleanValue(ENV_MT_OPTIONS_ENCODE_ENABLE);
    }

    public static String optionsEncodeKey() {
        return getStringValue(ENV_MT_OPTIONS_ENCODE_KEY, "");
    }

    public static Class<? extends IUserExt> getUserExt() {
        String clazz = getStringValue(ENV_MT_EXT_USER, EmptyUserExt.class.getCanonicalName());
        Class<? extends IUserExt> value = EmptyUserExt.class;

        try {
            value = (Class<? extends IUserExt>) Class.forName(clazz);
        } catch (ClassNotFoundException e) {
        }

        return value;
    }

    public static Messages.MessageType[] getMsgTypeExt() {
        String[] types = getStringValue(ENV_MT_EXT_MSG_TYPE, "" + Integer.MAX_VALUE).split(",");
        Messages.MessageType[] value = new Messages.MessageType[types.length];
        int i = 0;

        for (String type : types) {
            value[i++] = Messages.MessageType.valueOf(type);
        }

        return value;
    }

    private static String getStringValue(String key, String defaultValue) {
        String value = System.getProperty(key);
        return value == null ? defaultValue : value;
    }

    private static boolean getBooleanValue(String key) {
        String value = System.getProperty(key);
        return value == null ? false : Boolean.valueOf(value);
    }

    private static long getLongValue(String key) {
        String value = System.getProperty(key);
        return value == null ? 0L : Long.parseLong(value);
    }

    private static int getIntValue(String key) {
        String value = System.getProperty(key);
        return value == null ? 0 : Integer.parseInt(value);
    }

    private static void setENV(String key, String value) {
        System.setProperty(key, value);
    }

    private static void removeENV(String key) {
        System.clearProperty(key);
    }

    private static final long KB = 1024;

    public enum Network {
        /**
         * 无网络链接
         */
        NULL(0L),
        /**
         * wifi 链接
         */
        WIFI(KB * 256),
        /**
         * 4G 链接
         */
        MOBILE_4G(KB * 512),
        /**
         * 3G 链接
         */
        MOBILE_3G(KB * 256),
        /**
         * 2G 链接
         */
        MOBILE_2G(KB * 4),
        /**
         * 未知手机网络链接
         */
        MOBILE_UNKNOWN(KB * 256);

        private long upload;
        private long download;

        Network(long upload) {
            this.upload = upload;
            this.download = this.upload * 2;
        }

        public boolean isMobile() {
            return this != NULL && this != WIFI;
        }

        public boolean isWifi() {
            return this == WIFI;
        }

        public long getUpload() {
            return upload;
        }

        public long getDownload() {
            return download;
        }
    }
}
