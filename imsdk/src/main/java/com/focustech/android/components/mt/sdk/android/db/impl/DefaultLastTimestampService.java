package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.ILastTimestampService;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestampDao;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

import java.util.List;

/**
 * @author zhangxu
 */
public class DefaultLastTimestampService implements ILastTimestampService {
    private static final String GLOBAL_USER_ID = "0";
    private static LastTimestampDao dao = DBHelper.getInstance().getLastTimestampDao();

    private static final ILastTimestampService INSTANCE = new DefaultLastTimestampService();

    public static ILastTimestampService getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveLastRegistryTime() {
        LastTimestamp timestamp = new LastTimestamp();
        timestamp.setUserId(GLOBAL_USER_ID);
        timestamp.setTimestamp(System.currentTimeMillis());
        timestamp.setType(LastTimestampType.REGISTRY);
        dao.insert(timestamp);
    }

    @Override
    public long getLastRegistryTime() {
        LastTimestamp old = dao.queryBuilder().where(LastTimestampDao.Properties.UserId.eq(GLOBAL_USER_ID), LastTimestampDao.Properties.Type.eq(LastTimestampType.REGISTRY)).unique();
        return null == old ? 0 : old.getTimestamp();
    }

    public void addOrUpdate(LastTimestamp lastTimestamp) {
        LastTimestamp old = get(lastTimestamp.getUserId(), lastTimestamp.getContactId(), lastTimestamp.getType());

        if (old != null) {
            ReflectionUtil.copyProperties(lastTimestamp, old);
        } else {
            old = lastTimestamp;
        }

        dao.insertOrReplace(old);
    }

    @Override
    public long getDataTimestamp(String userId, long type) {
        LastTimestamp timestamp = get(userId, type);
        return null == timestamp ? 0 : timestamp.getTimestamp();
    }

    @Override
    public long getDataTimestamp(String userId, String contactId, long type) {
        LastTimestamp timestamp = get(userId, contactId, type);
        return null == timestamp ? 0 : timestamp.getTimestamp();
    }

    @Override
    public List<LastTimestamp> getDataTimestampList(String userId, long... types) {
        return dao.queryBuilder().where(LastTimestampDao.Properties.UserId.eq(userId), LastTimestampDao.Properties.Type.in(types)).list();
    }

    private LastTimestamp get(String userId, long type) {
        return get(userId, null, type);
    }

    private LastTimestamp get(String userId, String contactId, long type) {
        List<LastTimestamp> timestamp = null;

        if (null == contactId) {
            timestamp = dao.queryBuilder().where(LastTimestampDao.Properties.UserId.eq(userId), LastTimestampDao.Properties.Type.eq(type)).orderDesc(LastTimestampDao.Properties.Timestamp).limit(1).list();
        } else {
            timestamp = dao.queryBuilder().where(LastTimestampDao.Properties.UserId.eq(userId), LastTimestampDao.Properties.ContactId.eq(contactId), LastTimestampDao.Properties.Type.eq(type)).orderDesc(LastTimestampDao.Properties.Timestamp).limit(1).list();
        }

        if (timestamp.size() > 0) {
            return timestamp.get(0);
        }

        return null;
    }
}
