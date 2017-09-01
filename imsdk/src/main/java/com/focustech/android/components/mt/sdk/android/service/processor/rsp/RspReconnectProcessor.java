package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncLoginContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 */
public class RspReconnectProcessor extends AbstractProcessor {
    private static final int RE_LOGIN_SUCCESS = 10004;
    private static final int RE_LOGIN_KICKOUT = 10003;

    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.ReconnectRsp rsp = Messages.ReconnectRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        operationComplete(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()));
        if (0 == rsp.getCode()) {
            // 成功， 更新当前缓存的channelId
            if (getSessionManager().getCurrent() == null) {
                //恢复之前保存的seqId  如果从零开始，并且无消息时  会进入死循环
                getMessageService().restoreLastSeqId();

                setSessionManager();
                getSessionManager().getCurrent().setChannelId(message.getHead().getChannelId());
            } else {
                getSessionManager().getCurrent().setChannelId(message.getHead().getChannelId());
            }

        } else if (RE_LOGIN_SUCCESS == rsp.getCode()) {     //当重连服务器帮我们自动登录时，我们本地创建一个上下文处理自动登录上下文
            if (getSessionManager().getCurrent() == null) {
                setSessionManager();
            }
            getSessionManager().getCurrent().setChannelId(message.getHead().getChannelId());
            ContextHolder.getMessageService().resume();
            MTModel model = getSessionManager().getCurrent();
            if (model != null && model.getLoginData() != null) {
                addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(message.getHead().getCliSeqId()), new AsyncLoginContext(model.getLoginData()));
            }
        } else if (RE_LOGIN_KICKOUT == rsp.getCode()) {
            if (ContextHolder.getAndroidContext() != null) {
                SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
                sharedPrefLoginInfo.saveString("loginInfo", "");
                sharedPrefLoginInfo.saveBoolean("loginState", false);
            }

            SessionManager.getInstance().setKickOut(true);
            if (null != getBizInvokeCallback()) {
                getBizInvokeCallback().privateKickout(rsp.getUserId(), Messages.Equipment.PC.toString());       //在此随便传一个值给前台，前台并不需要这个值
            }
        } else {
            // 失败，通知客户端
            if (null != getBizInvokeCallback())
                getBizInvokeCallback().privateAutoLoginFailed();
        }

    }

    /**
     * 恢复SessionManager
     */
    private void setSessionManager() {
        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        String mtModel = sharedPrefLoginInfo.getString(MTModel.class.getSimpleName(), "");
        MTModel mtModel1 = JSONObject.parseObject(mtModel, MTModel.class);

        getSessionManager().setCurrent(mtModel1);
    }
}
