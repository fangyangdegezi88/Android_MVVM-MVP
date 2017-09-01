package com.focustech.android.commonlibs.capability.log;


import com.focustech.android.commonlibs.capability.request.http.OkHttpUtil;
import com.focustech.android.commonlibs.capability.request.http.Param;

import java.util.List;

public class LogFormat {
    private LogFormat() {
    }

    public static String getPacketFormat() {
        return "head:{}\r\nbody:{}";
    }

    public static String format(LogModule module, Operation operation, String info) {
        return module.name() + ":" + operation.name() + ":" + info;
    }

    public static String formatHttp(HTTP_TYPE type, String url, Param... params) {
        return LogModule.NET.name() + ":" + Operation.HTTP_REQUEST.name() + ":" + type.name() + ":" + "[" + OkHttpUtil.getInstance().constructUrl(url, params) + "]";
    }

    public static String formatHttp(HTTP_TYPE type, String url, List<Param> params) {
        return LogModule.NET.name() + ":" + Operation.HTTP_REQUEST.name() + ":" + type.name() + ":" + "[" + OkHttpUtil.getInstance().constructUrl(url, params) + "]";
    }

    public static enum LogModule {
        /**
         * 网络层
         */
        NET,
        /**
         * 报文
         */
        PACKET,
        /**
         * 系统
         */
        SYSTEM,
        /**
         * 业务
         */
        SERVICE,
        /**
         * 会话
         */
        SESSION,
        /**
         * 数据库操作
         */
        DB,
        /**
         * 作业
         */
        STUDENT_WORK,
        /**
         * 通知
         */
        STUDENT_NOTICE,
        /**
         * 登录模块
         */
        LOGIN,
    }

    public static enum Operation {
        // 未知异常
        EXCEPTION, CRASH,

        // 网络层操作
        CONNECT, RECONNECT, CLOSE, NET_STAT, HEARTBEAT, POLLING, HTTP_FAIL,

        // 报文
        RECEIVE, SEND, DECODE, CODE, SYNC, LOG,

        // 业务触发
        INVOKE, PROCESS, PROCESS_COMPLETE, TIME, OS_OPERATION, SCENE_CHANGE, SAVE_CACHE, WORK_LIST, NOTICE_LIST,

        // 系统初始化
        INIT_APP, INIT_SDK, INIT_LOG, INIT_THREADS, INIT_NTP, INIT_NET, INIT_NET_CONNECT, INIT_NET_RECEIVER, DESTROY, CHECK_UPGRADE,

        //登录
        LOGIN,

        // 会话
        CREATE, FILL_DATA, REMOVE,

        // 数据库操作
        INSERT, UPDATE, SELECT, DELETE,

        //系统操作
        READ_CARD, MODULE_ANI, HTTP_REQUEST, SERVICE_HEARTBEAT,

        //具体业务
        RE_LOGIN,
        //文件处理
        FILE_OPERATION,
    }

    public enum HTTP_TYPE {
        GET,
        POST,
        PUT,
        DELETE

    }
}
