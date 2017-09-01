package com.focustech.android.components.mt.sdk.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.focustech.android.components.mt.sdk.android.db.gen.DaoMaster;

/**
 * release模式下的DB HELPER，做一些数据更新操作
 *
 * @author zhangxu
 */
public class PublishOpenHelper extends DaoMaster.OpenHelper {
    public PublishOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建不存在的表
        DaoMaster.createAllTables(db, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 根据版本升级
    }
}
