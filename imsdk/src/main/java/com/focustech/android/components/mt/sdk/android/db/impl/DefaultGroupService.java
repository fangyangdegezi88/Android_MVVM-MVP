package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.Group;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupDao;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupSetting;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupSettingDao;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupUser;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupUserDao;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author zhangxu
 */
public class DefaultGroupService implements IGroupService {
    private static GroupDao dao = DBHelper.getInstance().getGroupDao();
    private static GroupUserDao userDao = DBHelper.getInstance().getGroupUserDao();
    private static GroupSettingDao settingDao = DBHelper.getInstance().getGroupSettingDao();
    private static final IGroupService INSTANCE = new DefaultGroupService();

    public static IGroupService getInstance() {
        return INSTANCE;
    }

    @Override
    public void delete(final String userId, final String groupId, final int feature) {
        DBHelper.getInstance().runInTx(new Runnable() {
            @Override
            public void run() {
                List<String> ids = new ArrayList<>();
                ids.add(groupId);
                delete(userId, ids, feature);
            }
        });
    }

    @Override
    public void deleteGroupUser(String userId, String groupId, String groupUserId, int feature) {
        List<String> ids = new ArrayList<>();
        ids.add(groupUserId);
        deleteGroupUserIds(userId, groupId, ids, feature);
    }

    @Override
    public void deleteGroupUser(String userId, String groupId, List<String> groupUserIds, int feature) {
        deleteGroupUserIds(userId, groupId, groupUserIds, feature);
    }

    @Override
    public void updateGroupUserUserType(final String userId, final String groupId, final List<String> groupUserIds, final Messages.UserType userType, final int feature) {
        DBHelper.getInstance().runInTx(new Runnable() {
            @Override
            public void run() {
                for (String groupUserId : groupUserIds) {
                    MTGroupUser user = new MTGroupUser();
                    user.setUserType(userType);
                    user.setGroupUserId(groupUserId);
                    user.setGroupId(groupId);

                    doAddOrUpdateUser(userId, user, feature);
                }
            }
        });
    }

    @Override
    public void reset(final String userId, final MTGroups groups, final int feature) {
        List<String> exists = getAllGroupIds(userId, feature);
        Set<String> newGroupIds = groups.getGroupIds();

        final Collection<String> needDelete = CollectionUtils.subtract(exists, newGroupIds);
        final Collection<String> needAdd = CollectionUtils.subtract(newGroupIds, exists);
        final Collection<String> intersection = CollectionUtils.intersection(exists, newGroupIds);

        DBHelper.getInstance().runInTx(new Runnable() {
            @Override
            public void run() {

                delete(userId, needDelete, feature);

                for (String groupId : needAdd) {
                    doAddOrUpdate(userId, groups.getGroup(groupId), feature);
                }

                for (String groupId : intersection) {
                    resetUsers(userId, groups.getGroup(groupId), feature);
                }
            }
        });
    }

    @Override
    public void addOrUpdate(String userId, MTGroup group, int feature) {
        addInTx(userId, group, feature, true);
    }

    @Override
    public void addOrUpdateGroupUser(String userId, MTGroupUser groupUser, int feature) {
        addUserInTx(userId, groupUser, feature, true);
    }

    @Override
    public void addOrUpdateGroupSetting(String userId, GroupSetting setting, int feature) {
        GroupSetting old = getGroupSetting(userId, setting.getGroupId(), feature);

        if (null != old) {
            ReflectionUtil.copyProperties(setting, old);
        } else {
            old = setting;
        }

        settingDao.insertOrReplace(old);
    }

    @Override
    public GroupSetting getGroupSetting(String userId, String groupId, int feature) {
        return settingDao.queryBuilder().where(GroupSettingDao.Properties.UserId.eq(userId), GroupSettingDao.Properties.GroupId.eq(groupId), GroupSettingDao.Properties.Feature.eq(feature)).unique();
    }

    @Override
    public Group get(String userId, String groupId, int feature) {
        return dao.queryBuilder().where(GroupDao.Properties.UserId.eq(userId), GroupDao.Properties.Feature.eq(feature)
                , GroupDao.Properties.GroupId.eq(groupId)).unique();
    }

    @Override
    public List<Group> getAll(String userId, int feature) {
        return dao.queryBuilder().where(GroupDao.Properties.UserId.eq(userId), GroupDao.Properties.Feature.eq(feature)).list();
    }

    private void resetUsers(String userId, MTGroup group, int feature) {
        List<String> exists = getAllGroupUserIds(userId, group.getGroupId(), feature);
        Set<String> newGroupUserIds = group.getGroupUserIds();

        final Collection<String> needDelete = CollectionUtils.subtract(exists, newGroupUserIds);
        final Collection<String> needAdd = CollectionUtils.subtract(newGroupUserIds, exists);

        if (needDelete.size() > 0) {
            deleteGroupUserIds(userId, group.getGroupId(), needDelete, feature);
        }

        for (String groupUserId : needAdd) {
            doAddOrUpdateUser(userId, group.getGroupUser(groupUserId), feature);
        }
    }

