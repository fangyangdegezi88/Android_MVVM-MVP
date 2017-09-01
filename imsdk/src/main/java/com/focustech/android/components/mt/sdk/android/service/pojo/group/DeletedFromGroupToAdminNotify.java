package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;

/**
 * 被删除出群
 *
 * @author zhangxu
 */
public class DeletedFromGroupToAdminNotify extends AbstractSystemNotify {
    private String groupId;               // 群ID
    private String groupName;             // 群ID
    private String srcUserId;             // 操作发起人
    private String srcUserName;           // 操作发起人
    private String targetUserId;          // 被删除人ID
    private String targetUserName;        // 被删除人名称
    private long timestamp;             // 系统接收到消息请求的时间

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSrcUserId() {
        return srcUserId;
    }

    public void setSrcUserId(String srcUserId) {
        this.srcUserId = srcUserId;
    }

    public String getSrcUserName() {
        return srcUserName;
    }

    public void setSrcUserName(String srcUserName) {
        this.srcUserName = srcUserName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_DELETE_GROUP_USER_TO_ADMIN;
    }
}
