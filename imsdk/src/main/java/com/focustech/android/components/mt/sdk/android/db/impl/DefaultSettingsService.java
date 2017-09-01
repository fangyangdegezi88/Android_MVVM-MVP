package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.ISettingService;
import com.focustech.android.components.mt.sdk.android.db.gen.Settings;
import com.focustech.android.components.mt.sdk.android.db.gen.SettingsDao;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

/**
 * @author zhangxu
 */
public class DefaultSettingsService implements ISettingService {
    private static SettingsDao dao = DBHelper.getInstance().getSettingsDao();

    private static final ISettingService INSTANCE = new DefaultSettingsService();

    public static ISettingService getInstance() {
        return INSTANCE;
    }

    public Settings addOrUpdate(String userId, Settings settings) {
        Settings old = dao.load(userId);

        if (old != null) {
            ReflectionUtil.copyProperties(settings, old);
            settings = old;
        }

        dao.insertOrReplace(settings);

        return settings;
    }
}
