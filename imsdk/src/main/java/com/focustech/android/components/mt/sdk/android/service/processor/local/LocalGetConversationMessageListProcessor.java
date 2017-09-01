package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.android.service.pojo.GetConversationMessageListData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.NTPTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉取本地会话列表
 * <p/>
 * ----------------------------------------------------------------------
 * | t1 : 已经收到的最后一个消息的时间戳
 * | t2 : 当前登录的时间戳
 * |
 * |                                    0           t1             t2
 * |a. t1 ~ t2之间没有消息              begin                        end
 * |b. t1 ~ t2之间有没有fetch完的消息                 begin           end
 * |
 * ----------------------------------------------------------------------
 */
public class LocalGetConversationMessageListProcessor extends AbstractMessageProcessor<GetConversationMessageListData, Void, Void> {
    @Override
    public Void request(final GetConversationMessageListData data) {
        if (0 == data.getEarliestTimestamp()) {
            data.setEarliestTimestamp(NTPTime.now());
        }

        final String userId = getSessionManager().getUserId();

        // 不设置begin
        final long beginTimestamp;

        final boolean inFetching = isTimestampInFetchingRange(data.getLastTimestampType(), data.getContactId(), data.getEarliestTimestamp());

        // 本地消息没有同步完成，并且最老的时间戳在同步消息范围内，那么从fromTimestamp开始
        if (inFetching) {
            beginTimestamp = getFromTimestamp(data.getLastTimestampType(), data.getContactId());
        } else {
            beginTimestamp = 0L;
        }

        // 拉取 beginTimestamp - data.getEarliestTimestamp() 倒序排列的 data.getCount() 条消息
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                List<Message> messages = getDBMessageService().getBetween(userId, data.getContactId(), data.getContactType().getNumber(), beginTimestamp, data.getEarliestTimestamp(), data.getCount());

                List<MessageData> value = new ArrayList<>();

                for (Message m : messages) {
                    value.add(new MessageData(m, true));
                }

                if (inFetching && value.isEmpty()) {
                    try {
                        if (getBizInvokeCallback() != null) {
                            getBizInvokeCallback().privateLocalConversationMessageListOnFetching(data.getContactType().name(), data.getContactId());
                        }
                    } catch (RemoteException e) {
                    }
                } else {
                    try {
                        if (getBizInvokeCallback() != null) {
                            getBizInvokeCallback().privateLocalConversationMessageList(data.getContactType().name(), data.getContactId(), JSONObject.toJSONString(value));
                        }
                    } catch (RemoteException e) {
                    }
                }
            }
        });

        return null;
    }
}
