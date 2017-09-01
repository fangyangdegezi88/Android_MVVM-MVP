package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Conversation;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationSetting;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 聊天会话
 *
 * @author zhangxu
 */
public interface IConversationService {
    void addConversation(String userId, Messages.RecentContactType type, String contactId, String contactName);

    void deleteConversation(String userId, Messages.RecentContactType type, String contactId);

    void clean(String userId);

    List<Conversation> getAll(String userId);

    boolean exists(String userId, Messages.RecentContactType type, String contactId);

    void updateSetting(String userId, ConversationSetting setting);
}
