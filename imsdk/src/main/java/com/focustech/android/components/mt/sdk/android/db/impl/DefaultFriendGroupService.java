package com.focustech.android.components.mt.sdk.android.db.impl;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IFriendGroupService;
import com.focustech.android.components.mt.sdk.android.db.IFriendRelationshipService;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendGroupDao;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;
import com.focustech.android.components.mt.sdk.util.BeanConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhangxu
 */
public class DefaultFriendGroupService implements IFriendGroupService {
    private static Logger logger = LoggerFactory.getLogger(DefaultFriendGroupService.class);
    private static FriendGroupDao dao = DBHelper.getInstance().getFriendGroupDao();
    private static IFriendRelationshipService relationshipService = DBHelper.getFriendRelationshipService();

    private static final IFriendGroupService INSTANCE = new DefaultFriendGroupService();

    public static IFriendGroupService getInstance() {
        return INSTANCE;
    }

    public List<com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup> getAll(String userId) {
        return dao.queryBuilder().where(FriendGroupDao.Properties.UserId.eq(userId)).list();
    }

    @Override
    public void delete(String userId, String friendGroupId, String moveToFriendGroupId) {
        dao.queryBuilder().where(FriendGroupDao.Properties.UserId.eq(userId), FriendGroupDao.Properties.FriendGroupId.eq(friendGroupId)).buildDelete().executeDeleteWithoutDetachingEntities();
        relationshipService.moveTo(userId, friendGroupId, moveToFriendGroupId);
    }

    @Override
    public void clear(String userId) {
        clear(userId, true);
    }

    public void reset(final FriendGroups data) {
        final String userId = SessionManager.getInstance().getUserId();

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.UPDATE, "update friend group. {}, {}"), userId, JSONObject.toJSONString(data));
        }

        DBHelper.getInstance().runInTx(new Runnable() {
            @Override
            public void run() {
                try {
                    doChange();

                    if (logger.isDebugEnabled()) {
                        logger.debug(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.UPDATE, "update friend group successful."));
                    }
                } catch (Throwable e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.UPDATE, ""), e);
                    }

                    throw e;
                }
            }

            private void doChange() {
                if (logger.isDebugEnabled()) {
                    logger.debug(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.DELETE, "reset friend groups. {}"), userId);
                }

                clear(userId, false);
                resetFriendGroups();
                resetRelationship();
            }

            private void resetRelationship() {
                for (FriendGroup d : data.getFriendGroups()) {
                    for (FriendBase friendBase : d.getFriends()) {
                        relationshipService.add(BeanConverter.toFriendRelationship(userId, friendBase));

                        if (logger.isDebugEnabled()) {
                            logger.debug(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.INSERT, "create friend relationship. {}, {}"), userId, JSONObject.toJSONString(friendBase));
                        }
                    }
                }
            }

            private void resetFriendGroups() {
                for (com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup friendGroup : data.toPOList(userId)) {
                    dao.insert(friendGroup);

                    if (logger.isDebugEnabled()) {
                        logger.debug(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.INSERT, "create friend groups. {}, {}"), userId, JSONObject.toJSONString(friendGroup));
                    }
                }
            }
        });
    }

    private void clear(final String userId, boolean useTx) {
        if (useTx) {
            DBHelper.getInstance().runInTx(new Runnable() {
                @Override
                public void run() {
                    doClear(userId);
                }
            });
        } else {
            doClear(userId);
        }
    }

    private void doClear(String userId) {
        dao.queryBuilder().where(FriendGroupDao.Properties.UserId.eq(userId)).buildDelete().executeDeleteWithoutDetachingEntities();
        relationshipService.clear(userId);
    }
}
