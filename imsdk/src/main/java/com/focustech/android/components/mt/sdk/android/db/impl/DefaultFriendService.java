package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IFriendService;
import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendDao;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author zhangxu
 */
public class DefaultFriendService implements IFriendService {
    private static FriendDao dao = DBHelper.getInstance().getFriendDao();

    private static final IFriendService INSTANCE = new DefaultFriendService();

    public static IFriendService getInstance() {
        return INSTANCE;
    }

    public void addOrUpdate(Friend friend) {
        Friend old = getFriend(friend.getUserId(), friend.getFriendUserId());

        if (old != null) {
            ReflectionUtil.copyProperties(friend, old);
        } else {
            old = friend;
        }

        dao.insertOrReplace(old);
    }

    @Override
    public boolean updateSignature(String userId, String friendUserId, String newSignature) {
        Friend friend = getFriend(userId, friendUserId);

        if (null != friend) {
            friend.setUserSignature(newSignature);
            update(friend);

            return true;
        }

        return false;
    }

    @Override
    public boolean updateNickName(String userId, String friendUserId, String newNickName) {
        Friend friend = getFriend(userId, friendUserId);

        if (null != friend) {
            friend.setUserNickName(newNickName);
            update(friend);

            return true;
        }

        return false;
    }

    @Override
    public void deleteFriend(String userId, String friendUserId, String friendGroupId) {
        dao.queryBuilder().where(FriendDao.Properties.UserId.eq(userId)
                , FriendDao.Properties.FriendUserId.eq(friendUserId)
                , FriendDao.Properties.FriendGroupId.eq(friendGroupId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public boolean updateHead(String userId, String friendUserId, long userHeadType, String userHeadId) {
        Friend friend = getFriend(userId, friendUserId);

        if (null != friend) {
            friend.setUserHeadType(userHeadType);
            friend.setUserHeadId(userHeadId);
            update(friend);
            return true;
        }

        return false;
    }

    @Override
    public List<Friend> getFriends(String userId, List<String> friendIds) {
        return null == friendIds || friendIds.size() == 0 ? new ArrayList<Friend>() : dao.queryBuilder().where(FriendDao.Properties.UserId.eq(userId), FriendDao.Properties.FriendUserId.in(friendIds)).list();
    }

    private Friend getFriend(String userId, String friendUserId) {
        return fillWhere(userId, friendUserId).unique();
    }

    private void update(Friend friend) {
        dao.update(friend);
    }

    private QueryBuilder<Friend> fillWhere(String userId, String friendUserId) {
        return dao.queryBuilder().where(FriendDao.Properties.UserId.eq(userId), FriendDao.Properties.FriendUserId.eq(friendUserId));
    }
}
