package com.focustech.android.components.mt.sdk.android.service;

/**
 * 业务场景
 *
 * @author zhangxu
 */
public enum Operation {
    /**
     * 获取所有本地账号
     */
    ACCOUNT_GET_ALL,
    /**
     * 获取指定账号
     */
    ACCOUNT_DELETE,
    /**
     * 清除本地账号
     */
    ACCOUNT_CLEAN,
    /**
     * 修改本地账号信息
     */
    ACCOUNT_UPDATE,
    /**
     * 登陆
     */
    LOGIN,
    /**
     * 登录控制
     */
    LOGIN_CONTROL,
    /**
     * 获取系统信息
     */
    GET_SYS_NTY(CMD.REQ_GET_SYS_NTY),
    /**
     * 重新链接
     */
    RECONNECT(CMD.REQ_RECONNECT),
    /**
     * 退出登陆
     */
    LOGOUT(CMD.REQ_LOGOUT),
    /**
     * 更新用户信息
     */
    UPDATE_USER_INFO(CMD.REQ_UPDATE_USER_INFO),
    /**
     * 主动更新状态
     */
    UPDATE_USER_STATUS(CMD.REQ_UPDATE_USER_STATUS),
    /**
     * 更新用户签名
     */
    UPDATE_USER_SIGNATURE(CMD.REQ_UPDATE_USER_SIGNATURE),
    /**
     * 更新用户昵称
     */
    UPDATE_USER_NICKNAME(CMD.REQ_UPDATE_USER_NICKNAME),
    /**
     * 更新用户头像
     */
    UPDATE_USER_HEAD(CMD.REQ_UPDATE_USER_HEAD),
    /**
     * 更新用户设置
     */
    UPDATE_USER_SETTING(CMD.REQ_UPDATE_USER_SETTING),
    /**
     * 设置免打扰
     */
    UPDATE_NO_DISTURB(CMD.REQ_UPDATE_FRIEND_NODISTURB),
    /**
     * 拉取好友分组和好友列表
     */
    GET_FRIEND_GROUPS(CMD.REQ_FRIEND_GROUPS),
    /**
     * 删除好友分组
     */
    DELETE_FRIEND_GROUP(CMD.REQ_DELETE_FRIEND_GROUP),
    /**
     * 拉取好友的最新信息
     */
    GET_FRIEND_INFO_LIST(CMD.REQ_FRIENDS),
    /**
     * 拉取好友规则
     */
    GET_FRIEND_RULE(CMD.REQ_GET_FRIEND_RULE),
    /**
     * 删除好友
     */
    DELETE_FRIEND(CMD.REQ_DELETE_FRIEND),
    /**
     * 获取用户详情
     */
    GET_USERS_INFO(CMD.REQ_USERS_INFO),
    /**
     * 获取用户设置
     */
    GET_USER_SETTING(CMD.REQ_GET_USER_SETTING),
    /**
     * 获取本地会话列表
     */
    LOCAL_GET_CONVERSATION_LIST,
    /**
     * 拉取本地会话消息列表
     */
    LOCAL_GET_CONVERSATION_MESSAGE_LIST,
    /**
     * 拉取本地扩展消息列表
     */
    LOCAL_GET_EXT_MESSAGE_LIST,
    /**
     * 删除扩展消息
     */
    LOCAL_DELETE_EXT_MESSAGE,
    LOCAL_DELETE_EXT_MESSAGE_LIST,
    /**
     * 更新本地会话设置
     */
    LOCAL_UPDATE_CONVERSATION_SETTING,
    /**
     * 获取本地好友分组列表
     */
    LOCAL_GET_FRIEND_GROUPS,
    /**
     * 更新本地扩展信息
     */
    LOCAL_UPDATE_USER_EXT,
    /**
     * 拉取消息
     */
    FETCH_MESSAGE(CMD.REQ_FETCH_MESSAGE),
    /**
     * 发送消息
     */
    SEND_MESSAGE(CMD.MESSAGE),
    /**
     * 群消息
     */
    SEND_GROUP_MESSAGE(CMD.GROUP_MESSAGE),
    /**
     * 讨论组消息
     */
    SEND_DISCUSSION_MESSAGE(CMD.DISCUSSION_MESSAGE),
    /**
     * 激活聊天
     */
    ACTIVE_CONVERSATION,
    /**
     * 去激活聊天
     */
    INACTIVE_CONVERSATION,
    /**
     * 发送离线文件
     */
    SEND_OFFLINE_FILE(CMD.REQ_ADD_OFFLINE_FILE),
    /**
     * 拉取群
     */
    GET_GROUP_LIST(CMD.REQ_GROUP_LIST),
    // 创建群组
    GROUP_CREATE(CMD.REQ_GROUP_CREATE),
    /**
     * 增加好友
     */
    ADD_FRIEND(CMD.REQ_ADD_FRIEND),
    /**
     * 增加好友响应
     */
    ADD_FRIEND_ANSWER(CMD.REQ_ADD_FRIEND_ANSWER),
    /**
     * 设置群规则
     */
    SET_GROUP_RULE(CMD.REQ_SET_GROUP_RULE),
    /**
     * 获取群规则
     */
    GET_GROUP_RULE(CMD.REQ_GET_GROUP_RULE),
    /**
     * 获取群中个人的设置
     */
    GET_GROUP_USER_RULE(CMD.REQ_GET_GROUP_USER_RULE),
    /**
     * 获取群用户状态
     */
    GET_GROUP_USER_STATUS(CMD.REQ_GET_GROUP_USER_STATUS),
    /**
     * 拉取群组用户信息
     */
    GROUP_USER_INFO_LIST(CMD.REQ_GROUP_USER_INFO_LIST),
    /**
     * 拉取单个群用户信息
     */
    GROUP_USER_INFO(CMD.REQ_GROUP_USER_INFO),
    UPDATE_GROUP_NICKNAME_SETTING(CMD.REQ_GROUP_UPDATE_NICKNAME_SETTING),
    UPDATE_GROUP_REMARK(CMD.REQ_GROUP_UPDATE_REMARK),
    UPDATE_GROUP_NICKNAME(CMD.REQ_GROUP_UPDATE_NICKNAME),
    UPDATE_GROUP_USER_SETTING(CMD.REQ_GROUP_UPDATE_USER_SETTING),
    GROUP_EXIT(CMD.REQ_GROUP_EXIT),
    UPDATE_GROUP_INFO(CMD.REQ_GROUP_UPDATE_INFO),
    INVITE_JOIN_TO_GROUP(CMD.REQ_GROUP_INVITE_JOIN_TO),
    INVITE_USER_JOIN_GROUP_ANSWER(CMD.REQ_GROUP_INVITE_USER_JOIN_ANSWER),
    DELETE_GROUP_USER(CMD.REQ_GROUP_DELETE_USER),
    DISABLE_GROUP(CMD.REQ_GROUP_DISABLE),
    SET_GROUP_ADMIN(CMD.REQ_GROUP_SET_ADMIN),
    JOIN_GROUP(CMD.REQ_GROUP_JOIN),
    JOIN_GROUP_ANSWER(CMD.REQ_GROUP_JOIN_ANSWER),
    GET_DISCUSSION_LIST(CMD.REQ_DISCUSSION_LIST),
    GET_DISCUSSION_USERS_INFO_LIST(CMD.REQ_DISCUSSION_USERS_INFO_LIST),
    CREATE_DISCUSSION(CMD.REQ_DISCUSSION_CREATE),
    INVITE_DISCUSSION(CMD.REQ_DISCUSSION_INVITE),
    DISCUSSION_EXPIRED(CMD.REQ_DISCUSSION_EXPIRED),
    DISCUSSION_EXIT(CMD.REQ_DISCUSSION_EXIT),
    UPDATE_DISCUSSION_USER_SETTING(CMD.REQ_DISCUSSION_UPDATE_USER_SETTING),
    DISCUSSION_UPDATE_NAME(CMD.REQ_DISCUSSION_UPDATE_NAME),
    DISCUSSION_GET_USER_STATUS(CMD.REQ_DISCUSSION_GET_USER_STATUS),
    DISCUSSION_GET_INFO(CMD.REQ_DISCUSSION_GET_INFO), LOCAL_GET_GROUPS, LOCAL_GET_DISCUSSIONS;

    private CMD value;

    Operation() {
    }

    Operation(CMD value) {
        this.value = value;
    }

    public CMD getValue() {
        return value;
    }
}
