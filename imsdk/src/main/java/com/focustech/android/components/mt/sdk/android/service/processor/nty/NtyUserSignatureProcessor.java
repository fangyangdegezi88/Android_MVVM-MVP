package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 用户签名更改
 *
 * @author zhangxu
 */
public class NtyUserSignatureProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.UserSignatureNty nty = Messages.UserSignatureNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        String friendUserId = nty.getUserId();
        String newSignature = nty.getUserSignature();

        if (getSessionManager().isSelf(friendUserId)) {
            updateUserBase(newSignature);
            getAccountService().updateSignature(newSignature);
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMySignatureChanged(newSignature);
            }
        } else {
            getFriendService().updateSignature(getSessionManager().getUserId(), friendUserId, newSignature);
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateFriendSignatureChanged(friendUserId, newSignature);
            }
        }
    }

    private void updateUserBase(String newSignature) {
        try {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            String userBaseStr = sharedPrefLoginInfo.getString(UserBase.class.getSimpleName(), "");
            if (null != userBaseStr && userBaseStr.length() > 0) {
                UserBase userBase = JSONObject.parseObject(userBaseStr, UserBase.class);
                userBase.setUserSignature(newSignature);
                sharedPrefLoginInfo.saveString(UserBase.class.getSimpleName(), JSONObject.toJSONString(userBase));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
