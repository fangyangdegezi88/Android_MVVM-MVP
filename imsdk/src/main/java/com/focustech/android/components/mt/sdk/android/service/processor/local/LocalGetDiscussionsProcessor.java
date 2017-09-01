package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.Group;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.FetchData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 获取本地群组列表
 */
public class LocalGetDiscussionsProcessor extends AbstractMessageProcessor<Void, Void, Void> {
    @Override
    public Void request(final Void data) {
        final String userId = getSessionManager().getUserId();
        final MTModel model = getSessionManager().getCurrent();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Group> groups = getGroupService().getAll(userId, IGroupService.FEATURE_TEMP);

                    for (final Group group : groups) {
                        if (MTRuntime.optionsRemoteMessagesDiscussions()) {
                            // 加载聊天记录
                            asyncExecute(MTRuntime.optionsRemoteMessagesDelay(), TimeUnit.SECONDS, new Runnable() {
                                @Override
                                public void run() {
                                    FetchData data = new FetchData();
                                    data.setType(Messages.RecentContactType.DISCUSSION);
                                    data.setContactId(group.getGroupId());
                                    data.setFromTimestamp(getFromTimestamp(LastTimestampType.MESSAGE_DISCUSSION));
                                    data.setToTimestamp(getToTimestamp(LastTimestampType.MESSAGE_DISCUSSION));

                                    CMD.REQ_FETCH_MESSAGE.getProcessor().request(data);
                                }
                            });
                        }

                        model.getDiscussions().addOrUpdateGroup(new MTGroup(group));
                    }
                } catch (Throwable e) {
                    Log.e("user", "error", e);
                }

                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateDiscussionsChanged(JSONObject.toJSONString(model.getGroups()));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }
}
