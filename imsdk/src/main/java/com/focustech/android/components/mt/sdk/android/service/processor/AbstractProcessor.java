package com.focustech.android.components.mt.sdk.android.service.processor;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.FocusTechInterface;
import com.focustech.android.components.mt.sdk.android.IntentAction;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IAccountService;
import com.focustech.android.components.mt.sdk.android.db.IConversationService;
import com.focustech.android.components.mt.sdk.android.db.IFriendExtService;
import com.focustech.android.components.mt.sdk.android.db.IFriendGroupService;
import com.focustech.android.components.mt.sdk.android.db.IFriendRelationshipService;
import com.focustech.android.components.mt.sdk.android.db.IFriendService;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.ILastTimestampService;
import com.focustech.android.components.mt.sdk.android.db.IMessageService;
import com.focustech.android.components.mt.sdk.android.db.ISettingService;
import com.focustech.android.components.mt.sdk.android.db.ISystemNotifyService;
import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.CMDProcessor;
import com.focustech.android.components.mt.sdk.android.service.MessageService;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.keepalive.ServiceProtectUtils;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.communicate.Communication;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import io.netty.util.Timeout;

/**
 * 抽象实现
 *
 * @author zhangxu
 */
public abstract class AbstractProcessor<PARAM, RETURN, INNER> implements CMDProcessor<PARAM, RETURN, INNER>, AsyncContent.TimeoutHandler {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractProcessor.class);

    @Override
    public void onMessage(TMMessage message) throws Throwable {
    }

    @Override
    public RETURN request(PARAM data) {
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return true;
    }

    @Override
    public void internalCycle(INNER message) throws Throwable {

    }

    @Override
    public void timeout(String asyncKey, Object asyncContext, Timeout timeout) {
        doReconnect();

        AbstractAsyncContext context = (AbstractAsyncContext) asyncContext;

        if (null == context) {
            return;
        }

        try {
            if (getBizInvokeCallback() != null) {
                if (context.getOperation() == Operation.LOGIN) {
                    logger.info("timeout clean login control context");
                    AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                }
                getBizInvokeCallback().privateOperationTimeout(context.getOperation().name(), context.getOperationData());
            }
        } catch (RemoteException e) {
        }
    }

    protected void doReconnect() {
        // 重链请求
        CMD.REQ_RECONNECT.getProcessor().request(null);
    }

    protected AbstractAsyncContext getAsyncContent(TMMessage message) {
        return AsyncContent.getContent(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));
    }

    protected void operationComplete(TMMessage message) {
        AsyncContent.cleanContent(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));
    }

    protected void operationComplete(String asyncKey) {
        AsyncContent.cleanContent(asyncKey);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param operation
     */
    protected void addDefaultTimeoutProcess(String asyncKey, Operation operation) {
        addDefaultTimeoutProcess(asyncKey, operation, DEFAULT_TIMEOUT);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param operation
     */
    protected void addDefaultTimeoutProcess(String asyncKey, Operation operation, String data) {
        addDefaultTimeoutProcess(asyncKey, operation, data, DEFAULT_TIMEOUT);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param operation
     */
    protected void addDefaultTimeoutProcess(String asyncKey, final Operation operation, long timeout) {
        addDefaultTimeoutProcess(asyncKey, operation, "", timeout);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param operation
     */
    protected void addDefaultTimeoutProcess(String asyncKey, final Operation operation, final String data, long timeout) {
        addDefaultTimeoutProcess(asyncKey, new AbstractAsyncContext() {
            @Override
            public Operation getOperation() {
                return operation;
            }

            @Override
            public String getOperationData() {
                return data;
            }
        }, timeout);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param context
     */
    protected void addDefaultTimeoutProcess(String asyncKey, AbstractAsyncContext context) {
        addDefaultTimeoutProcess(asyncKey, context, DEFAULT_TIMEOUT);
    }

    /**
     * 增加默认超时处理配置
     *
     * @param asyncKey
     * @param context
     */
    protected void addDefaultTimeoutProcess(String asyncKey, AbstractAsyncContext context, long timeout) {
        AsyncContent.addContent(asyncKey, context, timeout, this);
    }

    protected IBizInvokeCallback getBizInvokeCallback() {
        return getMessageService().getBizInvokeCallback();
    }

    protected MessageService getMessageService() {
        return ContextHolder.getMessageService();
    }

    protected SessionManager getSessionManager() {
        return SessionManager.getInstance();
    }

    protected IAccountService getAccountService() {
        return DBHelper.getAccountService();
    }

    protected IFriendService getFriendService() {
        return DBHelper.getFriendService();
    }

    protected IFriendExtService getFriendExtService() {
        return DBHelper.getFriendExtService();
    }

    protected ISystemNotifyService getSystemNotifyService() {
        return DBHelper.getSystemNotifyService();
    }

    protected IFriendRelationshipService getFriendRelationshipService() {
        return DBHelper.getFriendRelationshipService();
    }

    protected ISettingService getSettingService() {
        return DBHelper.getSettingService();
    }

    protected IFriendGroupService getFriendGroupService() {
        return DBHelper.getFriendGroupService();
    }

    protected IGroupService getGroupService() {
        return DBHelper.getGroupService();
    }

    protected ILastTimestampService getLastTimestampService() {
        return DBHelper.getLastTimestampService();
    }

    protected IMessageService getDBMessageService() {
        return DBHelper.getMessageService();
    }

    protected IConversationService getConversationService() {
        return DBHelper.getConversationService();
    }

    protected void asyncExecute(Runnable task) {
        TaskUtil.execute(task);
    }

    protected void asyncExecute(long delay, TimeUnit unit, Runnable task) {
        TaskUtil.schedule(task, delay, unit);
    }

    protected void updateLastTimestamp(String userId, long type) {
        updateLastTimestamp(userId, null, type);
    }

    protected void updateLastTimestamp(String userId, String contactId, long type) {
        LastTimestamp lastTimestamp = new LastTimestamp();
        lastTimestamp.setTimestamp(NTPTime.now());
        lastTimestamp.setType(type);
        lastTimestamp.setContactId(contactId);
        lastTimestamp.setUserId(userId);
        getLastTimestampService().addOrUpdate(lastTimestamp);
    }

    protected SystemNotify addSystemNotify(String userId, CMD cmd, Messages.RecentContactType contactType, String contactId, String relatedId, byte[] data) {
        // 存消息
        SystemNotify sys = new SystemNotify();
        sys.setUserId(userId);
        sys.setCmd(cmd.getValue());
        if (null != contactType) {
            sys.setContactType(Long.valueOf(contactType.getNumber()));
        }
        sys.setContactId(contactId);
        sys.setRelatedId(relatedId);
        sys.setData(data);

        getSystemNotifyService().add(sys);

        return sys;
    }

    protected SystemNotify addSystemNotify(CMD cmd, Messages.RecentContactType contactType, String contactId, String relatedId, byte[] data) {
        return addSystemNotify(getSessionManager().getUserId(), cmd, contactType, contactId, relatedId, data);
    }

    protected SystemNotify addSystemNotify(CMD cmd, Messages.RecentContactType contactType, String contactId, byte[] data) {
        return addSystemNotify(cmd, contactType, contactId, null, data);
    }

    protected SystemNotify addSystemNotify(CMD cmd, byte[] data) {
        return addSystemNotify(cmd, null, null, null, data);
    }

    /**
     * @param communication 新定义的消息体
     */
    protected void addMessage(Communication communication) {
        if (null != communication) {
            communication.setUserId(getSessionManager().getUserId());
            communication.setProcessName(ServiceProtectUtils.getInstance().getProcessName(ContextHolder.getAndroidContext()));
            //如果主进程没挂，直接调用sdkcallback
            if (null != getBizInvokeCallback()) {
                String result = null;
                try {
                    result = getBizInvokeCallback().communicate(communication);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                logger.info("addMessage result is:" + result);
                if (StringUtils.isEmpty(result))
                    sendCommByBroadcast(communication);
            } else {
                sendCommByBroadcast(communication);
            }
        }
    }

    /**
     * 通过广播发送对象
     *
     * @param communication
     */
    private void sendCommByBroadcast(Communication communication) {
        Intent intent = new Intent();
        intent.setAction(IntentAction.FOCUSTECH_INTENT_MESSAGE_RECEIVCED);
        Bundle bundle = new Bundle();
        bundle.putParcelable(FocusTechInterface.MESSAGE_KEY, communication);
        intent.putExtras(bundle);
        ContextHolder.getAndroidContext().sendBroadcast(intent);
    }

}
