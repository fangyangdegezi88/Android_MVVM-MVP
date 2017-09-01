package com.focustech.android.components.mt.sdk.android.service;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.HashMap;
import java.util.Map;

/**
 * 最后时间戳类型
 *
 * @author zhangxu
 */
public interface LastTimestampType {
    long DATA_ACCOUNT = 1;              // 个人信息
    long DATA_FRIEND_GROUPS = 2;        // 联系人用户资料
    long DATA_GROUP_LIST = 3;               // 群组数据最后更新时间
    long DATA_GROUP_USER_INFO = 4;               // 群组用户数据最后更新时间
    long DATA_DISCUSSION_LIST = 5;          // 讨论组数据最后更新时间
    long DATA_DISCUSSION_USER_INFO = 6;          // 讨论组用户数据最后更新时间
    long DATA_LAST_CHAT_TIME = 7;       // 聊天最后阅读时间
    long MESSAGE_FRIEND = 100;          // 联系人消息最后更新时间
    long MESSAGE_GROUP = 101;           // 群组消息最后更新时间
    long MESSAGE_DISCUSSION = 102;      // 讨论组消息最后更新时间
    long READ_FRIEND = 200;          // 联系人消息最后阅读时间
    long READ_GROUP = 201;           // 群组消息最后阅读时间
    long READ_DISCUSSION = 202;      // 讨论组消最后阅读时间
    long REGISTRY = 999;      // 系统第一次注册系统事件的时间

    Map<Messages.RecentContactType, Long> READ_MAPPING = new HashMap<Messages.RecentContactType, Long>() {
        {
            put(Messages.RecentContactType.PERSON, READ_FRIEND);
            put(Messages.RecentContactType.GROUP, READ_GROUP);
            put(Messages.RecentContactType.DISCUSSION, READ_DISCUSSION);
        }
    };

    Map<Messages.RecentContactType, Long> MESSAGE_MAPPING = new HashMap<Messages.RecentContactType, Long>() {
        {
            put(Messages.RecentContactType.PERSON, MESSAGE_FRIEND);
            put(Messages.RecentContactType.GROUP, MESSAGE_GROUP);
            put(Messages.RecentContactType.DISCUSSION, MESSAGE_DISCUSSION);
        }
    };

    Map<Long, Messages.RecentContactType> MESSAGE_MAPPING_REVERSE = new HashMap<Long, Messages.RecentContactType>() {
        {
            put(MESSAGE_FRIEND, Messages.RecentContactType.PERSON);
            put(MESSAGE_GROUP, Messages.RecentContactType.GROUP);
            put(MESSAGE_DISCUSSION, Messages.RecentContactType.DISCUSSION);
        }
    };
}
