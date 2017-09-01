package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 消息
 *
 * @author zhangxu
 */
public interface IMessageService {
    /**
     * 新增一个聊天消息
     *
     * @param userId
     * @param data
     * @param check
     */
    void addMessage(String userId, MessageData data, boolean check);

    /**
     * 消息是否存在
     *
     * @param svrMsgId
     * @return
     */
    boolean exists(String svrMsgId);

    /**
     * 获取一个联系类型的指定时间区间的消息
     *
     * @param userId
     * @param toContactId
     * @param contactType
     * @param beginTimestamp
     * @param endTimestamp
     * @param count
     * @return
     */
    List<Message> getBetween(String userId, String toContactId, long contactType, long beginTimestamp, long endTimestamp, int count);

    /**
     * 获取指定类型的消息
     *
     * @param userId
     * @param before
     * @param count
     * @param type
     * @return
     */
    List<Message> getMessages(String userId, long contactType, long before, int count, Messages.MessageType type);

    /**
     * 获取指定联系类型的最新消息
     *
     * @param userId
     * @param toContactId
     * @param contactType
     * @return
     */
    Message getNewest(String userId, String toContactId, long contactType);

    /**
     * 删除
     *
     * @param svrMsgId
     */
    void delete(String svrMsgId);

    /**
     * 删除
     *
     * @param userId
     * @param type
     */
    void delete(String userId, int type);
}
