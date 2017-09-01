package com.focustech.android.components.mt.sdk.util;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 数据库代码自动生成
 *
 * @author zhangxu
 */
public class DBGenerator {
    private static final String PROJECT_SRC_PATH = "C:\\workspace\\android\\TM\\android\\trunk\\components\\MT-SDK\\app\\src\\main\\java";
    private static final String DEFAULT_PACKAGE = "com.focustech.android.components.mt.sdk.android.db.gen";
    private static final int DB_VERSION = 1;

    // 表名称:
    private static final String TABLE_NAME_ACCOUNT = "Account";    // 本地账号表
    private static final String TABLE_NAME_SETTINGS = "Settings";    // 设置表
    private static final String TABLE_NAME_LAST_TIMESTAMP = "LastTimestamp";    // 最后更新时间戳
    private static final String TABLE_NAME_FRIEND_GROUP = "FriendGroup"; // 好友分组表
    private static final String TABLE_NAME_FRIEND = "Friend"; // 好友表，包括陌生人，这个表即所有的用户
    private static final String TABLE_NAME_FRIEND_EXT = "FriendExt"; // 用户扩展表，基于列模式，字段可以无限制扩展
    private static final String TABLE_NAME_FRIEND_RELATIONSHIP = "FriendRelationship"; // 好友关系表
    private static final String TABLE_NAME_MESSAGE = "Message";  // 消息表
    private static final String TABLE_NAME_SYS_NTY = "SystemNotify";  // 系统通知
    private static final String TABLE_NAME_CONVERSATION = "Conversation";  // 聊天会话
    private static final String TABLE_NAME_RECENT_CONTACT = "RecentContact";  // 最近联系人
    private static final String TABLE_NAME_GROUP = "Group"; // 群表
    private static final String TABLE_NAME_GROUP_USER = "GroupUser"; // 群用户表
    private static final String TABLE_NAME_GROUP_SETTING = "GroupSetting"; // 群设置表

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DB_VERSION, DEFAULT_PACKAGE);
        generateAccount(schema);
        generateSettings(schema);
        generateLastTimestamp(schema);
        generateFriendGroup(schema);
        generateFriend(schema);
        generateFriendExt(schema);
        generateFriendRelationship(schema);
        generateMessage(schema);
        generateConversation(schema);
        generateRecentContact(schema);
        generateGroup(schema);
        generateGroupUser(schema);
        generateGroupSetting(schema);
        generateSystemNotify(schema);
        new DaoGenerator().generateAll(schema, PROJECT_SRC_PATH);
    }

    private static void generateAccount(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_ACCOUNT);
        createUserInfoBase(entity, true);
        entity.addStringProperty("password").notNull();
        entity.addStringProperty("lastToken").notNull();
        entity.addLongProperty("lastAction").notNull(); // 上一次行为：0：无;1：主动退出
    }

    private static void generateLastTimestamp(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_LAST_TIMESTAMP);

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();   // 用户ID
        entity.addLongProperty("type").notNull();       // 联系类型。业务枚举 {#link com.focustech.android.components.mt.sdk.android.service.LastTimestampType}
        entity.addLongProperty("timestamp").notNull();  // 时间
        entity.addStringProperty("contactId");          // 联系ID
    }

    private static void generateSettings(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_SETTINGS);

        entity.addStringProperty("userId").primaryKey().notNull();
        entity.addLongProperty("allowStrangerChatToMe");
        entity.addLongProperty("allowChatRecordOnServer");
        entity.addLongProperty("friendRule");
        entity.addLongProperty("groupRule");
        entity.addStringProperty("customerSettings");
    }

    private static void generateFriendGroup(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_FRIEND_GROUP);
        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("friendGroupId").notNull();
        entity.addStringProperty("friendGroupName").notNull();
        entity.addLongProperty("friendGroupType").notNull();
    }

    private static void generateFriendRelationship(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_FRIEND_RELATIONSHIP);
        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("friendGroupId").notNull();
        entity.addStringProperty("friendUserId").notNull();
    }

    private static void generateFriend(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_FRIEND);

        entity.addIdProperty();

        createUserInfoBase(entity, false);

        entity.addStringProperty("friendUserId").notNull();
        entity.addStringProperty("friendGroupId");
        entity.addStringProperty("remark");
        entity.addLongProperty("lastChatTimestamp");
        entity.addBooleanProperty("onlineRemind");
        entity.addBooleanProperty("noDisturb");
    }

    private static void generateFriendExt(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_FRIEND_EXT);

        entity.addIdProperty();
        entity.addStringProperty("friendUserId").notNull().index();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("value").notNull();
    }

    private static void createUserInfoBase(Entity entity, boolean isUserIdPK) {
        if (isUserIdPK) {
            entity.addStringProperty("userId").primaryKey().notNull();
        } else {
            entity.addStringProperty("userId").notNull();
        }

        entity.addStringProperty("userName");
        entity.addStringProperty("userNickName");
        entity.addStringProperty("userSignature");
        entity.addLongProperty("userHeadType");
        entity.addStringProperty("userHeadId");
        entity.addLongProperty("timestamp");
        entity.addLongProperty("tmNum");
        entity.addLongProperty("role");
        entity.addStringProperty("domain");
    }

    private static void generateMessage(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_MESSAGE);

        entity.addIdProperty();
        entity.addStringProperty("userId").index();               // 所属用户
        entity.addStringProperty("localMsgId").unique();          // 本地消息ID
        entity.addStringProperty("svrMsgId").unique().index();    // 服务器消息唯一ID
        entity.addStringProperty("fromUserId").notNull().index(); // 发送方
        entity.addStringProperty("fromUserName").notNull();       // 发送方用户名
        entity.addStringProperty("contactId").notNull().index();  // 联系对象
        entity.addLongProperty("contactType").notNull().index();  // 联系类型
        entity.addStringProperty("msg").notNull();                // 消息内容
        entity.addLongProperty("msgType").notNull();              // 消息类型
        entity.addStringProperty("msgMeta");                      // 消息meta
        entity.addStringProperty("fileIds");                      // 文件ID
        entity.addLongProperty("sendStatus").notNull();           // 消息发送状态
        entity.addLongProperty("status");                         // 状态
        entity.addLongProperty("timestamp").index().notNull();    // 消息时间戳
    }

    private static void generateConversation(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_CONVERSATION);

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();             // 用户
        entity.addLongProperty("contactType").notNull();          // 联系类型,Messages.RecentContactType
        entity.addStringProperty("contactId").notNull();          // 联系ID
        entity.addStringProperty("contactName");                  // 联系名称
        entity.addStringProperty("setting");                      // 自定义设置，JSON对象
        entity.addLongProperty("timestamp").notNull();            // 创建时间
    }

    private static void generateRecentContact(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_RECENT_CONTACT);

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();             // 用户
        entity.addLongProperty("contactType").notNull();          // 联系类型,Messages.RecentContactType
        entity.addStringProperty("contactId").notNull();          // 联系ID
        entity.addLongProperty("timestamp").notNull();            // 创建时间
    }

    private static void generateGroup(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_GROUP);
        entity.setTableName("GROUP_CHAT");

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("groupId").notNull();
        entity.addStringProperty("groupName");
        entity.addStringProperty("groupSignature");
        entity.addStringProperty("groupKeyword");
        entity.addStringProperty("groupDesc");
        entity.addStringProperty("groupRemark");
        entity.addLongProperty("groupType");
        entity.addLongProperty("validateRule");
        entity.addStringProperty("addUserId");
        entity.addStringProperty("addUserName");
        entity.addStringProperty("addTime");
        entity.addStringProperty("groupNo");
        entity.addLongProperty("limit");
        entity.addLongProperty("adminCount");
        entity.addLongProperty("groupEnable");
        entity.addLongProperty("timestamp");
        entity.addLongProperty("lastActiveTimestamp"); // 最后激活时间。对应临时群聊
        entity.addLongProperty("feature"); // 特性，区分各种类型的群聊。0:永久。1：临时（讨论组）
    }

    private static void generateGroupUser(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_GROUP_USER);

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("groupId").notNull();
        entity.addStringProperty("groupUserId").notNull();
        entity.addLongProperty("userType");
        entity.addLongProperty("groupNickName");
        entity.addLongProperty("lastChatTimestamp");
        entity.addBooleanProperty("nickNameSetting");
        entity.addLongProperty("userHeadType");
        entity.addStringProperty("userHeadId");
        entity.addLongProperty("feature"); // 特性，区分各种类型的群聊。0:永久。1：临时（讨论组）
    }

    private static void generateGroupSetting(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_GROUP_SETTING);

        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("groupId").notNull();
        entity.addLongProperty("messageSetting").notNull();
        entity.addStringProperty("setting");
        entity.addLongProperty("feature").notNull(); // 特性，区分各种类型的群聊。0:永久。1：临时（讨论组）
    }

    private static void generateSystemNotify(Schema schema) {
        Entity entity = schema.addEntity(TABLE_NAME_SYS_NTY);
        entity.addIdProperty();
        entity.addStringProperty("userId").notNull();
        entity.addStringProperty("cmd").notNull();
        entity.addLongProperty("contactType");
        entity.addStringProperty("contactId");
        entity.addStringProperty("relatedId");
        entity.addBooleanProperty("processed");
        entity.addByteArrayProperty("data").notNull();
        entity.addLongProperty("addTime").notNull(); // 添加时间
        entity.addLongProperty("updateTime");        // 处理时间
    }
}
