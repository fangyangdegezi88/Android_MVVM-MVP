package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;

/**
 * 系统通知
 *
 * @author zhangxu
 */
public interface ISystemNotifyService {
    void add(SystemNotify notify);

    boolean isProcessed(String userId, String cmd);

    boolean isProcessed(String userId, String cmd, Long type, String contactId);

    boolean isProcessed(String userId, String cmd, Long type, String contactId, String relatedId);

    void processed(String userId, String cmd);

    void processed(String userId, String cmd, Long type, String contactId);

    void processed(String userId, String cmd, Long type, String contactId, String relatedId);

    void processed(SystemNotify notify);
}
