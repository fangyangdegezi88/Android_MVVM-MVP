package com.focustech.android.components.mt.sdk.android.service.processor.req;


import android.os.RemoteException;

import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.IAccountService;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 登录请求处理
 *
 * @author zhangxu
 */
public class ReqLogoutProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        // 更新最后的行为
        getAccountService().updateLastAction(getSessionManager().getUserId(), IAccountService.ACTION_LOGOUT);

        byte[] send = Messages.LogoutReq.getDefaultInstance().toByteArray();

        int cliSeqId = ContextHolder.getMessageService().sendMessage(CMD.REQ_LOGOUT, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.LOGOUT, getSessionManager().getUserId());

        try {
            if (ContextHolder.getAndroidContext() != null) {
                SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
                sharedPrefLoginInfo.saveString("loginInfo", "");
                sharedPrefLoginInfo.saveBoolean("loginState", false);
                System.out.println("ReqLogoutProcessor clear login info");
            }
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateLogoutSuccessful(getSessionManager().getUserId());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            // 删除最近登录记录，避免断线后自动登录
            getSessionManager().clear();
        }
        AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
        return null;
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        ContextHolder.getMessageService().clean(); // 收到回执断开链接
        return super.onReceipt(message, context);
    }
}
