package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.FriendGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;

import java.util.List;

/**
 * 好友分组Service
 *
 * @author zhangxu
 */
public interface IFriendGroupService {
    /**
     * 刷新所有好友分组数据
     *
     * @param data
     */
    void reset(FriendGroups data);

    /**
     * 拉取自己的好友分组
     *
     * @param userId
     * @return
     */
    List<FriendGroup> getAll(String userId);

    /**
     * 删除好友分组
     *
     * @param userId
     * @param friendGroupId
     * @param moveToFriendGroupId
     */
    void delete(String userId, String friendGroupId, String moveToFriendGroupId);

    void clear(String userId);
}
