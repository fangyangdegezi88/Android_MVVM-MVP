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
public class NtyUserHeadProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.UserHeadNty nty = Messages.UserHeadNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        String friendUserId = nty.getUserId();
        int userHeadType = nty.getUserHeadType().getNumber();
        String userHeadId = nty.getUserHeadId();

        if (getSessionManager().isSelf(friendUserId)) {
            updateUserBase(userHeadId, userHeadType);
            if (getAccountService() != null) {
                getAccountService().updateHead(userHeadType, userHeadId);
            }
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyHeadChanged(userHeadType, userHeadId);
            }
        } else {
            if (getFriendExtService() != null) {
                getFriendService().updateHead(getSessionManager().getUserId(), friendUserId, userHeadType, userHeadId);
            }
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateFriendHeadChanged(friendUserId, userHeadType, userHeadId);
            }
        }
    }

    private void updateUserBase(String userHeadId, int userHeadType) {
        try {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
            String userBaseStr = sharedPrefLoginInfo.getString(UserBase.class.getSimpleName(), "");
            if (null != userBaseStr && userBaseStr.length() > 0) {
                UserBase userBase = JSONObject.parseObject(userBaseStr, UserBase.class);
                if (null != userBase) {
                    userBase.setUserHeadId(userHeadId);
                    userBase.setUserHeadType(Messages.HeadType.valueOf(userHeadType));
                    sharedPrefLoginInfo.saveString(UserBase.class.getSimpleName(), JSONObject.toJSONString(userBase));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
