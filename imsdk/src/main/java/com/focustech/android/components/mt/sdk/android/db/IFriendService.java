package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Friend;

import java.util.List;

/**
 * 好友Service
 *
 * @author zhangxu
 */
public interface IFriendService {
    void addOrUpdate(Friend friend);

    boolean updateSignature(String userId, String friendUserId, String newSignature);

    boolean updateNickName(String userId, String friendUserId, String newNickName);

    /**
     * 删除好友
     *
     * @param userId
     * @param friendUserId
     * @param friendGroupId
     */
    void deleteFriend(String userId, String friendUserId, String friendGroupId);

    /**
     * 更新用户头像图片
     *
     * @param userHeadType 头像类型
     * @param userHeadId   头像ID
     * @return
     */
    boolean updateHead(String userId, String friendUserId, long userHeadType, String userHeadId);

    List<Friend> getFriends(String userId, List<String> friendIds);
}
