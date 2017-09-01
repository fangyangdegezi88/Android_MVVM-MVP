package com.focustech.android.components.mt.sdk.android.service.processor;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.util.BeanConverter;
/**
 * 抽象实现
 *
 * @author zhangxu
 */
public abstract class AbstractUserProcessor<PARAM, RETURN, INNER> extends AbstractProcessor<RETURN, PARAM, INNER> {

    /**
     * 刷新好友数据，包括陌生人
     *
     * @param userId
     * @param newFriendBase
     */
    protected void refreshUserBase(String userId, FriendBase newFriendBase, UserBase userBase) {
        MTModel model = getSessionManager().getCurrent();
        model.getFriendGroups().updateFriendBase(newFriendBase);

        refreshUserBase(userId, BeanConverter.toFriend(userId, newFriendBase, userBase), userBase);
    }

    /**
     * 刷新用户数据，包括陌生人
     *
     * @param userId
     * @param userBase
     */
    protected void refreshUserBase(String userId, UserBase userBase) {
        refreshUserBase(userId, BeanConverter.toFriend(userId, userBase), userBase);
    }

    /**
     * 刷新用户数据，包括陌生人
     *
     * @param friend
     * @param friend
     */
    private void refreshUserBase(final String userId, final Friend friend, UserBase userBase) {
        asyncExecute(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getFriendService().addOrUpdate(friend);
                            updateLastTimestamp(userId, LastTimestampType.DATA_FRIEND_GROUPS);
                        } catch (Exception e) {
                            if (logger.isErrorEnabled()) {
                                logger.error(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.UPDATE, "update friend error"), e);
                            }
                            throw e;
                        }
                    }
                });
            }
        });

        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateFriendUserInfoChanged(JSONObject.toJSONString(userBase));
            }
        } catch (RemoteException e) {
            // TODO
        }
    }
}
