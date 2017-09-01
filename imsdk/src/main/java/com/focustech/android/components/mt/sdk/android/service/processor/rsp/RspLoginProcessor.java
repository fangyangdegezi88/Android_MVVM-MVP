package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 登陆rsp响应
 *
 * @author zhangxu
 */
public class RspLoginProcessor extends AbstractMessageProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.LoginRsp rsp = Messages.LoginRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        // 这里只关心失败，成功后由 UserInfoRsp返回再通知业务方
        if (rsp.getCode() != 0) {
            operationComplete(message);
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            sharedPrefLoginInfo.saveString("loginInfo", "");
            if (rsp.getCode() == 10001) {
                SessionManager.getInstance().setIsUserOrPsdError(true);
            }
            if (getBizInvokeCallback() != null) {
                AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                logger.info("ReqLoginProcessor clean login control context");

                getBizInvokeCallback().privateLoginFailed(rsp.getCode());
            } else {
                AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                logger.info("clean login control context");
            }
        } else {
            SessionManager.getInstance().newSession(rsp.getUserId(), rsp.getToken(), rsp.getPlantData(), message.getHead());
        }
    }
}
