package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 增加好友响应
 *
 * @author zhangxu
 */
public class AddFriendAnswer {
    private Messages.FriendAnswer answer;
    private String srcFriendUserId = "";           // 来源好友ID
    private String srcFriendGroupId = "";          // 自己的好友分组ID
    private String selfFriendGroupId = "";         // 把src放到自己的那个用户分组中
    private String ext = "";                          // 客户端自己的附带信息
    private String svrMsgId;               //系统消息唯一标示

    public Messages.FriendAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(Messages.FriendAnswer answer) {
        this.answer = answer;
    }

    public String getSrcFriendUserId() {
        return srcFriendUserId;
    }

    public void setSrcFriendUserId(String srcFriendUserId) {
        this.srcFriendUserId = srcFriendUserId;
    }

    public String getSrcFriendGroupId() {
        return srcFriendGroupId;
    }

    public void setSrcFriendGroupId(String srcFriendGroupId) {
        this.srcFriendGroupId = srcFriendGroupId;
    }

    public String getSelfFriendGroupId() {
        return selfFriendGroupId;
    }

    public void setSelfFriendGroupId(String selfFriendGroupId) {
        this.selfFriendGroupId = selfFriendGroupId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }
}
