package com.focustech.android.components.mt.sdk.android.db.gen;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig settingsDaoConfig;
    private final DaoConfig lastTimestampDaoConfig;
    private final DaoConfig friendGroupDaoConfig;
    private final DaoConfig friendDaoConfig;
    private final DaoConfig friendExtDaoConfig;
    private final DaoConfig friendRelationshipDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig conversationDaoConfig;
    private final DaoConfig recentContactDaoConfig;
    private final DaoConfig groupDaoConfig;
    private final DaoConfig groupUserDaoConfig;
    private final DaoConfig groupSettingDaoConfig;
    private final DaoConfig systemNotifyDaoConfig;

    private final AccountDao accountDao;
    private final SettingsDao settingsDao;
    private final LastTimestampDao lastTimestampDao;
    private final FriendGroupDao friendGroupDao;
    private final FriendDao friendDao;
    private final FriendExtDao friendExtDao;
    private final FriendRelationshipDao friendRelationshipDao;
    private final MessageDao messageDao;
    private final ConversationDao conversationDao;
    private final RecentContactDao recentContactDao;
    private final GroupDao groupDao;
    private final GroupUserDao groupUserDao;
    private final GroupSettingDao groupSettingDao;
    private final SystemNotifyDao systemNotifyDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        settingsDaoConfig = daoConfigMap.get(SettingsDao.class).clone();
        settingsDaoConfig.initIdentityScope(type);

        lastTimestampDaoConfig = daoConfigMap.get(LastTimestampDao.class).clone();
        lastTimestampDaoConfig.initIdentityScope(type);

        friendGroupDaoConfig = daoConfigMap.get(FriendGroupDao.class).clone();
        friendGroupDaoConfig.initIdentityScope(type);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        friendExtDaoConfig = daoConfigMap.get(FriendExtDao.class).clone();
        friendExtDaoConfig.initIdentityScope(type);

        friendRelationshipDaoConfig = daoConfigMap.get(FriendRelationshipDao.class).clone();
        friendRelationshipDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        conversationDaoConfig = daoConfigMap.get(ConversationDao.class).clone();
        conversationDaoConfig.initIdentityScope(type);

        recentContactDaoConfig = daoConfigMap.get(RecentContactDao.class).clone();
        recentContactDaoConfig.initIdentityScope(type);

        groupDaoConfig = daoConfigMap.get(GroupDao.class).clone();
        groupDaoConfig.initIdentityScope(type);

        groupUserDaoConfig = daoConfigMap.get(GroupUserDao.class).clone();
        groupUserDaoConfig.initIdentityScope(type);

        groupSettingDaoConfig = daoConfigMap.get(GroupSettingDao.class).clone();
        groupSettingDaoConfig.initIdentityScope(type);

        systemNotifyDaoConfig = daoConfigMap.get(SystemNotifyDao.class).clone();
        systemNotifyDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        settingsDao = new SettingsDao(settingsDaoConfig, this);
        lastTimestampDao = new LastTimestampDao(lastTimestampDaoConfig, this);
        friendGroupDao = new FriendGroupDao(friendGroupDaoConfig, this);
        friendDao = new FriendDao(friendDaoConfig, this);
        friendExtDao = new FriendExtDao(friendExtDaoConfig, this);
        friendRelationshipDao = new FriendRelationshipDao(friendRelationshipDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        conversationDao = new ConversationDao(conversationDaoConfig, this);
        recentContactDao = new RecentContactDao(recentContactDaoConfig, this);
        groupDao = new GroupDao(groupDaoConfig, this);
        groupUserDao = new GroupUserDao(groupUserDaoConfig, this);
        groupSettingDao = new GroupSettingDao(groupSettingDaoConfig, this);
        systemNotifyDao = new SystemNotifyDao(systemNotifyDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(Settings.class, settingsDao);
        registerDao(LastTimestamp.class, lastTimestampDao);
        registerDao(FriendGroup.class, friendGroupDao);
        registerDao(Friend.class, friendDao);
        registerDao(FriendExt.class, friendExtDao);
        registerDao(FriendRelationship.class, friendRelationshipDao);
        registerDao(Message.class, messageDao);
        registerDao(Conversation.class, conversationDao);
        registerDao(RecentContact.class, recentContactDao);
        registerDao(Group.class, groupDao);
        registerDao(GroupUser.class, groupUserDao);
        registerDao(GroupSetting.class, groupSettingDao);
        registerDao(SystemNotify.class, systemNotifyDao);
    }

    public void clear() {
        accountDaoConfig.getIdentityScope().clear();
        settingsDaoConfig.getIdentityScope().clear();
        lastTimestampDaoConfig.getIdentityScope().clear();
        friendGroupDaoConfig.getIdentityScope().clear();
        friendDaoConfig.getIdentityScope().clear();
        friendExtDaoConfig.getIdentityScope().clear();
        friendRelationshipDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
        conversationDaoConfig.getIdentityScope().clear();
        recentContactDaoConfig.getIdentityScope().clear();
        groupDaoConfig.getIdentityScope().clear();
        groupUserDaoConfig.getIdentityScope().clear();
        groupSettingDaoConfig.getIdentityScope().clear();
        systemNotifyDaoConfig.getIdentityScope().clear();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public SettingsDao getSettingsDao() {
        return settingsDao;
    }

    public LastTimestampDao getLastTimestampDao() {
        return lastTimestampDao;
    }

    public FriendGroupDao getFriendGroupDao() {
        return friendGroupDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public FriendExtDao getFriendExtDao() {
        return friendExtDao;
    }

    public FriendRelationshipDao getFriendRelationshipDao() {
        return friendRelationshipDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }

    public RecentContactDao getRecentContactDao() {
        return recentContactDao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public GroupUserDao getGroupUserDao() {
        return groupUserDao;
    }

    public GroupSettingDao getGroupSettingDao() {
        return groupSettingDao;
    }

    public SystemNotifyDao getSystemNotifyDao() {
        return systemNotifyDao;
    }

}