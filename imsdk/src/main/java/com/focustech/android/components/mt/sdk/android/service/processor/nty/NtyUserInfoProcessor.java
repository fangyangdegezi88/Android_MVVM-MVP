package com.focustech.android.components.mt.sdk.android.service.processor.nty;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.BeanConverter;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户信息更新通知
 *
 * @author zhangxu
 */
public class NtyUserInfoProcessor extends AbstractUserProcessor {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.UserInfoNty nty = Messages.UserInfoNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        String userId = nty.getUserId();
        UserBase userBase = new UserBase(nty);

        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        sharedPrefLoginInfo.saveString(UserBase.class.getSimpleName(), JSONObject.toJSONString(userBase));

        if (getSessionManager().isSelf(userId)) {
            refreshAccount(nty);

            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMyInfoChanged(JSONObject.toJSONString(userBase));
            }
        } else {
            MTModel model = getSessionManager().getCurrent();
            refreshUserBase(userId, model.addOrUpdateUserBase(userBase, true));
        }
    }

    private void refreshAccount(final Messages.UserInfoNty nty) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        getAccountService().addOrUpdate(BeanConverter.toAccount(nty));
                        updateLastTimestamp(nty.getUserId(), LastTimestampType.DATA_ACCOUNT);
                    }
                });
            }
        });
    }
}
