package com.focustech.android.components.mt.sdk.android.db.impl;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IConversationService;
import com.focustech.android.components.mt.sdk.android.db.gen.Conversation;
import com.focustech.android.components.mt.sdk.android.db.gen.ConversationDao;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationSetting;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author zhangxu
 */
public class DefaultConversationService implements IConversationService {
    private static final IConversationService INSTANCE = new DefaultConversationService();

    private static ConversationDao dao = DBHelper.getInstance().getConversationDao();

    public static IConversationService getInstance() {
        return INSTANCE;
    }

    @Override
    public void addConversation(String userId, Messages.RecentContactType type, String contactId, String contactName) {
        if (!exists(userId, type, contactId)) {
            Conversation conversation = new Conversation();
            conversation.setUserId(userId);
            conversation.setContactType(type.getNumber());
            conversation.setContactId(contactId);
            conversation.setTimestamp(NTPTime.now());
            conversation.setContactName(contactName);
            dao.insert(conversation);
        }
    }

    @Override
    public void deleteConversation(String userId, Messages.RecentContactType type, String contactId) {
        getWhereCondition(userId, type, contactId).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void clean(String userId) {
        dao.queryBuilder().where(ConversationDao.Properties.UserId.eq(userId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<Conversation> getAll(String userId) {
        return dao.queryBuilder().where(ConversationDao.Properties.UserId.eq(userId)).list();
    }

    @Override
    public boolean exists(String userId, Messages.RecentContactType type, String contactId) {
        return getWhereCondition(userId, type, contactId).count() > 0;
    }

    @Override
    public void updateSetting(String userId, ConversationSetting setting) {
        Conversation conversation = getWhereCondition(userId, setting.getType(), setting.getContactId()).unique();

        if (null != conversation) {
            conversation.setSetting(JSONObject.toJSONString(setting));
            dao.update(conversation);
        }
    }

    private QueryBuilder<Conversation> getWhereCondition(String userId, Messages.RecentContactType type, String contactId) {
        return dao.queryBuilder().where(ConversationDao.Properties.UserId.eq(userId)
                , ConversationDao.Properties.ContactType.eq(type.getNumber())
                , ConversationDao.Properties.ContactId.eq(contactId));
    }
}
