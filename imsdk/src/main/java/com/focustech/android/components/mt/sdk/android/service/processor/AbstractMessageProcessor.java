package com.focustech.android.components.mt.sdk.android.service.processor;

import android.os.RemoteException;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.db.gen.Message;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncMessageContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractMessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.FetchData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationData;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.communicate.Communication;
import com.focustech.android.components.mt.sdk.communicate.CommunicationContent;
import com.focustech.android.components.mt.sdk.communicate.CommunicationType;
import com.focustech.android.components.mt.sdk.util.BeanConverter;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.netty.util.Timeout;

/**
 * 消息处理抽象实现
 *
 * @author zhangxu
 */
public abstract class AbstractMessageProcessor<PARAM, RETURN, INNER> extends AbstractHttpOperationSupportProcessor<PARAM, RETURN, INNER> {
    protected static final String ATTR_TIMESTAMP_FROM = "msg.fetch.from";
    protected static final String ATTR_TIMESTAMP_TO = "msg.fetch.to";
    protected static final String ATTR_MESSAGE_PROMPT = "msg.prompt";
    protected static final String ATTR_MESSAGE_LAST_CHAT = "msg.last.chat";
    protected static final Set<String> PROCESSING_MSGS = new HashSet<>();

    @Override
    public void timeout(String asyncKey, Object asyncContext, Timeout timeout) {
        doReconnect();

        AbstractAsyncContext context = (AbstractAsyncContext) asyncContext;

        if (null == context) {
            return;
        }

        if (Operation.SEND_MESSAGE == context.getOperation()) {
            AsyncMessageContext c = ((AsyncMessageContext) context);
            MessageData data = c.getData();
            data.setSendStatus(AbstractMessageData.Status.SEND_FAIL);
            asyncSaveMessage(getSessionManager().getUserId(), data, false);
            try {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateMessageSendFail(data.getContactId(), (int) data.getContactType(), data.getLocalMessageId());
                }
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * 处理群消息
     *
     * @param msg
     * @throws RemoteException
     */
    protected void processGroupMessage(Messages.GroupMessage msg, boolean notice) throws RemoteException {
        MessageData data = BeanConverter.toMessageData(msg, true);

        data.setPrompt(isPrompt());
        data.setRead(isRead(LastTimestampType.READ_GROUP, msg.getGroupId(), msg.getTimestamp()));
        data.setContactName(msg.getGroupName());

        // 查看联系人设置中的提醒设置
        if (data.isPrompt()) {
            // 是自己的消息，不提醒
            if (data.getFromUserId().equals(getSessionManager().getUserId())) {
                data.setPrompt(false);
            } else {
                // TODO 设置提醒
            }
        }


        if (!PROCESSING_MSGS.contains(data.getSvrMsgId()) && !getDBMessageService().exists(data.getSvrMsgId())) {
            asyncSaveMessage(getSessionManager().getUserId(), data, false);

            createConversation(Messages.RecentContactType.GROUP, data, notice);

            if (notice) {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateMessage(getSessionManager().getUserId(), JSONObject.toJSONString(data));
                }
            }
        }
    }

    /**
     * 处理群消息
     *
     * @param msg
     * @throws RemoteException
     */
    protected void processDiscussionMessage(Messages.DiscussionMessage msg, boolean notice) throws RemoteException {
        MessageData data = BeanConverter.toMessageData(msg, true);

        data.setPrompt(isPrompt());
        data.setRead(isRead(LastTimestampType.READ_DISCUSSION, msg.getDiscussionId(), msg.getTimestamp()));
        data.setContactName(msg.getDiscussionName());

        // 查看联系人设置中的提醒设置
        if (data.isPrompt()) {
            // 是自己的消息，不提醒
            if (data.getFromUserId().equals(getSessionManager().getUserId())) {
                data.setPrompt(false);
            } else {
                // TODO 设置提醒
            }
        }


        if (!PROCESSING_MSGS.contains(data.getSvrMsgId()) && !getDBMessageService().exists(data.getSvrMsgId())) {
            asyncSaveMessage(getSessionManager().getUserId(), data, false);

            createConversation(Messages.RecentContactType.DISCUSSION, data, notice);

            if (notice) {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateMessage(getSessionManager().getUserId(), JSONObject.toJSONString(data));
                }
            }
        }
    }


