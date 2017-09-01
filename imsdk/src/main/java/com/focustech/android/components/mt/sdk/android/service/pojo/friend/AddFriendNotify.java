package com.focustech.android.components.mt.sdk.android.service.pojo.friend;

import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractSystemNotify;

/**
 * 加好友通知
 *
 * @author zhangxu
 */
public class AddFriendNotify extends AbstractSystemNotify {
    /**
     * 来源用户ID
     */
    private String srcUserId;
    /**
     * 来源好友分组ID
     */
    private String srcFriendGroupId;
    /**
     * 来源用户名称
     */
    private String srcUserName;
    /**
     * 附带信息
     */
    private String ext;
    /**
     * 系统消息唯一ID
     */
    private String svrMsgId;

    public String getSrcUserId() {
        return srcUserId;
    }

    public void setSrcUserId(String srcUserId) {
        this.srcUserId = srcUserId;
    }

    public String getSrcFriendGroupId() {
        return srcFriendGroupId;
    }

    public void setSrcFriendGroupId(String srcFriendGroupId) {
        this.srcFriendGroupId = srcFriendGroupId;
    }

    public String getSrcUserName() {
        return srcUserName;
    }

    public void setSrcUserName(String srcUserName) {
        this.srcUserName = srcUserName;
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

    @Override
    public CMD getCMD() {
        return CMD.SYS_NTY_ADD_FRIEND;
    }
}
