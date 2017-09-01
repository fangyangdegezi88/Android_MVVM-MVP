package com.focustech.android.components.mt.sdk.android.service.processor.req;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.concurrent.TimeUnit;

/**
 * 断线重连
 *
 * @author zhangxu
 */
public class ReqReconnectProcessor extends AbstractProcessor<MTModel, Void, MTModel> {
    @Override
    public Void request(MTModel data) {
        //检查是否登录逾期
        if (ContextHolder.getAndroidContext() != null) {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            long lastLoginSuccessfulTime = sharedPrefLoginInfo.getLong("lastLoginSuccessfulTime", 0);
            long thisLoginTime = System.currentTimeMillis();
            if (thisLoginTime - lastLoginSuccessfulTime > TimeUnit.DAYS.toMillis(60) && lastLoginSuccessfulTime > 0) {
                if (getBizInvokeCallback() != null) {
                    try {
                        SessionManager.getInstance().setIsOverDue(true);
                        sharedPrefLoginInfo.saveString("loginInfo", "");
                        sharedPrefLoginInfo.saveBoolean("loginState", false);
                        AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                        getBizInvokeCallback().privateLoginFailed(9005);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }

        internalCycle(data);
        return null;
    }

    @Override
    public void internalCycle(MTModel data) {
        MTModel model = getSessionManager().getCurrent();

        if (null != model && GeneralUtils.isNotNullOrEmpty(model.getCurrent().getUserName())) {
            sendReconnect(model);
        } else if (data != null && GeneralUtils.isNotNullOrEmpty(data.getCurrent().getUserName())) {
            sendReconnect(data);
        }
    }

    /**
     * 发送重连请求
     *
     * @param model
     */
    private void sendReconnect(MTModel model) {

        byte[] send = Messages.ReconnectReq.newBuilder()
                .setChannelId(model.getChannelId())
                .setEquipment(Messages.Equipment.MOBILE_ANDROID)
                .setToken(model.getToken())
                .setUserId(model.getCurrent().getUserId())
                .setUserName(model.getCurrent().getUserName())
                .setRole(Messages.Role.valueOf(model.getLoginData().getRole()))
                .setUserPassword(model.getLoginData().getPassword())
                .setEquipment(Messages.Equipment.MOBILE_ANDROID)
                .setEquipmentInfo(model.getLoginData().getEquipmentInfo())
                .setDeviceUUID(JSONObject.parseObject(model.getLoginData().getEquipmentInfo()).getString("myUUID"))
                .build().toByteArray();
        int cliSeqId = ContextHolder.getMessageService().sendMessage(CMD.REQ_RECONNECT, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.RECONNECT, getSessionManager().getUserId());
    }


    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        return false;
    }
}
