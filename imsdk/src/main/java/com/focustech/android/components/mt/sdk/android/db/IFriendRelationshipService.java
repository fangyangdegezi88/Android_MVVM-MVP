package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.FriendRelationship;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxu
 */
public interface IFriendRelationshipService {
    void clear(String userId);

    void moveTo(String userId, String fromFriendGroupId, String toFriendGroupId);

    void add(FriendRelationship relationship);

    Map<String, List<String>> getFriendRelations(String userId, List<String> friendGroupIds);
}
