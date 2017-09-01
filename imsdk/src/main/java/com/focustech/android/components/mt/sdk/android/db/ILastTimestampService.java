package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.LastTimestamp;

import java.util.List;

/**
 * 最后时间戳
 *
 * @author zhangxu
 */
public interface ILastTimestampService {
    void saveLastRegistryTime();

    long getLastRegistryTime();

    /**
     * 更新最后时间戳
     *
     * @param lastTimestamp
     */
    void addOrUpdate(LastTimestamp lastTimestamp);

    /**
     * 数据最后更新时间
     *
     * @param userId 用户ID
     * @param type   类型
     * @return
     */
    long getDataTimestamp(String userId, long type);

    /**
     * 数据最后更新时间
     *
     * @param userId 用户ID
     * @param type   类型
     * @return
     */
    long getDataTimestamp(String userId, String contactId, long type);

    /**
     * 数据最后更新时间
     *
     * @param userId 用户ID
     * @param types  类型
     * @return
     */
    List<LastTimestamp> getDataTimestampList(String userId, long... types);
}
