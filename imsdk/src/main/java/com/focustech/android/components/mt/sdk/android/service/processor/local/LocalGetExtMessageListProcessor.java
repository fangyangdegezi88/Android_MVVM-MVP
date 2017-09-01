package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.android.service.pojo.GetExtMessageListData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉取本地最新的消息列表
 *
 * @author zhangxu
 */
public class LocalGetExtMessageListProcessor extends AbstractMessageProcessor<GetExtMessageListData, Void, Void> {
    @Override
    public Void request(final GetExtMessageListData data) {
        final String userId = getSessionManager().getUserId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                List<Message> messages = getDBMessageService().getMessages(userId, Messages.RecentContactType.PERSON.getNumber(), data.getBefore(), data.getCount(), data.getType());

                List<MessageData> value = new ArrayList<>();

                for (Message m : messages) {
                    value.add(new MessageData(m, false));
                }

                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateLocalExtMessageList(userId, data.getType().name(), JSONObject.toJSONString(value));
                    }
                } catch (RemoteException e) {
                    // TODO 日志
                }
            }
        });

        return null;
    }
}