    private void addInTx(final String userId, final MTGroup group, final int feature, boolean tx) {
        if (tx) {
            DBHelper.getInstance().runInTx(new Runnable() {
                @Override
                public void run() {
                    doAddOrUpdate(userId, group, feature);
                }
            });
        } else {
            doAddOrUpdate(userId, group, feature);
        }
    }

    private void addUserInTx(final String userId, final MTGroupUser groupUser, final int feature, boolean tx) {
        if (tx) {
            DBHelper.getInstance().runInTx(new Runnable() {
                @Override
                public void run() {
                    doAddOrUpdateUser(userId, groupUser, feature);
                }
            });
        } else {
            doAddOrUpdateUser(userId, groupUser, feature);
        }
    }

    private void doAddOrUpdate(String userId, MTGroup group, int feature) {
        Group entity = get(userId, group.getGroupId(), feature);

        if (null == entity) {
            entity = new Group();
        }

        ReflectionUtil.copyProperties(group, entity);
        entity.setGroupName(group.getGroupName());
        entity.setFeature(Long.valueOf(feature));
        entity.setUserId(userId);

        if (null != group.getGroupEnable()) {
            entity.setGroupEnable(Long.valueOf(group.getGroupEnable().getNumber()));
        }

        if (null != group.getGroupType()) {
            entity.setGroupType(Long.valueOf(group.getGroupType().getNumber()));
        }

        if (null != group.getValidateRule()) {
            entity.setValidateRule(Long.valueOf(group.getValidateRule().getNumber()));
        }

        dao.insertOrReplace(entity);

        for (MTGroupUser user : group.getUsers().values()) {
            doAddOrUpdateUser(userId, user, feature);
        }
    }

    private void doAddOrUpdateUser(String userId, MTGroupUser user, int feature) {
        GroupUser entity = userDao.queryBuilder().where(GroupUserDao.Properties.UserId.eq(userId), GroupUserDao.Properties.GroupId.eq(user.getGroupId()), GroupUserDao.Properties.GroupUserId.eq(user.getGroupUserId())
                , GroupUserDao.Properties.Feature.eq(feature)).unique();

        if (null == entity) {
            entity = new GroupUser();
        }

        ReflectionUtil.copyProperties(user, entity);
        entity.setUserId(userId);
        entity.setGroupId(user.getGroupId());
        entity.setFeature(Long.valueOf(feature));

        if (null != user.getNickNameSetting()) {
            entity.setNickNameSetting(Messages.Enable.ENABLE == user.getNickNameSetting());
        }

        if (null != user.getUserHeadType()) {
            entity.setUserHeadType(Long.valueOf(user.getUserHeadType().getNumber()));
        }

        if (null != user.getUserType()) {
            entity.setUserType(Long.valueOf(user.getUserType().getNumber()));
        }

        userDao.insertOrReplace(entity);
    }

    private void delete(String userId, Collection<String> groupIds, int feature) {
        dao.queryBuilder().where(GroupDao.Properties.UserId.eq(userId), GroupDao.Properties.Feature.eq(feature)
                , GroupDao.Properties.GroupId.in(groupIds)).buildDelete().executeDeleteWithoutDetachingEntities();

        userDao.queryBuilder().where(GroupUserDao.Properties.UserId.eq(userId), GroupUserDao.Properties.Feature.eq(feature)
                , GroupUserDao.Properties.GroupId.in(groupIds)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    private void deleteGroupUserIds(String userId, String groupId, Collection<String> groupUserIds, int feature) {
        userDao.queryBuilder().where(GroupUserDao.Properties.UserId.eq(userId), GroupUserDao.Properties.Feature.eq(feature)
                , GroupUserDao.Properties.GroupId.eq(groupId), GroupUserDao.Properties.UserId.in(groupUserIds)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    private List<String> getAllGroupIds(String userId, int feature) {
        List<Group> groups = dao.queryBuilder().where(GroupDao.Properties.UserId.eq(userId), GroupDao.Properties.Feature.eq(feature)).list();
        List<String> groupIds = new ArrayList<>();
        for (Group g : groups) {
            groupIds.add(g.getGroupId());
        }

        return groupIds;
    }

    private List<String> getAllGroupUserIds(String userId, String groupId, int feature) {
        List<GroupUser> groupUsers = userDao.queryBuilder().where(GroupUserDao.Properties.UserId.eq(userId)
                , GroupUserDao.Properties.GroupId.eq(groupId), GroupUserDao.Properties.Feature.eq(feature)).list();
        List<String> groupUserIds = new ArrayList<>();
        for (GroupUser u : groupUsers) {
            groupUserIds.add(u.getGroupUserId());
        }

        return groupUserIds;
    }
}
