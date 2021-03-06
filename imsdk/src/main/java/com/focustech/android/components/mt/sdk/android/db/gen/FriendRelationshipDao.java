package com.focustech.android.components.mt.sdk.android.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table FRIEND_RELATIONSHIP.
 */
public class FriendRelationshipDao extends AbstractDao<FriendRelationship, Long> {

    public static final String TABLENAME = "FRIEND_RELATIONSHIP";

    /**
     * Properties of entity FriendRelationship.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property FriendGroupId = new Property(2, String.class, "friendGroupId", false, "FRIEND_GROUP_ID");
        public final static Property FriendUserId = new Property(3, String.class, "friendUserId", false, "FRIEND_USER_ID");
    }

    ;


    public FriendRelationshipDao(DaoConfig config) {
        super(config);
    }

    public FriendRelationshipDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FRIEND_RELATIONSHIP' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' TEXT NOT NULL ," + // 1: userId
                "'FRIEND_GROUP_ID' TEXT NOT NULL ," + // 2: friendGroupId
                "'FRIEND_USER_ID' TEXT NOT NULL );"); // 3: friendUserId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FRIEND_RELATIONSHIP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FriendRelationship entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
        stmt.bindString(3, entity.getFriendGroupId());
        stmt.bindString(4, entity.getFriendUserId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FriendRelationship readEntity(Cursor cursor, int offset) {
        FriendRelationship entity = new FriendRelationship( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // userId
                cursor.getString(offset + 2), // friendGroupId
                cursor.getString(offset + 3) // friendUserId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FriendRelationship entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setFriendGroupId(cursor.getString(offset + 2));
        entity.setFriendUserId(cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FriendRelationship entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FriendRelationship entity) {
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
