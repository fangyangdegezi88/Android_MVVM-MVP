package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 应答申请加入群
 *
 * @author zhangxu
 */
public class JoinGroupAnswer {
    private String groupId;         // 群ID
    private String userId;          // 申请人ID
    private Messages.Enable result;          // 结果
    private String refuseMessage;   // 拒绝理由
    private long timestamp;       // 系统接收到消息请求的时间
    private String svrMsgId;        //系统消息唯一标示

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Messages.Enable getResult() {
        return result;
    }

    public void setResult(Messages.Enable result) {
        this.result = result;
    }

    public String getRefuseMessage() {
        return refuseMessage;
    }

    public void setRefuseMessage(String refuseMessage) {
        this.refuseMessage = refuseMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSvrMsgId() {
        return svrMsgId;
    }

    public void setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
    }
}
