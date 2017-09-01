package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendExt;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendBase;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractUserProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取本地好友分组列表
 */
public class LocalGetFriendGroupsProcessor extends AbstractUserProcessor<Void, Void, Void> {
    @Override
    public Void request(final Void data) {
        final String userId = getSessionManager().getUserId();
        final MTModel model = getSessionManager().getCurrent();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                List<com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup> groups = getFriendGroupService().getAll(userId);
                List<String> ids = new ArrayList<>();

                for (com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup fg : groups) {
                    ids.add(fg.getFriendGroupId());
                }

                Map<String, List<String>> relations = getFriendRelationshipService().getFriendRelations(userId, ids);

                FriendGroup tmp;

                try {
                    for (com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup fg : groups) {
                        tmp = new FriendGroup(fg);
                        List<Friend> friends = getFriendService().getFriends(userId, relations.get(fg.getFriendGroupId()));
                        Map<String, List<FriendExt>> exts = getFriendExtService().getFriendExts(relations.get(fg.getFriendGroupId()));

                        for (Friend friend : friends) {
                            model.addOrUpdateUserBase(new UserBase(friend, exts.get(fg.getUserId())), false);
                            tmp.addFriend(new FriendBase(friend));
                        }

                        model.getFriendGroups().addFriendGroup(tmp, model);
                    }
                } catch (Throwable e) {
                    Log.e("user", "error", e);
                }

                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateUsersPoolChanged(JSONObject.toJSONString(model.getUsers()));
                    }
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateFriendGroupsChanged(JSONObject.toJSONString(model.getFriendGroups()));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }
}
