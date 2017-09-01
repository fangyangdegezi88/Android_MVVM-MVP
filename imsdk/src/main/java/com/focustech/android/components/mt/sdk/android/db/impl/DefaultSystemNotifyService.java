package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.ISystemNotifyService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotifyDao;
import com.focustech.android.components.mt.sdk.util.NTPTime;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author zhangxu
 */
public class DefaultSystemNotifyService implements ISystemNotifyService {
    private static SystemNotifyDao dao = DBHelper.getInstance().getSystemNotifyDao();

    private static final ISystemNotifyService INSTANCE = new DefaultSystemNotifyService();

    public static ISystemNotifyService getInstance() {
        return INSTANCE;
    }


    @Override
    public void add(SystemNotify notify) {
        boolean processed = isProcessed(notify.getUserId(), notify.getCmd(), notify.getContactType(), notify.getContactId());
        long now = NTPTime.now();
        notify.setAddTime(now);
        notify.setUpdateTime(now);
        notify.setProcessed(processed);
        dao.insert(notify);
    }

    @Override
    public boolean isProcessed(String userId, String cmd) {
        return isProcessed(userId, cmd, null, null, null);
    }

    @Override
    public boolean isProcessed(String userId, String cmd, Long type, String contactId) {
        return isProcessed(userId, cmd, type, contactId, null);
    }

    @Override
    public boolean isProcessed(String userId, String cmd, Long type, String contactId, String relatedId) {
        QueryBuilder<SystemNotify> qb = dao.queryBuilder();
        qb.where(SystemNotifyDao.Properties.UserId.eq(userId)
                , SystemNotifyDao.Properties.Cmd.eq(cmd), SystemNotifyDao.Properties.Processed.eq(true));

        if (null != type && null != contactId && null != relatedId) {
            qb.and(SystemNotifyDao.Properties.ContactType.eq(type.longValue()), SystemNotifyDao.Properties.ContactId.eq(contactId), SystemNotifyDao.Properties.RelatedId.eq(relatedId));
        }

        if (null != type && null != contactId && null == relatedId) {
            qb.and(SystemNotifyDao.Properties.ContactType.eq(type.longValue()), SystemNotifyDao.Properties.ContactId.eq(contactId));
        }

        return qb.count() > 0;
    }

    @Override
    public void processed(String userId, String cmd) {
        processed(userId, cmd, null, null);
    }

    @Override
    public void processed(String userId, String cmd, Long type, String contactId) {
        processed(userId, cmd, type, contactId, null);
    }

    @Override
    public void processed(SystemNotify notify) {
        processed(notify.getUserId(), notify.getCmd(), notify.getContactType(), notify.getContactId(), notify.getRelatedId());
    }

    @Override
    public void processed(String userId, String cmd, Long type, String contactId, String relatedId) {
        QueryBuilder<SystemNotify> qb = dao.queryBuilder();
        qb.where(SystemNotifyDao.Properties.UserId.eq(userId)
                , SystemNotifyDao.Properties.Cmd.eq(cmd), SystemNotifyDao.Properties.Processed.eq(true));

        if (null != type && null != contactId && null != relatedId) {
            qb.and(SystemNotifyDao.Properties.ContactType.eq(type.longValue()), SystemNotifyDao.Properties.ContactId.eq(contactId), SystemNotifyDao.Properties.RelatedId.eq(relatedId));
        }

        if (null != type && null != contactId && null == relatedId) {
            qb.and(SystemNotifyDao.Properties.ContactType.eq(type.longValue()), SystemNotifyDao.Properties.ContactId.eq(contactId));
        }

        long now = NTPTime.now();

        List<SystemNotify> notifies = qb.list();

        for (SystemNotify notify : notifies) {
            notify.setUpdateTime(now);
            notify.setProcessed(true);
        }

        dao.updateInTx(notifies);
    }
}
