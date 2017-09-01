package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncLoginContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.BeanConverter;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息响应
 *
 * @author zhangxu
 */
public class RspUserInfoProcessor extends AbstractUserProcessor {
    private Logger logger = LoggerFactory.getLogger(RspUserInfoProcessor.class);

    public void onMessage(TMMessage message) throws Throwable {
        Messages.UserInfoRsp rsp = Messages.UserInfoRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        AbstractAsyncContext content = AsyncContent.getContent(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));

        try {
            createAccount(message, content, rsp);
            updateFriendInfo(rsp);
        } catch (Exception ex) {
            if (logger.isErrorEnabled())
                logger.error("RspUserInfoProcessor", ex.toString());
            if (getBizInvokeCallback() != null) {
                AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                logger.info("ReqLoginProcessor clean login control context");
                getBizInvokeCallback().privateLoginFailed(1002);        //当后台登录发现问题时，及时通知前台登录失败，以便前台能更快的进行后台登录操作而不用等到超时触发
            } else {
                AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                logger.info("clean login control context");
            }

        }
    }

    /**
     * 创建自己的登陆账号
     *
     * @param content
     * @param rsp
     */
    private void createAccount(TMMessage message, AbstractAsyncContext content, Messages.UserInfoRsp rsp) throws RemoteException {
        if (null != content && Operation.LOGIN == content.getOperation()) {
            operationComplete(message);

            saveUserInfo(rsp);

            if (logger.isInfoEnabled())
                logger.info("createAccount start");
            CMD.LOCAL_AFTER_LOGIN_SUCCESSFUL.getProcessor().request(null);

            AsyncLoginContext context = ((AsyncLoginContext) content);

            if (logger.isInfoEnabled())
                logger.info("createAccount start1");
            Account account = BeanConverter.toAccount(rsp, context.getData().getPassword());

            if (logger.isInfoEnabled())
                logger.info("createAccount start2");
            account.setLastToken(getSessionManager().getCurrent().getToken());

            if (logger.isInfoEnabled())
                logger.info("createAccount start3");
            createNewSession(account);

            if (logger.isInfoEnabled())
                logger.info("createAccount start4");
            updateAccount(account);

            if (logger.isInfoEnabled())
                logger.info("createAccount start5");
            updateMyStatus(Messages.Status.valueOf(context.getData().getStatus()));

            if (logger.isInfoEnabled())
                logger.info("createAccount start6");
            notifyLoginSuccessful(rsp.getUserId());
            if (logger.isInfoEnabled())
                logger.info("createAccount start7");
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyInfoChanged(JSONObject.toJSONString(new UserBase(rsp)));
            }
        }
    }

    private void saveUserInfo(Messages.UserInfoRsp rsp) {
        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        sharedPrefLoginInfo.saveString(UserBase.class.getSimpleName(), JSONObject.toJSONString(new UserBase(rsp)));
    }


    private void updateAccount(final Account account) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getAccountService().addOrUpdate(account);
            }
        });
    }

    private void createNewSession(Account account) {
        SessionManager.getInstance().completeSelfUserBase(account);
    }

    /**
     * 通知登录成功
     *
     * @param userId
     */
    private void notifyLoginSuccessful(String userId) throws RemoteException {
        if (ContextHolder.getAndroidContext() != null) {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            sharedPrefLoginInfo.saveBoolean("loginState", true);
            sharedPrefLoginInfo.saveLong("lastLoginSuccessfulTime", System.currentTimeMillis());
        }
        if (getBizInvokeCallback() != null) {
            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
            logger.info("notifyLoginSuccessful clean login control context");
            getBizInvokeCallback().privateLoginSuccessful(userId, SessionManager.getInstance().getCurrent().getToken(), SessionManager.getInstance().getCurrent().getPlatformData());
        } else
            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
    }

    /**
     * 更新自己的状态
     *
     * @param status
     */
    private void updateMyStatus(Messages.Status status) {
        CMD.REQ_UPDATE_USER_STATUS.getProcessor().request(status);
    }

    private void updateFriendInfo(Messages.UserInfoRsp rsp) throws Throwable {
        MTModel model = getSessionManager().getCurrent();
        refreshUserBase(getSessionManager().getUserId(), model.addOrUpdateUserBase(new UserBase(rsp), true));
    }
}
