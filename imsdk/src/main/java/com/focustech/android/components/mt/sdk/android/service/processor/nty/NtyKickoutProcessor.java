package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import android.util.Log;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.IAccountService;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.communicate.Communication;
import com.focustech.android.components.mt.sdk.communicate.CommunicationContent;
import com.focustech.android.components.mt.sdk.communicate.CommunicationType;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 被迫下线
 *
 * @author zhangxu
 */
public class NtyKickoutProcessor extends AbstractMessageProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.KickoutNty nty = Messages.KickoutNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        try {
            MTModel model = getSessionManager().getCurrent();

            // 不匹配忽略
            if (null != model && !model.getChannelId().equals(message.getHead().getChannelId())) {
                if (logger.isWarnEnabled()) {
                    logger.warn(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "kick out fail.{}, {}"), model.getChannelId(), message.getHead().getChannelId());
                }

                return;
            }
            // 通知前台
            addMessage(new Communication(CommunicationType.KICKOUT, new CommunicationContent(null, NTPTime.now())));
            // 清除信息
            getSessionManager().clear();
            //连接恢复初始状态
            ContextHolder.getMessageService().clean();

            if (ContextHolder.getAndroidContext() != null) {
                SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
                sharedPrefLoginInfo.saveString("loginInfo", "");
                sharedPrefLoginInfo.saveBoolean("loginState", false);
            }

//            if (getBizInvokeCallback() != null && nty != null && nty.getEquipment() != null) {
//                getBizInvokeCallback().privateKickout(nty.getUserId(), nty.getEquipment().name());
//            }

            if (getAccountService() != null) {
                getAccountService().updateLastAction(nty.getUserId(), IAccountService.ACTION_KICKOUT);
            }
            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
        } catch (Throwable e) {
            Log.e("error", "error", e);
        }
    }
}
