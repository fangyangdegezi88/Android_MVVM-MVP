package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Conversation;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉取本地会话列表
 */
public class LocalGetConversationListProcessor extends AbstractMessageProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                List<Conversation> conversations = getConversationService().getAll(userId);
                List<ConversationData> list = new ArrayList<>();
                ConversationData tmp = null;

                for (Conversation c : conversations) {
                    tmp = new ConversationData(c);

                    if (fillNewestMsg(tmp, c.getUserId(), c.getContactId(), c.getContactType())) {
                        list.add(tmp);
                    }
                }

                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateLocalConversationList(JSONObject.toJSONString(list));
                    }
                } catch (RemoteException e) {
                    // TODO 日志
                }
            }
        });

        return null;
    }
}
