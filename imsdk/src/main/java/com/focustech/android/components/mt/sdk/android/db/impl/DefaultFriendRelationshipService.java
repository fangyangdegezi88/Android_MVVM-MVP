package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IFriendRelationshipService;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendRelationship;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendRelationshipDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxu on 2015/5/6.
 */
public class DefaultFriendRelationshipService implements IFriendRelationshipService {
    private static FriendRelationshipDao dao = DBHelper.getInstance().getFriendRelationshipDao();

    private static final IFriendRelationshipService INSTANCE = new DefaultFriendRelationshipService();

    public static IFriendRelationshipService getInstance() {
        return INSTANCE;
    }

    public void clear(String userId) {
        dao.queryBuilder().where(FriendRelationshipDao.Properties.UserId.eq(userId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void moveTo(String userId, String fromFriendGroupId, String toFriendGroupId) {
        dao.getDatabase().execSQL("update FRIEND_RELATIONSHIP set friendGroupId=? where userId=? and friendGroupId=?", new Object[]{userId, toFriendGroupId, fromFriendGroupId});
    }

    @Override
    public void add(FriendRelationship relationship) {
        dao.insert(relationship);
    }

    @Override
    public Map<String, List<String>> getFriendRelations(String userId, List<String> friendGroupIds) {
        List<FriendRelationship> relationships = dao.queryBuilder()
                .where(FriendRelationshipDao.Properties.UserId.eq(userId)
                        , FriendRelationshipDao.Properties.FriendGroupId.in(friendGroupIds))
                .list();

        Map<String, List<String>> value = new HashMap<>();

        for (FriendRelationship relationship : relationships) {
            String friendGroupId = relationship.getFriendGroupId();

            if (!value.containsKey(friendGroupId)) {
                value.put(friendGroupId, new ArrayList<String>());
            }

            value.get(friendGroupId).add(relationship.getFriendUserId());
        }

        return value;
    }
}
