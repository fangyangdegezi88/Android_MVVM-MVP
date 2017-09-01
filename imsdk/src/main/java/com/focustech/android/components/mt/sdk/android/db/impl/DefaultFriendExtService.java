package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IFriendExtService;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendExt;
import com.focustech.android.components.mt.sdk.android.db.gen.FriendExtDao;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxu
 */
public class DefaultFriendExtService implements IFriendExtService {
    private static FriendExtDao dao = DBHelper.getInstance().getFriendExtDao();

    private static final IFriendExtService INSTANCE = new DefaultFriendExtService();

    public static IFriendExtService getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, List<FriendExt>> getFriendExts(List<String> userIds) {
        if (null == userIds || userIds.size() == 0) {
            return new HashMap<>();
        }

        List<FriendExt> exts = dao.queryBuilder().where(FriendExtDao.Properties.FriendUserId.in(userIds)).list();
        Map<String, List<FriendExt>> value = new HashMap<>();

        for (FriendExt ext : exts) {
            if (!value.containsKey(ext.getFriendUserId())) {
                value.put(ext.getFriendUserId(), new ArrayList<FriendExt>());
            }

            value.get(ext.getFriendUserId()).add(ext);
        }

        return value;
    }

    @Override
    public void addOrUpdate(final List<FriendExt> exts) {
        DBHelper.getInstance().runInTx(new Runnable() {
            @Override
            public void run() {
                for (FriendExt ext : exts) {
                    addOrUpdate(ext);
                }
            }
        });
    }

    private void addOrUpdate(FriendExt ext) {
        FriendExt old = dao.queryBuilder().where(FriendExtDao.Properties.FriendUserId.eq(ext.getFriendUserId()), FriendExtDao.Properties.Name.eq(ext.getName())).unique();

        if (old != null) {
            ReflectionUtil.copyProperties(ext, old);
        } else {
            old = ext;
        }

        dao.insertOrReplace(old);
    }
}
