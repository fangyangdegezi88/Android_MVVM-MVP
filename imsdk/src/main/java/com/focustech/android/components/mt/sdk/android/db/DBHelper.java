package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.gen.DaoMaster;
import com.focustech.android.components.mt.sdk.android.db.gen.DaoSession;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultAccountService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultConversationService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultFriendExtService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultFriendGroupService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultFriendRelationshipService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultFriendService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultGroupService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultLastTimestampService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultMessageService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultSettingsService;
import com.focustech.android.components.mt.sdk.android.db.impl.DefaultSystemNotifyService;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * DB Helper
 *
 * @author zhangxu
 */
public class DBHelper {
    private static final String DB_NAME = "sdk.db";
    private static DaoSession instance = null;

    public static DaoSession getInstance() {
        if (MTRuntime.optionsSQL()) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }

        if (null == instance) {
            instance = new DaoMaster(MTRuntime.isDBUpgrade() ?
                    new PublishOpenHelper(ContextHolder.getAndroidContext(), DB_NAME, null).getWritableDatabase()
                    : new DaoMaster.DevOpenHelper(ContextHolder.getAndroidContext(), DB_NAME, null).getWritableDatabase()
            ).newSession();
        }

        return instance;
    }

    public static IAccountService getAccountService() {
        return DefaultAccountService.getInstance();
    }

    public static IFriendService getFriendService() {
        return DefaultFriendService.getInstance();
    }

    public static ILastTimestampService getLastTimestampService() {
        return DefaultLastTimestampService.getInstance();
    }

    public static ISettingService getSettingService() {
        return DefaultSettingsService.getInstance();
    }

    public static ISystemNotifyService getSystemNotifyService() {
        return DefaultSystemNotifyService.getInstance();
    }

    public static IFriendGroupService getFriendGroupService() {
        return DefaultFriendGroupService.getInstance();
    }

    public static IGroupService getGroupService() {
        return DefaultGroupService.getInstance();
    }

    public static IFriendRelationshipService getFriendRelationshipService() {
        return DefaultFriendRelationshipService.getInstance();
    }

    public static IMessageService getMessageService() {
        return DefaultMessageService.getInstance();
    }

    public static IConversationService getConversationService() {
        return DefaultConversationService.getInstance();
    }

    public static IFriendExtService getFriendExtService() {
        return DefaultFriendExtService.getInstance();
    }
}
