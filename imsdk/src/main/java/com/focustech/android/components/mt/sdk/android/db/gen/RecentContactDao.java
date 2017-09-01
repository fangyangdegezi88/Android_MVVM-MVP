package com.focustech.android.components.mt.sdk.android.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table RECENT_CONTACT.
 */
public class RecentContactDao extends AbstractDao<RecentContact, Long> {

    public static final String TABLENAME = "RECENT_CONTACT";

    /**
     * Properties of entity RecentContact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property ContactType = new Property(2, long.class, "contactType", false, "CONTACT_TYPE");
        public final static Property ContactId = new Property(3, String.class, "contactId", false, "CONTACT_ID");
        public final static Property Timestamp = new Property(4, long.class, "timestamp", false, "TIMESTAMP");
    }

    ;


    public RecentContactDao(DaoConfig config) {
        super(config);
    }

    public RecentContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RECENT_CONTACT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' TEXT NOT NULL ," + // 1: userId
                "'CONTACT_TYPE' INTEGER NOT NULL ," + // 2: contactType
                "'CONTACT_ID' TEXT NOT NULL ," + // 3: contactId
                "'TIMESTAMP' INTEGER NOT NULL );"); // 4: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RECENT_CONTACT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RecentContact entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
        stmt.bindLong(3, entity.getContactType());
        stmt.bindString(4, entity.getContactId());
        stmt.bindLong(5, entity.getTimestamp());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RecentContact readEntity(Cursor cursor, int offset) {
        RecentContact entity = new RecentContact( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // userId
                cursor.getLong(offset + 2), // contactType
                cursor.getString(offset + 3), // contactId
                cursor.getLong(offset + 4) // timestamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RecentContact entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setContactType(cursor.getLong(offset + 2));
        entity.setContactId(cursor.getString(offset + 3));
        entity.setTimestamp(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RecentContact entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RecentContact entity) {
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