package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Group;
import com.focustech.android.components.mt.sdk.android.db.gen.GroupSetting;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.List;

/**
 * 群组Service
 *
 * @author zhangxu
 */
public interface IGroupService {
    int FEATURE_FOREVER = 0;
    int FEATURE_TEMP = 1;

    void delete(String userId, String groupId, int feature);

    void deleteGroupUser(String userId, String groupId, String groupUserId, int feature);

    void deleteGroupUser(String userId, String groupId, List<String> groupUserIds, int feature);

    void updateGroupUserUserType(String userId, String groupId, List<String> groupUserIds, Messages.UserType userType, int feature);

    /**
     * 刷新数据库的群聊信息，包括了：新增的群、需要删除的群。包括了群中用户的信息重置
     *
     * @param userId
     * @param groups
     * @param feature
     */
    void reset(String userId, MTGroups groups, int feature);

    /**
     * 更新群信息
     *
     * @param userId
     * @param group
     * @param feature
     */
    void addOrUpdate(String userId, MTGroup group, int feature);

    /**
     * 更新群聊用户
     *
     * @param userId
     * @param user
     * @param feature
     */
    void addOrUpdateGroupUser(String userId, MTGroupUser user, int feature);

    void addOrUpdateGroupSetting(String userId, GroupSetting setting, int feature);

    GroupSetting getGroupSetting(String userId, String groupId, int feature);

    /**
     * 查询
     *
     * @param userId
     * @param groupId
     * @param feature
     * @return
     */
    Group get(String userId, String groupId, int feature);

    /**
     * 查询
     *
     * @param userId
     * @param feature
     * @return
     */
    List<Group> getAll(String userId, int feature);
}
