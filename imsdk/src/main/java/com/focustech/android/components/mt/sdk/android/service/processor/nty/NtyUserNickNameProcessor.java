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
 * 用户昵称更改
 *
 * @author zhangxu
 */
public class NtyUserNickNameProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.UserNickNameNty nty = Messages.UserNickNameNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        String friendUserId = nty.getUserId();
        String newNickName = nty.getNickName();

        if (getSessionManager().isSelf(friendUserId)) {
            updateUserBase(newNickName);

            getAccountService().updateNickName(newNickName);
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyNickNameChanged(newNickName);
            }
        } else {
            getFriendService().updateNickName(getSessionManager().getUserId(), friendUserId, newNickName);
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateFriendNickNameChanged(friendUserId, newNickName);
            }
        }
    }

    private void updateUserBase(String newNickName) {
        try {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            String userBaseStr = sharedPrefLoginInfo.getString(UserBase.class.getSimpleName(), "");
            if (null != userBaseStr && userBaseStr.length() > 0) {
                UserBase userBase = JSONObject.parseObject(userBaseStr, UserBase.class);
                userBase.setUserNickName(newNickName);
                sharedPrefLoginInfo.saveString(UserBase.class.getSimpleName(), JSONObject.toJSONString(userBase));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
