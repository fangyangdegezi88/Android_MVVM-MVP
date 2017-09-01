package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 好友信息
 *
 * @author zhangxu
 */
public class FriendBase {
    private String friendUserId;
    private String friendGroupId;
    private String remark;
    private long lastChatTimestamp;
    private boolean onlineRemind;
    private boolean noDisturb = false; // 免打扰

    public FriendBase() {
    }

    public FriendBase(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public FriendBase(Friend friend) {
        ReflectionUtil.copyProperties(friend, this);
        setNoDisturb(friend.getNoDisturb());
    }

    public FriendBase(Messages.FriendInfoRsp rsp) {
        ReflectionUtil.copyProperties(rsp, this);
        setFriendUserId(rsp.getFriend().getUserId());
        setOnlineRemind(Messages.Enable.ENABLE == rsp.getOnlineRemind());
        setNoDisturb(Messages.Enable.ENABLE == rsp.getNoDisturbSetting());
    }

    public FriendBase(Messages.FriendInfoNty nty) {
        ReflectionUtil.copyProperties(nty, this);
        setFriendUserId(nty.getFriend().getUserId());
        setOnlineRemind(Messages.Enable.ENABLE == nty.getOnlineRemind());
        // TODO
//        setNoDisturb(Messages.Enable.ENABLE == nty.getNoDisturbSetting());
    }

    public FriendBase(String friendGroupId, String friendUserId) {
        setFriendGroupId(friendGroupId);
        setFriendUserId(friendUserId);
    }

    public void change(FriendBase newest) {
        ReflectionUtil.copyProperties(newest, this);
    }

    public String getFriendGroupId() {
        return friendGroupId;
    }

    public void setFriendGroupId(String friendGroupId) {
        this.friendGroupId = friendGroupId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getLastChatTimestamp() {
        return lastChatTimestamp;
    }

    public void setLastChatTimestamp(long lastChatTimestamp) {
        this.lastChatTimestamp = lastChatTimestamp;
    }

    public boolean isOnlineRemind() {
        return onlineRemind;
    }

    public void setOnlineRemind(boolean onlineRemind) {
        this.onlineRemind = onlineRemind;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getDisplayName(UserBase base) {
        return null != remark ? remark : null == base ? "" : base.getDisplayName();
    }

    public boolean isNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(boolean noDisturb) {
        this.noDisturb = noDisturb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendBase that = (FriendBase) o;

        if (!friendUserId.equals(that.friendUserId)) return false;
        return friendGroupId.equals(that.friendGroupId);

    }

    @Override
    public int hashCode() {
        int result = friendUserId.hashCode();
        result = 31 * result + friendGroupId.hashCode();
        return result;
    }
}
