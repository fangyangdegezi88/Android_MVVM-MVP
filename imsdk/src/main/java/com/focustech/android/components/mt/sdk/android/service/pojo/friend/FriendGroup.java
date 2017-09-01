package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 好友分组
 *
 * @author zhangxu
 */
public class FriendGroup {
    private String friendGroupId;
    private String friendGroupName;
    private Messages.FriendGroupType friendGroupType;

    private List<FriendBase> friends = Collections.synchronizedList(new ArrayList<FriendBase>());

    public FriendGroup() {
    }

    public FriendGroup(Messages.FriendGroupRsp friendGroupRsp, MTModel model) {
        ReflectionUtil.copyProperties(friendGroupRsp, this);

        for (Messages.FriendStatusRsp friendStatusRsp : friendGroupRsp.getFriendsList()) {
            addFriend(friendStatusRsp, model);
        }
    }

    public FriendGroup(com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup friendGroup) {
        ReflectionUtil.copyProperties(friendGroup, this);
        setFriendGroupType(Messages.FriendGroupType.valueOf((int) friendGroup.getFriendGroupType()));
    }

    public FriendBase getFriend(String userId) {
        FriendBase value = null;

        for (FriendBase d : friends) {
            if (d.getFriendUserId().equals(userId)) {
                value = d;
                break;
            }
        }

        return value;
    }

    public void changeFromNet(FriendGroup newest) {
        setFriendGroupId(newest.getFriendGroupId());
        setFriendGroupName(newest.getFriendGroupName());
        setFriendGroupType(newest.getFriendGroupType());

        for (FriendBase d : newest.getFriends()) {
            if (null == getFriend(d.getFriendUserId())) {
                friends.remove(d);
            }
        }
    }

    /**
     * 根据本地数据变更
     *
     * @param local
     * @param model
     */
    public void changeFromLocal(FriendGroup local, MTModel model) {
        FriendBase current = null;

        for (FriendBase localFriendBase : local.getFriends()) {
            current = getFriend(localFriendBase.getFriendUserId());

            // 本地加载的数据，在用户信息没有被加载的情况下，把本地数据复制过去。
            if (null != current && null == model.getUserBase(localFriendBase.getFriendUserId()).getUserName()) {
                current.change(localFriendBase);
            }
        }
    }

    public void addFriend(FriendBase data) {
        this.friends.add(data);
        data.setFriendGroupId(this.friendGroupId);
    }

    public void addFriend(Messages.FriendStatusRsp friendStatusRsp, MTModel model) {
        // TODO 处理状态
        UserBase userBase = new UserBase();
        userBase.setUserId(friendStatusRsp.getFriendUserId());
        model.addOrUpdateUserBase(userBase, false);
        addFriend(new FriendBase(getFriendGroupId(), friendStatusRsp.getFriendUserId()));
    }

    public void deleteFriend(FriendBase friendBase) {
        friends.remove(friendBase);
    }

    public com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup toPO(String userId) {
        com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup value = new com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup();
        value.setUserId(userId);
        value.setFriendGroupId(friendGroupId);
        value.setFriendGroupName(friendGroupName);
        value.setFriendGroupType(friendGroupType.getNumber());

        return value;
    }

    public String getFriendGroupId() {
        return friendGroupId;
    }

    public void setFriendGroupId(String friendGroupId) {
        this.friendGroupId = friendGroupId;
    }

    public String getFriendGroupName() {
        return friendGroupName;
    }

    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public Messages.FriendGroupType getFriendGroupType() {
        return friendGroupType;
    }

    public void setFriendGroupType(Messages.FriendGroupType type) {
        this.friendGroupType = type;
    }

    public List<FriendBase> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendBase> friends) {
        this.friends = friends;
    }

    public int size() {
        return friends.size();
    }

    public FriendBase getFriend(int index) {
        return index >= size() ? null : friends.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendGroup groupData = (FriendGroup) o;

        return friendGroupId.equals(groupData.friendGroupId);

    }

    @Override
    public int hashCode() {
        return friendGroupId.hashCode();
    }
}
