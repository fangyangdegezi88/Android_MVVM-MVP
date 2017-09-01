package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.android.components.mt.sdk.android.service.pojo.status.UserStatusData;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 麦通 MODEL，映射了整个数据结构
 *
 * @author zhangxu
 */
public class MTModel {
    /**
     * 当前登录的channel
     */
    private String channelId;
    /**
     * 当前登录的token
     */
    private String token;
    /**
     * 当前登录数据
     */
    private LoginData loginData;
    /**
     * 当前用户
     */
    private UserBase current;
    /**
     * 好友分组
     */
    private FriendGroups friendGroups = new FriendGroups();
    /**
     * 用户基本数据，包括了：好友，陌生人，群、讨论组中的用户数据
     */
    private List<UserBase> users = Collections.synchronizedList(new ArrayList<UserBase>());
    /**
     * 群组
     */
    private MTGroups groups = new MTGroups();
    private MTGroups discussions = new MTGroups();

    private String platformData;

    public MTModel() {

    }

    public MTModel(String userId) {
        current = new UserBase();
        current.setUserId(userId);
    }

    @JSONField(serialize = false, deserialize = false)
    public MTGroup getGroup(String contactId, long contactType) {
        MTGroups target = Messages.RecentContactType.GROUP_VALUE == contactType ? groups : discussions;
        return target.getGroup(contactId);
    }

    @JSONField(serialize = false, deserialize = false)
    public List<UserStatusData> getUsersStatus(List<String> userIds) {
        List<UserStatusData> value = new ArrayList<>();
        UserBase tmp;

        for (String userId : userIds) {
            tmp = getUserBase(userId);

            if (null != tmp) {
                value.add(tmp.getStatusData());
            } else {
                value.add(new UserStatusData(userId));
            }
        }

        return value;
    }

    public void reset(List<UserStatusData> statusDataList) {
        for (UserStatusData data : statusDataList) {
            reset(data);
        }
    }

    public void reset(String userId, List<Messages.EquipmentStatus> status) {
        UserBase userBase = getUserBase(userId);

        if (userBase == null) {
            userBase = new UserBase(userId, null);
            addOrUpdateUserBase(userBase, false);
        }

        userBase.reset(status);
    }

    private void reset(UserStatusData data) {
        UserBase userBase = getUserBase(data.getUserId());

        if (userBase == null) {
            userBase = new UserBase(data.getUserId(), null);
            addOrUpdateUserBase(userBase, false);
        }

        userBase.setStatusData(data);
    }

    @JSONField(serialize = false, deserialize = false)
    public UserBase getUserBase(String userId) {
        for (UserBase userBase : users) {
            if (userBase.getUserId().equals(userId)) {
                return userBase;
            }
        }

        return null;
    }

    public UserBase addOrUpdateUserBase(UserBase userBase, boolean update) {
        UserBase value = userBase;

        if (users.contains(userBase)) {
            value = getUserBase(userBase.getUserId());
            // 如果model中的用户名称都为空，那么意味着这个数据只有userId
            if (update || !value.isInfoComplete()) {
                ReflectionUtil.copyProperties(userBase, value);
            }
        } else {
            users.add(userBase);
        }

        return value;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public UserBase getCurrent() {
        return current;
    }

    public void setCurrent(UserBase current) {
        this.current = current;
    }

    public FriendGroups getFriendGroups() {
        return friendGroups;
    }

    public void setFriendGroups(FriendGroups friendGroups) {
        this.friendGroups = friendGroups;
    }

    public List<UserBase> getUsers() {
        return users;
    }

    public void setUsers(List<UserBase> users) {
        this.users = users;
    }

    public MTGroups getGroups() {
        return groups;
    }

    public void setGroups(MTGroups groups) {
        this.groups = groups;
    }

    public MTGroups getDiscussions() {
        return discussions;
    }

    public void setDiscussions(MTGroups discussions) {
        this.discussions = discussions;
    }

    public String getPlatformData() {
        return platformData;
    }

    public void setPlatformData(String platformData) {
        this.platformData = platformData;
    }
}
