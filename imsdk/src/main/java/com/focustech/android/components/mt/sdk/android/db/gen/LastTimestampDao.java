package com.focustech.android.components.mt.sdk.android.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table LAST_TIMESTAMP.
 */
public class LastTimestampDao extends AbstractDao<LastTimestamp, Long> {

    public static final String TABLENAME = "LAST_TIMESTAMP";

    /**
     * Properties of entity LastTimestamp.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Type = new Property(2, long.class, "type", false, "TYPE");
        public final static Property Timestamp = new Property(3, long.class, "timestamp", false, "TIMESTAMP");
        public final static Property ContactId = new Property(4, String.class, "contactId", false, "CONTACT_ID");
    }

    ;


    public LastTimestampDao(DaoConfig config) {
        super(config);
    }

    public LastTimestampDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LAST_TIMESTAMP' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' TEXT NOT NULL ," + // 1: userId
                "'TYPE' INTEGER NOT NULL ," + // 2: type
                "'TIMESTAMP' INTEGER NOT NULL ," + // 3: timestamp
                "'CONTACT_ID' TEXT);"); // 4: contactId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LAST_TIMESTAMP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LastTimestamp entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
        stmt.bindLong(3, entity.getType());
        stmt.bindLong(4, entity.getTimestamp());
 
        String contactId = entity.getContactId();
        if (contactId != null) {
            stmt.bindString(5, contactId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LastTimestamp readEntity(Cursor cursor, int offset) {
        LastTimestamp entity = new LastTimestamp( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // userId
                cursor.getLong(offset + 2), // type
                cursor.getLong(offset + 3), // timestamp
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // contactId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LastTimestamp entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setType(cursor.getLong(offset + 2));
        entity.setTimestamp(cursor.getLong(offset + 3));
        entity.setContactId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(LastTimestamp entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(LastTimestamp entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
