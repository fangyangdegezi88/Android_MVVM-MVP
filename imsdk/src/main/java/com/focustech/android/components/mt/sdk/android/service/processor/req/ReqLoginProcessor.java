package com.focustech.android.components.mt.sdk.android.service.processor.req;


import android.os.RemoteException;
import android.os.SystemClock;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.IAccountService;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncLoginContext;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncLoginControlContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.LoginData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.concurrent.TimeUnit;

/**
 * 登录请求处理
 *
 * @author zhangxu
 */
public class ReqLoginProcessor extends AbstractProcessor<LoginData, Void, Void> {

    public static final String LOGIN_KEY = "login_key";

    @Override
    public Void request(final LoginData data) {
        TaskUtil.execute(new Runnable() {
            @Override
            public void run() {
                /*
                 * 2017/1/17 加入距离上次登录超过60天，则登录逾期
                 */
                if (ContextHolder.getAndroidContext() != null && data.isNeedOverdue()) {
                    SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
                    long lastLoginSuccessfulTime = sharedPrefLoginInfo.getLong("lastLoginSuccessfulTime", 0);
                    long thisLoginTime = System.currentTimeMillis();
                    if (thisLoginTime - lastLoginSuccessfulTime > TimeUnit.DAYS.toMillis(60) && lastLoginSuccessfulTime > 0) {
                        if (getBizInvokeCallback() != null) {
                            try {
                                sharedPrefLoginInfo.saveString("loginInfo", "");
                                sharedPrefLoginInfo.saveBoolean("loginState", false);
                                AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                                getBizInvokeCallback().privateLoginFailed(9005);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                }

                int connectTemp = MTRuntime.getServers().length;
                while (!ContextHolder.getMessageService().isConnected()) {       //尝试连接,如果当前无连接，则将每个地址尝试连接一次，直到所有地址尝试失败，返回前台失败
                    if (connectTemp-- < 0) {
                        try {
                            if (getBizInvokeCallback() != null)
                                getBizInvokeCallback().privateLoginFailed(1000);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    ContextHolder.getMessageService().connectServer();
                    SystemClock.sleep(100);
                }
                if (logger.isInfoEnabled()) {
                    logger.info("ReqLoginProcessor" + ContextHolder.getMessageService().currentHostAndPort());
                    logger.info("ReqLoginProcessor--->request");
                }
                if (!addLoginControl())
                    return;
                if (logger.isInfoEnabled())
                    logger.info("ReqLoginProcessor--->request to");
                try {
                    getSessionManager().clear();
                    ContextHolder.getMessageService().resume();

                    Account account = getAccountService().getAccountByName(data.getUserName());

                    // 只有上一次是主动退出，或者从来没有登陆过，重新登录
                    if (account == null || account.getLastAction() == IAccountService.ACTION_LOGOUT) {
                        doLogin(data);
                    } else {
                        // 否则查看
                        doLogin(data);
                    }
                } catch (Exception ex) {
                    if (logger.isInfoEnabled()) {
                        logger.info("ReqLoginProcessor", ex.getMessage());
                    }
                    try {
                        if (getBizInvokeCallback() != null) {
                            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                            logger.info("ReqLoginProcessor clean login control context");
                            getBizInvokeCallback().privateLoginFailed(1001);
                        } else
                            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return null;
    }

    private synchronized static boolean addLoginControl() {
        //当正在进行登录操作时，不在重复进行登录操作
        if (logger.isInfoEnabled()) {
            logger.info("ReqLoginProcessor---->addLoginControl in");
        }
        if (AsyncLoginControlContent.exists(LOGIN_KEY)) {
            if (logger.isInfoEnabled()) {
                logger.info("ReqLoginProcessor---->login is running");
            }
            return false;
        }

        //添加登录控制上下文
        AsyncLoginControlContent.addContent(LOGIN_KEY, new AsyncLoginControlContext(), DEFAULT_TIMEOUT);
        if (AsyncLoginControlContent.exists(LOGIN_KEY)) {
            logger.info("ReqLoginProcessor->>add successful");
        }
        return true;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return false;
    }

    private void doLogin(LoginData data) {
        if (ContextHolder.getAndroidContext() != null) {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            sharedPrefLoginInfo.saveString("loginInfo", JSONObject.toJSONString(data));
        }
        if (logger.isInfoEnabled())
            logger.info("ReqLoginProcessor--->doLogin");
        byte[] send = Messages.LoginReq.newBuilder()
                .setEquipment(Messages.Equipment.MOBILE_ANDROID)
                .setEquipmentInfo(data.getEquipmentInfo())
                .setUserName(data.getUserName())
                .setUserPassword(data.getPassword())
                .setRole(Messages.Role.valueOf(data.getRole()))
                .setLocale(data.getLocale())
                .setDeviceToken(data.getDeviceToken())
                .setIdentityCode(data.getIdentityCode())
                .setNeedIdentify(data.isNeedIdentify())
                .build().toByteArray();

        int cliSeqId = ContextHolder.getMessageService().sendMessage(CMD.REQ_LOGIN, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), new AsyncLoginContext(data));
    }
}
