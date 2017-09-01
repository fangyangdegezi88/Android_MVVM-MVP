package com.focustech.android.components.mt.sdk.util;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendRelationship;
import com.focustech.android.components.mt.sdk.android.db.gen.Settings;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractMessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageMeta;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 转换器，把protobuf对象转换为本地数据库对象
 *
 * @author zhangxu
 */
public class BeanConverter {
    private BeanConverter() {

    }

    public static Friend toFriend(String userId, FriendBase data, UserBase userBase) {
        Friend friend = new Friend();
        ReflectionUtil.copyProperties(data, friend);
        ReflectionUtil.copyProperties(userBase, friend);
        friend.setUserHeadType(Long.valueOf(userBase.getUserHeadType().getNumber()));
        friend.setUserId(userId);
        return friend;
    }

    public static Friend toFriend(String userId, UserBase data) {
        Friend friend = new Friend();
        ReflectionUtil.copyProperties(data, friend);
        friend.setUserHeadType(Long.valueOf(data.getUserHeadType().getNumber()));
        friend.setUserId(userId);
        friend.setFriendUserId(data.getUserId());
        return friend;
    }

    public static Account toAccount(Messages.UserInfoNty nty) {
        Account account = new Account();
        fillProperties(nty, account);
        return account;
    }

    public static Account toAccount(Messages.UserInfoRsp rsp, String password) {
        Account account = new Account();
        fillProperties(rsp, account);
        account.setPassword(password);
        return account;
    }

    public static Settings toSettings(Messages.UserSettingRsp rsp) {
        Settings settings = new Settings();

        if (rsp.hasAllowChatRecordOnServer()) {
            settings.setAllowChatRecordOnServer(Long.valueOf(rsp.getAllowChatRecordOnServer().getNumber()));
        }

        if (rsp.hasAllowStrangerChatToMe()) {
            settings.setAllowStrangerChatToMe(Long.valueOf(rsp.getAllowStrangerChatToMe().getNumber()));
        }

        if (rsp.hasFriendRule()) {
            settings.setFriendRule(Long.valueOf(rsp.getFriendRule().getNumber()));
        }

        if (rsp.hasGroupRule()) {
            settings.setGroupRule(Long.valueOf(rsp.getGroupRule().getNumber()));
        }

        if (rsp.hasCustomerSettings()) {
            settings.setCustomerSettings(rsp.getCustomerSettings());
        }

        return settings;
    }

    public static MessageData toMessageData(Messages.Message msg, boolean parse) {
        MessageData data = new MessageData();

        ReflectionUtil.copyProperties(msg, data);

        data.setSendStatus(AbstractMessageData.Status.SEND_SUCCESSFUL);
        data.setContactType(Messages.RecentContactType.PERSON_VALUE);

        if (null != msg.getMsgMeta() && msg.getMsgMeta().length() > 0) {
            data.setMsgMeta(JSONObject.parseObject(msg.getMsgMeta(), MessageMeta.class));
        } else {
            data.setMsgMeta(new MessageMeta());
        }

        if (parse) {
            data.parse();
        }

        return data;
    }

    public static MessageData toMessageData(Messages.GroupMessage msg, boolean parse) {
        MessageData data = new MessageData();

        ReflectionUtil.copyProperties(msg, data);

        data.setSendStatus(AbstractMessageData.Status.SEND_SUCCESSFUL);
        data.setContactType(Messages.RecentContactType.GROUP_VALUE);
        data.setContactId(msg.getGroupId());
        data.setFromUserId(msg.getUserId());

        if (null != msg.getMsgMeta() && msg.getMsgMeta().length() > 0) {
            data.setMsgMeta(JSONObject.parseObject(msg.getMsgMeta(), MessageMeta.class));
        } else {
            data.setMsgMeta(new MessageMeta());
        }

        if (parse) {
            data.parse();
        }

        return data;
    }

    public static MessageData toMessageData(Messages.DiscussionMessage msg, boolean parse) {
        MessageData data = new MessageData();

        ReflectionUtil.copyProperties(msg, data);

        data.setSendStatus(AbstractMessageData.Status.SEND_SUCCESSFUL);
        data.setContactType(Messages.RecentContactType.DISCUSSION_VALUE);
        data.setContactId(msg.getDiscussionId());
        data.setFromUserId(msg.getUserId());

        if (null != msg.getMsgMeta() && msg.getMsgMeta().length() > 0) {
            data.setMsgMeta(JSONObject.parseObject(msg.getMsgMeta(), MessageMeta.class));
        } else {
            data.setMsgMeta(new MessageMeta());
        }

        if (parse) {
            data.parse();
        }

        return data;
    }

    public static FriendRelationship toFriendRelationship(String userId, FriendBase data) {
        FriendRelationship value = new FriendRelationship();

        value.setFriendGroupId(data.getFriendGroupId());
        value.setFriendUserId(data.getFriendUserId());
        value.setUserId(userId);

        return value;
    }

    private static void fillProperties(Object from, Object to) {
        try {
            ReflectionUtil.copyProperties(from, to);
        } catch (Exception e) {
            // 忽略
        }
    }
}
