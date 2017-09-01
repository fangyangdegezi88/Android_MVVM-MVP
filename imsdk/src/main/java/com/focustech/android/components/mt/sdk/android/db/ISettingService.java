package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Settings;

/**
 * 设置
 *
 * @author zhangxu
 */
public interface ISettingService {
    Settings addOrUpdate(String userId, Settings settings);
}
