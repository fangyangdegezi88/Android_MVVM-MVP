package com.focustech.android.components.mt.sdk.android.service.pojo.user;

import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.db.gen.Friend;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendExt;
import com.focustech.android.components.mt.sdk.android.service.pojo.status.UserStatusData;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户基本信息
 *
 * @author zhangxu
 */
public class UserBase {
    private String userName;
    private String userId;
    private String userNickName;
    private String userSignature;
    private Messages.HeadType userHeadType;
    private String userHeadId;
    private String userPinyin;
    private String domain;

    private Messages.TeacherType teacherType;
    // 扩展信息
    private IUserExt ext;

    // 用户状态
    private UserStatusData statusData = new UserStatusData();

    public UserBase() {
    }

    public UserBase(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        statusData.setUserId(userId);
    }

    public UserBase(Messages.UserInfoRsp rsp) {
        ReflectionUtil.copyProperties(rsp, this);
    }

    public UserBase(Messages.UserInfoNty nty) {
        ReflectionUtil.copyProperties(nty, this);
    }

    public UserBase(Friend friend, List<FriendExt> exts) {
        ReflectionUtil.copyProperties(friend, this);
        setUserId(friend.getFriendUserId());

        initExt(exts);
    }

    public void reset(List<Messages.EquipmentStatus> equipmentStatuses) {
        statusData.setUserId(getUserId());
        statusData.reset(equipmentStatuses);
    }

    private void initExt(List<FriendExt> exts) {
        if (null != exts) {
            Map<String, String> map = new HashMap<>();
            for (FriendExt ext : exts) {
                map.put(ext.getName(), ext.getValue());
            }

            try {
                MTRuntime.getUserExt().newInstance().create(map);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public Messages.HeadType getUserHeadType() {
        return userHeadType;
    }

    public void setUserHeadType(Messages.HeadType userHeadType) {
        this.userHeadType = userHeadType;
    }

    public String getUserHeadId() {
        return userHeadId;
    }

    public void setUserHeadId(String userHeadId) {
        this.userHeadId = userHeadId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        statusData.setUserId(userId);
    }

    public String getUserPinyin() {
        return userPinyin;
    }

    public void setUserPinyin(String userPinyin) {
        this.userPinyin = userPinyin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public UserStatusData getStatusData() {
        return statusData;
    }

    public void setStatusData(UserStatusData statusData) {
        this.statusData = statusData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBase userBase = (UserBase) o;

        return userId.equals(userBase.userId);

    }

    public IUserExt getExt() {
        return ext;
    }

    public void setExt(IUserExt ext) {
        this.ext = ext;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    public String getDisplayName() {
        return null == userNickName ? userName : userNickName;
    }

    public boolean isInfoComplete() {
        return null != userHeadType && null != userHeadId;
    }

    public Messages.TeacherType getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(Messages.TeacherType teacherType) {
        this.teacherType = teacherType;
    }
}
