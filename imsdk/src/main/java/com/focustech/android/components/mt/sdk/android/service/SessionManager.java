package com.focustech.android.components.mt.sdk.android.service;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncLoginContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.core.net.ConnectionListener;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Head;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 登录会话管理
 *
 * @author zhangxu
 */
public class SessionManager implements ConnectionListener {
    private static Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final SessionManager instance = new SessionManager();
    private MTModel current;
    private Map<String, Object> attrs = new HashMap<>();
    private AtomicBoolean isKickOut = new AtomicBoolean(false); //是否被踢下线
    private AtomicBoolean isOverDue = new AtomicBoolean(false); //是否登录逾期
    private AtomicBoolean isUserOrPsdError = new AtomicBoolean(false);  //账号密码错误

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void clear() {
        attrs.clear();
        current = null;
    }

    public MTModel getCurrent() {
        return current;
    }

    public void setCurrent(MTModel current) {
        this.current = current;
    }

    public void newSession(String userId, String token, String platformData, Head.TMHeadMessage head) {
        AsyncLoginContext context = AsyncContent.getContent(IDGenerator.getKeyUseCliSeqId(head.getCliSeqId()));

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SESSION, LogFormat.Operation.CREATE, "{}, {}"), userId, context);
        }

        if (null != context) {
            current = new MTModel(userId);
            current.setLoginData(context.getData());
            current.setChannelId(head.getChannelId());
            current.setToken(token);
            current.setPlatformData(platformData);
        } else {
            current = new MTModel(userId);
            current.setChannelId(head.getChannelId());
            current.setToken(token);
            current.setPlatformData(platformData);
        }

    }

    /**
     * 缓存session
     */
    private void saveSession() {
        if (ContextHolder.getAndroidContext() != null) {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            sharedPrefLoginInfo.saveString(MTModel.class.getSimpleName(), JSONObject.toJSONString(current));
        }
    }

    public void completeSelfUserBase(Account account) {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SESSION, LogFormat.Operation.FILL_DATA, "{}"), JSONObject.toJSONString(account));
        }

        if (null != current) {
            ReflectionUtil.copyProperties(account, current.getCurrent());
        }

        saveSession();
    }

    @Override
    public void onReconnected() {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.RECONNECT, ""));
        }

        try {
            CMD.REQ_RECONNECT.getProcessor().request(null);
        } catch (Throwable throwable) {
            // TODO 日志
        }
    }

    @Override
    public void onDisconnected() {
        if (null != current) {
            if (logger.isInfoEnabled()) {
                logger.info(LogFormat.format(LogFormat.LogModule.SESSION, LogFormat.Operation.REMOVE, "{}"), "SessionManager onDisconnected");
            }
        }
    }

    public String getUserId() {
        return isOnline() ? current.getCurrent().getUserId() : null;
    }

    public boolean isOnline() {
        return null != current;
    }

    public boolean isSelf(String userId) {
        return isOnline() && current.getCurrent().getUserId().equals(userId);
    }

    public boolean isSelfEquipment(String userId, Messages.Equipment equipment) {
        return isSelf(userId) && equipment == Messages.Equipment.MOBILE_ANDROID;
    }

    public boolean isSelfOtherEquipment(String userId, Messages.Equipment equipment) {
        return isSelf(userId) && equipment != Messages.Equipment.MOBILE_ANDROID;
    }

    public void addAttr(String name, Object data) {
        attrs.put(name, data);
    }

    public boolean hasAttr(String name) {
        return attrs.containsKey(name);
    }

    public void removeAttr(String name) {
        attrs.remove(name);
    }

    public <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }

    public boolean isKickOut() {
        return isKickOut.get();
    }

    public void setKickOut(boolean kickOut) {
        isKickOut.set(kickOut);
    }

    public boolean getIsOverDue() {
        return isOverDue.get();
    }

    public void setIsOverDue(boolean isOverDue) {
        this.isOverDue.set(isOverDue);
    }

    public boolean getIsUserOrPsdError() {
        return isUserOrPsdError.get();
    }

    public void setIsUserOrPsdError(boolean isUserOrPsdError) {
        this.isUserOrPsdError.set(isUserOrPsdError);
    }
}
