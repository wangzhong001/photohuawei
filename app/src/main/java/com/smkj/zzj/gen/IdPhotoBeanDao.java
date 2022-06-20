package com.smkj.zzj.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.qihe.zzj.bean.IdPhotoBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ID_PHOTO_BEAN".
*/
public class IdPhotoBeanDao extends AbstractDao<IdPhotoBean, Long> {

    public static final String TABLENAME = "ID_PHOTO_BEAN";

    /**
     * Properties of entity IdPhotoBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Size = new Property(1, String.class, "size", false, "SIZE");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property PxSize = new Property(3, String.class, "pxSize", false, "PX_SIZE");
        public final static Property MmSize = new Property(4, String.class, "mmSize", false, "MM_SIZE");
        public final static Property CreateTime = new Property(5, String.class, "createTime", false, "CREATE_TIME");
        public final static Property Name = new Property(6, String.class, "name", false, "NAME");
        public final static Property Money = new Property(7, String.class, "money", false, "MONEY");
        public final static Property EffectiveTime = new Property(8, Long.class, "effectiveTime", false, "EFFECTIVE_TIME");
        public final static Property IsPay = new Property(9, boolean.class, "isPay", false, "IS_PAY");
    }


    public IdPhotoBeanDao(DaoConfig config) {
        super(config);
    }
    
    public IdPhotoBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ID_PHOTO_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"SIZE\" TEXT NOT NULL ," + // 1: size
                "\"URL\" TEXT NOT NULL ," + // 2: url
                "\"PX_SIZE\" TEXT NOT NULL ," + // 3: pxSize
                "\"MM_SIZE\" TEXT NOT NULL ," + // 4: mmSize
                "\"CREATE_TIME\" TEXT NOT NULL ," + // 5: createTime
                "\"NAME\" TEXT NOT NULL ," + // 6: name
                "\"MONEY\" TEXT NOT NULL ," + // 7: money
                "\"EFFECTIVE_TIME\" INTEGER," + // 8: effectiveTime
                "\"IS_PAY\" INTEGER NOT NULL );"); // 9: isPay
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ID_PHOTO_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, IdPhotoBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getSize());
        stmt.bindString(3, entity.getUrl());
        stmt.bindString(4, entity.getPxSize());
        stmt.bindString(5, entity.getMmSize());
        stmt.bindString(6, entity.getCreateTime());
        stmt.bindString(7, entity.getName());
        stmt.bindString(8, entity.getMoney());
 
        Long effectiveTime = entity.getEffectiveTime();
        if (effectiveTime != null) {
            stmt.bindLong(9, effectiveTime);
        }
        stmt.bindLong(10, entity.getIsPay() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, IdPhotoBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getSize());
        stmt.bindString(3, entity.getUrl());
        stmt.bindString(4, entity.getPxSize());
        stmt.bindString(5, entity.getMmSize());
        stmt.bindString(6, entity.getCreateTime());
        stmt.bindString(7, entity.getName());
        stmt.bindString(8, entity.getMoney());
 
        Long effectiveTime = entity.getEffectiveTime();
        if (effectiveTime != null) {
            stmt.bindLong(9, effectiveTime);
        }
        stmt.bindLong(10, entity.getIsPay() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public IdPhotoBean readEntity(Cursor cursor, int offset) {
        IdPhotoBean entity = new IdPhotoBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getString(offset + 1), // size
            cursor.getString(offset + 2), // url
            cursor.getString(offset + 3), // pxSize
            cursor.getString(offset + 4), // mmSize
            cursor.getString(offset + 5), // createTime
            cursor.getString(offset + 6), // name
            cursor.getString(offset + 7), // money
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // effectiveTime
            cursor.getShort(offset + 9) != 0 // isPay
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, IdPhotoBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSize(cursor.getString(offset + 1));
        entity.setUrl(cursor.getString(offset + 2));
        entity.setPxSize(cursor.getString(offset + 3));
        entity.setMmSize(cursor.getString(offset + 4));
        entity.setCreateTime(cursor.getString(offset + 5));
        entity.setName(cursor.getString(offset + 6));
        entity.setMoney(cursor.getString(offset + 7));
        entity.setEffectiveTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setIsPay(cursor.getShort(offset + 9) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(IdPhotoBean entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(IdPhotoBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(IdPhotoBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}