package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 好友分组集合
 *
 * @author zhangxu
 */
public class FriendGroups {
    private String userId;
    private List<FriendGroup> friendGroups = Collections.synchronizedList(new ArrayList<FriendGroup>());

    public void updateFriendBase(FriendBase friendBase) {
        FriendBase old = getFriend(friendBase.getFriendUserId());
        old.setFriendGroupId(friendBase.getFriendGroupId());
        old.setLastChatTimestamp(friendBase.getLastChatTimestamp());
        old.setOnlineRemind(friendBase.isOnlineRemind());
        old.setRemark(friendBase.getRemark());
    }

    /**
     * 这个是从服务器拉取的最新数据，如果当前已经有数据，则覆盖当前数据
     *
     * @param friendGroupRsp
     */
    public void addFriendGroup(Messages.FriendGroupRsp friendGroupRsp, MTModel model) {
        FriendGroup friendGroup = new FriendGroup(friendGroupRsp, model);

        if (friendGroups.contains(friendGroup)) {
            FriendGroup old = getFriendGroup(friendGroup.getFriendGroupId());
            old.changeFromNet(friendGroup);
        } else {
            friendGroups.add(friendGroup);
        }
    }

    /**
     * 从本地数据库加载的数据，如果这个时候，friendGroups已经有数据了，那么这个数据要么是刚从网络请求来的，要么是之前本地加载的
     *
     * @param friendGroup
     */
    public void addFriendGroup(FriendGroup friendGroup, MTModel model) {
        if (friendGroups.contains(friendGroup)) {
            FriendGroup old = getFriendGroup(friendGroup.getFriendGroupId());
            old.changeFromLocal(friendGroup, model);
        }
    }

    @JSONField(deserialize = false, serialize = false)
    public FriendBase getFriend(String userId) {
        FriendBase value = null;

        for (FriendGroup f : friendGroups) {
            value = f.getFriend(userId);

            if (null != value) {
                break;
            }
        }

        return value;
    }

    public FriendBase deleteFriend(String userId) {
        FriendBase target = getFriend(userId);

        if (null != target) {
            for (FriendGroup f : friendGroups) {
                if (f.getFriendGroupId().equals(target.getFriendGroupId())) {
                    f.deleteFriend(target);
                    break;
                }
            }
        }

        return target;
    }

    public List<FriendBase> deleteFriendGroup(String friendGroupId) {
        FriendGroup target = getFriendGroup(friendGroupId);
        FriendGroup defaultGroup = getDefaultFriendGroup();
        List<FriendBase> friends = null;

        if (null != target) {
            friendGroups.remove(target);
            friends = target.getFriends();

            if (null != friends) {
                for (FriendBase friend : friends) {
                    defaultGroup.addFriend(friend);
                }
            }
        }

        return friends;
    }

    public List<com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup> toPOList(String userId) {
        List<com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup> pos = new ArrayList<>();

        for (FriendGroup data : friendGroups) {
            pos.add(data.toPO(userId));
        }

        return pos;
    }

    public List<FriendGroup> getFriendGroups() {
        return friendGroups;
    }

    public void setFriendGroups(List<FriendGroup> friendGroups) {
        this.friendGroups = friendGroups;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int size() {
        return friendGroups.size();
    }

    public int sizeOfFriends(int friendGroupIndex) {
        return friendGroupIndex >= size() ? 0 : getFriendGroup(friendGroupIndex).size();
    }

    @JSONField(deserialize = false, serialize = false)
    public FriendGroup getFriendGroup(int friendGroupIndex) {
        return friendGroupIndex >= size() ? null : friendGroups.get(friendGroupIndex);
    }

    @JSONField(deserialize = false, serialize = false)
    public FriendBase getFriendBase(int friendGroupIndex, int friendIndex) {
        FriendGroup friendGroup = getFriendGroup(friendGroupIndex);
        return null == friendGroup ? null : friendGroup.getFriend(friendIndex);
    }

    @JSONField(deserialize = false, serialize = false)
    public FriendGroup getFriendGroup(String friendGroupId) {
        FriendGroup value = null;

        for (FriendGroup d : friendGroups) {
            if (null != d && d.getFriendGroupId().equals(friendGroupId)) {
                value = d;
                break;
            }
        }

        return value;
    }

    @JSONField(deserialize = false, serialize = false)
    public FriendGroup getDefaultFriendGroup() {
        FriendGroup value = null;

        for (FriendGroup d : friendGroups) {
            if (null != d && d.getFriendGroupType() == Messages.FriendGroupType.DEFAULT) {
                value = d;
                break;
            }
        }

        return value;
    }
}
