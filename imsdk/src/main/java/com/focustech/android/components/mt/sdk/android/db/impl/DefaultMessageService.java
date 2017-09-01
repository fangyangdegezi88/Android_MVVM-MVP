package com.focustech.android.components.mt.sdk.android.db.impl;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IMessageService;
import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.android.db.gen.MessageDao;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * @author zhangxu
 */
public class DefaultMessageService implements IMessageService {
    private static final IMessageService INSTANCE = new DefaultMessageService();
    private static MessageDao dao = DBHelper.getInstance().getMessageDao();

    public static IMessageService getInstance() {
        return INSTANCE;
    }

    @Override
    public void addMessage(String userId, MessageData data, boolean check) {
        if (check && exists(data.getSvrMsgId())) {
            return;
        }

        Message message = new Message();
        ReflectionUtil.copyProperties(data, message);

        message.setUserId(userId);
        message.setSendStatus(data.getSendStatus().getValue());
        message.setStatus(Long.valueOf(Messages.Enable.ENABLE_VALUE));
        message.setMsgType(data.getMsgType().getNumber());

        if (null != data.getMsgMeta()) {
            message.setMsgMeta(JSONObject.toJSONString(data.getMsgMeta()));
        }

        dao.insert(message);
    }

    @Override
    public boolean exists(String svrMsgId) {
        return dao.queryBuilder().where(MessageDao.Properties.SvrMsgId.eq(svrMsgId)).count() > 0;
    }

    @Override
    public List<Message> getBetween(String userId, String contactId, long contactType, long beginTimestamp, long endTimestamp, int count) {
        QueryBuilder<Message> qb = dao.queryBuilder();
        WhereCondition user = qb.or(qb.and(MessageDao.Properties.ContactId.eq(contactId), MessageDao.Properties.ContactType.eq(contactType)),
                qb.and(MessageDao.Properties.ContactId.eq(userId), MessageDao.Properties.ContactType.eq(contactType)));

        return qb.where(MessageDao.Properties.UserId.eq(userId)
                , MessageDao.Properties.Status.eq(Messages.Enable.ENABLE_VALUE)
                , MessageDao.Properties.Timestamp.between(beginTimestamp, endTimestamp)
                , MessageDao.Properties.MsgType.notIn(getExcludeTypes())
                , user)
                .orderDesc(MessageDao.Properties.Timestamp).limit(count).list();
    }

    @Override
    public List<Message> getMessages(String userId, long contactType, long before, int count, Messages.MessageType type) {
        QueryBuilder<Message> qb = dao.queryBuilder();
        return qb.where(MessageDao.Properties.UserId.eq(userId)
                , MessageDao.Properties.ContactType.eq(contactType)
                , MessageDao.Properties.Status.eq(Messages.Enable.ENABLE_VALUE)
                , MessageDao.Properties.MsgType.eq(type.getNumber())
                , MessageDao.Properties.Timestamp.lt(before)).orderDesc(MessageDao.Properties.Timestamp).limit(count).list();
    }

    @Override
    public Message getNewest(String userId, String contactId, long contactType) {
        QueryBuilder<Message> qb = dao.queryBuilder();
        WhereCondition user = qb.or(qb.and(MessageDao.Properties.ContactId.eq(contactId), MessageDao.Properties.ContactType.eq(contactType)),
                qb.and(MessageDao.Properties.ContactId.eq(userId), MessageDao.Properties.ContactType.eq(contactType)));


        qb.where(MessageDao.Properties.UserId.eq(userId)
                , MessageDao.Properties.Status.eq(Messages.Enable.ENABLE_VALUE)
                , MessageDao.Properties.MsgType.notIn(getExcludeTypes())
                , user);

        List<Message> messages = qb.orderDesc(MessageDao.Properties.Timestamp).limit(1).list();

        return messages.size() > 0 ? messages.get(0) : null;
    }

    @Override
    public void delete(String svrMsgId) {
        Message old = dao.queryBuilder().where(MessageDao.Properties.SvrMsgId.eq(svrMsgId)).unique();

        if (old != null) {
            old.setStatus(Long.valueOf(Messages.Enable.DISABLE_VALUE));
            dao.update(old);
        }
    }

    @Override
    public void delete(String userId, int msgType) {
        dao.queryBuilder().where(MessageDao.Properties.UserId.eq(userId)
                , MessageDao.Properties.MsgType.eq(msgType))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    private List<Long> getExcludeTypes() {
        Messages.MessageType[] types = MTRuntime.getMsgTypeExt();
        List<Long> excludeTypes = new ArrayList<>();

        for (Messages.MessageType type : types) {
            excludeTypes.add(Long.valueOf(type.getNumber()));
        }
        return excludeTypes;
    }
}
