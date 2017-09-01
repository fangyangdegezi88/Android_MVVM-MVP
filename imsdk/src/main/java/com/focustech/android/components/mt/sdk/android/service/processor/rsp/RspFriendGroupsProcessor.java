package com.focustech.android.components.mt.sdk.android.service.processor.rsp;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 用户信息响应
 *
 * @author zhangxu
 */
public class RspFriendGroupsProcessor extends AbstractProcessor {
    public void onMessage(TMMessage message) throws Throwable {
        Messages.FriendGroupsRsp rsp = Messages.FriendGroupsRsp.parseFrom(message.getBody());

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), rsp);
        }

        final MTModel model = getSessionManager().getCurrent();

        for (Messages.FriendGroupRsp friendGroupRsp : rsp.getFriendGroupsList()) {
            model.getFriendGroups().addFriendGroup(friendGroupRsp, model);
        }

        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateFriendGroupsChanged(JSONObject.toJSONString(model.getFriendGroups()));
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    update(model.getFriendGroups());
                } catch (Throwable e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(LogFormat.format(LogFormat.LogModule.DB, LogFormat.Operation.UPDATE, "error"), e);
                    }
                }
            }
        });
    }

    /**
     * 更新数据到数据库
     *
     * @param data
     */
    private void update(FriendGroups data) {
        getFriendGroupService().reset(data);
    }
}
