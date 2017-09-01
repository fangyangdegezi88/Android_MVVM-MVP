package com.focustech.android.components.mt.sdk.android.service.pojo.group;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.android.db.gen.Group;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 好友分组
 *
 * @author zhangxu
 */
public class MTGroup {
    private String groupId;               // 群ID
    private String groupName = "";             // 群名称
    private String groupSignature;        // 群签名
    private String groupKeyword;          // 群关键字
    private String groupDesc;             // 群描述
    private Messages.GroupType groupType; // 群类型
    private String addUserId;            // 创建人
    private String addUserName; // 创建人名称
    private Messages.ValidateRule validateRule;    // 群验证规则
    private String groupNo;              // 群号
    private int limit;                 // 群上限人数
    private int adminCount;            // 群管理员人数
    private Messages.Enable groupEnable;          // 群可用状态
    private long timestamp;            // 群最后更新时间戳
    private long lastActiveTimestamp;            // 最后更新时间戳
    private String groupRemark;        // 自己的备注
    private Map<String, MTGroupUser> users = new HashMap<>();

    public MTGroup() {
    }

    public MTGroup(Messages.DiscussionRsp discussionRsp) {
        this.groupId = discussionRsp.getDiscussionId();

        for (Messages.FriendStatusRsp rsp : discussionRsp.getFriendsList()) {
            addOrUpdateGroupUser(new MTGroupUser(rsp.getFriendUserId(), groupId));
        }
    }

    public MTGroup(Messages.GroupRsp groupRsp) {
        this.groupId = groupRsp.getGroupId();

        for (Messages.FriendStatusRsp rsp : groupRsp.getFriendsList()) {
            addOrUpdateGroupUser(new MTGroupUser(rsp.getFriendUserId(), groupId));
        }
    }

    public MTGroup(Messages.CreateGroupSuccessfulRsp groupRsp) {
        ReflectionUtil.copyProperties(groupRsp, this);
    }

    public MTGroup(Group group) {
        ReflectionUtil.copyProperties(group, this);
        if (null != group.getGroupEnable()) {
            setGroupEnable(Messages.Enable.valueOf(group.getGroupEnable().intValue()));
        }

        if (null != group.getGroupType()) {
            setGroupType(Messages.GroupType.valueOf(group.getGroupType().intValue()));
        }

        if (null != group.getValidateRule()) {
            setValidateRule(Messages.ValidateRule.valueOf(group.getValidateRule().intValue()));
        }
    }

    public void update(Messages.GroupInfoRsp infoRsp) {
        ReflectionUtil.copyProperties(infoRsp, this);
        Log.e("group", JSONObject.toJSONString(this));
    }

    public void update(Messages.MyGroupInfoRsp infoRsp) {
        update(infoRsp.getGroupInfoRsp());
        setGroupRemark(infoRsp.getGroupRemark());
    }

    public void update(Messages.DiscussionInfoRsp rsp) {
        ReflectionUtil.copyProperties(rsp, this);

        setGroupName(rsp.getDiscussionName());
        setAddUserId(rsp.getUserId());
        setAddUserName(rsp.getUserName());
        setGroupEnable(rsp.getDiscussionEnable());
    }

    @JSONField(serialize = false, deserialize = false)
    public MTGroupUser getGroupUser(String groupUserId) {
        return users.get(groupUserId);
    }

    @JSONField(serialize = false, deserialize = false)
    public Set<String> getGroupUserIds() {
        return users.keySet();
    }

    @JSONField(serialize = false, deserialize = false)
    public String getDisplayName() {
        return null == groupRemark || groupRemark.trim().length() == 0 ? groupName : groupRemark;
    }

    public void addOrUpdateGroupUser(MTGroupUser user) {
        if (users.containsKey(user.getGroupUserId())) {
            // 更新属性
            ReflectionUtil.copyProperties(user, users.get(user.getGroupUserId()));
        } else {
            users.put(user.getGroupUserId(), user);
        }
    }

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

    public String getGroupSignature() {
        return groupSignature;
    }

    public void setGroupSignature(String groupSignature) {
        this.groupSignature = groupSignature;
    }

    public String getGroupKeyword() {
        return groupKeyword;
    }

    public void setGroupKeyword(String groupKeyword) {
        this.groupKeyword = groupKeyword;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Messages.GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(Messages.GroupType groupType) {
        this.groupType = groupType;
    }

    public Messages.ValidateRule getValidateRule() {
        return validateRule;
    }

    public void setValidateRule(Messages.ValidateRule validateRule) {
        this.validateRule = validateRule;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getAdminCount() {
        return adminCount;
    }

    public void setAdminCount(int adminCount) {
        this.adminCount = adminCount;
    }

    public Messages.Enable getGroupEnable() {
        return groupEnable;
    }

    public void setGroupEnable(Messages.Enable groupEnable) {
        this.groupEnable = groupEnable;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }

    public Map<String, MTGroupUser> getUsers() {
        return users;
    }

    public void setUsers(Map<String, MTGroupUser> users) {
        this.users = users;
    }

    public String getGroupRemark() {
        return groupRemark;
    }

    public void setGroupRemark(String groupRemark) {
        this.groupRemark = groupRemark;
    }

    public long getLastActiveTimestamp() {
        return lastActiveTimestamp;
    }

    public void setLastActiveTimestamp(long lastActiveTimestamp) {
        this.lastActiveTimestamp = lastActiveTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MTGroup mtGroup = (MTGroup) o;

        return groupId.equals(mtGroup.groupId);

    }

    @Override
    public int hashCode() {
        return groupId.hashCode();
    }
}