    /**
     * 处理联系人消息
     *
     * @param msg
     * @throws RemoteException
     */
    protected void processFriendMessage(Messages.Message msg, boolean notice) throws RemoteException {
        boolean isExt = ArrayUtils.contains(MTRuntime.getMsgTypeExt(), msg.getMsgType());

        MessageData data = BeanConverter.toMessageData(msg, !isExt);

        if (null != msg.getToUserId() && msg.getToUserId().length() > 0) {
            data.setFromUserId(msg.getUserId());
            data.setContactId(msg.getUserId());
        } else {
            data.setContactId(msg.getUserId());
            data.setFromUserId(getSessionManager().getUserId());
        }

        if (!isExt) {
            data.setPrompt(isPrompt());
            data.setRead(isRead(LastTimestampType.READ_FRIEND, msg.getUserId(), msg.getTimestamp()));

            // 查看联系人设置中的提醒设置
            if (data.isPrompt()) {
                // 是自己的消息，不提醒
                if (data.getFromUserId().equals(getSessionManager().getUserId())) {
                    data.setPrompt(false);
                } else {
                    // 设置为不提醒的消息
                    FriendBase friendBase = getSessionManager().getCurrent().getFriendGroups().getFriend(data.getFromUserId());
                    data.setPrompt(null == friendBase ? data.isPrompt() : !friendBase.isNoDisturb());
                }
            }
        }
        if (!PROCESSING_MSGS.contains(data.getSvrMsgId()) && !getDBMessageService().exists(data.getSvrMsgId())) {
            asyncSaveMessage(getSessionManager().getUserId(), data, false);
            if (isExt) {
                /* 列表重构以后，不需要再处理拉取的离线消息（通知作业）。
                 * notice==true照常通知，notice==false那么除通知作业以外的类型进行通知。
                 * */
                if (notice || (data.getMsgType() != Messages.MessageType.NOTICE && data.getMsgType() != Messages.MessageType.WORK)) {
                    addMessage(new Communication(CommunicationType.WORK_NOTICE, new CommunicationContent(data, NTPTime.now())));
                }
            } else {
                createConversation(Messages.RecentContactType.PERSON, data, notice);
                if (notice) {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateMessage(getSessionManager().getUserId(), JSONObject.toJSONString(data));
                    }
                }
            }
        }
    }

    protected void asyncSaveMessage(final String userId, final MessageData data, final boolean check) {
        if (null != data.getSvrMsgId() && PROCESSING_MSGS.contains(data.getSvrMsgId())) {
            return;
        }

        if (null != data.getSvrMsgId()) {
            PROCESSING_MSGS.add(data.getSvrMsgId());
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    getDBMessageService().addMessage(userId, data, check);
                } finally {
                    if (null != data.getSvrMsgId()) {
                        PROCESSING_MSGS.remove(data.getSvrMsgId());
                    }
                }
            }
        });
    }

    protected void continueFetch(long lastTimestampType, Messages.RecentContactType fetchType) {
        continueFetch(lastTimestampType, null, fetchType);
    }

    protected void continueFetch(long lastTimestampType, String contactId, Messages.RecentContactType fetchType) {
        FetchData data = new FetchData();
        data.setFromTimestamp(getFromTimestamp(lastTimestampType, contactId));
        data.setToTimestamp(getToTimestamp(lastTimestampType, contactId));
        data.setType(fetchType);

        CMD.REQ_FETCH_MESSAGE.getProcessor().request(data);
    }

    /**
     * 创建本地聊天会话
     *
     * @param type
     * @param notify
     */
    protected void createConversation(final Messages.RecentContactType type, AbstractSystemNotify notify, boolean notice) {
        String userId = getSessionManager().getUserId();

        if (notice) {
            // 通知前台有新会话
            ConversationData conversationData = new ConversationData();
            conversationData.setType(type);
            conversationData.setContactId(userId);
            conversationData.setNotify(JSONObject.toJSONString(notify));
            conversationData.setCmd(notify.getCMD());
            conversationData.setTimestamp(notify.getTimestamp());

            try {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateLocalConversation(JSONObject.toJSONString(conversationData));
                }
            } catch (RemoteException e) {
                Log.e("error", "error", e);
            }
        }
    }

    /**
     * 创建本地聊天会话
     *
     * @param type
     * @param data
     */
    protected void createConversation(final Messages.RecentContactType type, MessageData data, boolean notice) {
        final String userId = getSessionManager().getUserId();

        final String contactId;
        final String contactName;

        if (type == Messages.RecentContactType.PERSON) {
            contactId = userId.equals(data.getFromUserId()) ? data.getContactId() : data.getFromUserId();
            contactName = data.getFromUserName();
        } else {
            contactId = data.getContactId();
            String temp = getSessionManager().getCurrent().getGroup(data.getContactId(), data.getContactType()).getDisplayName();

            if (null == temp || temp.trim().length() == 0) {
                temp = data.getContactName();
            }

            contactName = temp;
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getConversationService().addConversation(userId, type, contactId, contactName);
            }
        });

        if (notice) {
            // 通知前台有新会话
            ConversationData conversationData = new ConversationData();
            conversationData.setType(type);
            conversationData.setContactId(contactId);
            conversationData.setContactName(contactName);
            conversationData.setMsg(data);
            conversationData.setTimestamp(data.getTimestamp());

            try {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateLocalConversation(JSONObject.toJSONString(conversationData));
                }
            } catch (RemoteException e) {
                Log.e("error", "error", e);
            }
        }
    }

    protected long getFromTimestamp(long type) {
        return getFromTimestamp(type, null);
    }

    protected long getFromTimestamp(long type, String contactId) {
        String attr = ATTR_TIMESTAMP_FROM + type + contactId;

        if (!getSessionManager().hasAttr(attr)) {
            long value = getLastTimestampService().getDataTimestamp(getSessionManager().getUserId(), LastTimestampType.MESSAGE_FRIEND);
            getSessionManager().addAttr(attr, 0 == value ? DateUtils.addDays(DateUtils.truncate(new Date(NTPTime.now()), Calendar.DATE), -MTRuntime.getMTFetchDays()).getTime() : value);
        }

        return getSessionManager().getAttr(attr);
    }

    protected long getToTimestamp(long type) {
        return getToTimestamp(type, null);
    }

    protected long getToTimestamp(long type, String contactId) {
        String attr = ATTR_TIMESTAMP_TO + type + contactId;

        long value = NTPTime.now();

        if (getSessionManager().hasAttr(attr)) {
            value = getSessionManager().getAttr(attr);
        }

        return value;
    }

    protected void resetToTimestamp(long type, long toTimestamp) {
        resetToTimestamp(type, null, toTimestamp);
    }

    protected void resetToTimestamp(long type, String contactId, long toTimestamp) {
        String attr = ATTR_TIMESTAMP_TO + type + contactId;
        getSessionManager().addAttr(attr, toTimestamp);
    }

    protected void completeFetch(long type) {
        completeFetch(type, null);
    }

    protected void completeFetch(final long type, final String contactId) {
        String fromAttr = ATTR_TIMESTAMP_FROM + type + contactId;
        String toAttr = ATTR_TIMESTAMP_TO + type + contactId;

        getSessionManager().addAttr(fromAttr, NTPTime.now());
        getSessionManager().removeAttr(toAttr);

        // 放入线程池，等待前置任务做完
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                // TODO 日志

                // 通知前台
                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateHistoryMessageSyncComplete(LastTimestampType.MESSAGE_MAPPING_REVERSE.get(type).name(), contactId);
                    }
                } catch (RemoteException e) {
                    // TODO
                }
            }
        });
    }

    protected boolean isNotComplete(long type, String contactId) {
        return !isComplete(type, contactId);
    }

    protected boolean isComplete(long type, String contactId) {
        String fromAttr = ATTR_TIMESTAMP_FROM + type + contactId;
        return getSessionManager().hasAttr(fromAttr);
    }

    /**
     * 正在fetch中，并且没有fetch完毕，检查 lastTimeStamp 是否是fetch起始时间之后的时间戳，如果是，那么就是获取没有fetch完毕的消息内容
     *
     * @param type
     * @param contactId
     * @param lastTimeStamp
     * @return
     */
    protected boolean isTimestampInFetchingRange(long type, String contactId, long lastTimeStamp) {
        return isNotComplete(type, contactId) && lastTimeStamp >= getFromTimestamp(type, contactId);
    }

    protected void resetPrompt(boolean prompt) {
        getSessionManager().addAttr(ATTR_MESSAGE_PROMPT, prompt);
    }

    protected boolean isPrompt() {
        if (getSessionManager().hasAttr(ATTR_MESSAGE_PROMPT)) {
            return getSessionManager().getAttr(ATTR_MESSAGE_PROMPT);
        }

        return false;
    }

    protected void loadLastChatTimeFromDB() {
        final String userId = getSessionManager().getUserId();

        // 异步加载最后阅读时间
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                List<LastTimestamp> timestamps = getLastTimestampService().getDataTimestampList(userId, LastTimestampType.READ_FRIEND, LastTimestampType.READ_GROUP, LastTimestampType.READ_DISCUSSION);

                for (LastTimestamp timestamp : timestamps) {
                    resetLastChatTime(timestamp);
                }
            }
        });
    }

    protected void updateLastChatTimestampToDB(final LastTimestamp data) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getLastTimestampService().addOrUpdate(data);
            }
        });
    }

    protected void updateLastChatTimestampToDB(final List<LastTimestamp> data) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for (LastTimestamp lastTimestamp : data) {
                            getLastTimestampService().addOrUpdate(lastTimestamp);
                        }
                    }
                });
            }
        });
    }

    protected void resetLastChatTime(LastTimestamp timestamp) {
        resetLastChatTime(timestamp.getType(), timestamp.getContactId(), timestamp.getTimestamp());
    }

    protected void resetLastChatTime(Messages.LastChatTime lastChatTime) {
        resetLastChatTime(LastTimestampType.READ_MAPPING.get(lastChatTime.getType()), lastChatTime.getTargetId(), lastChatTime.getTimestamp());
    }

    protected void resetLastChatTime(long type, String contactId, long timestamp) {
        String key = ATTR_MESSAGE_LAST_CHAT + type + contactId;

        getSessionManager().addAttr(key, timestamp);
    }

    protected boolean isRead(long type, String contactId, long timestamp) {
        String key = ATTR_MESSAGE_LAST_CHAT + type + contactId;

        long lastChatTimestamp = 0;

        if (getSessionManager().hasAttr(key)) {
            lastChatTimestamp = getSessionManager().getAttr(key);
        }

        return lastChatTimestamp > timestamp;
    }

    protected boolean fillNewestMsg(ConversationData data, String userId, String contactId, long contactType) {
        Message msg = getDBMessageService().getNewest(userId, contactId, contactType);
        if (null != msg) {
            data.setMsg(new MessageData(msg, true));
            data.setTimestamp(msg.getTimestamp());

            return true;
        }

        return false;
    }

}
