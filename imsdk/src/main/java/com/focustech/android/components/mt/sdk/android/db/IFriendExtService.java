package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.FriendExt;

import java.util.List;
import java.util.Map;

/**
 * 用户扩展Service
 *
 * @author zhangxu
 */
public interface IFriendExtService {
    Map<String, List<FriendExt>> getFriendExts(List<String> userIds);

    void addOrUpdate(List<FriendExt> exts);
}
