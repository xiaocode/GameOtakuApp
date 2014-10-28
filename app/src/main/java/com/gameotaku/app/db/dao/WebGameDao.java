package com.gameotaku.app.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table WEB_GAME.
 */
public class WebGameDao extends AbstractDao<WebGame, Long> {

    public static final String TABLENAME = "WEB_GAME";

    public WebGameDao(DaoConfig config) {
        super(config);
    }

    ;


    public WebGameDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'WEB_GAME' (" + //
                "'LOCAL_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: local_id
                "'ITEM_ID' INTEGER NOT NULL ," + // 1: item_id
                "'REQUEST_URL' TEXT NOT NULL ," + // 2: request_url
                "'NAME' TEXT NOT NULL ," + // 3: name
                "'ICON' TEXT," + // 4: icon
                "'SIZE' INTEGER," + // 5: size
                "'LANGUAGE' TEXT," + // 6: language
                "'TYPE' TEXT," + // 7: type
                "'DESC' TEXT," + // 8: desc
                "'CHARGE_TYPE' INTEGER," + // 9: charge_type
                "'CHARGE_FEE' INTEGER," + // 10: charge_fee
                "'ORIENTATION' INTEGER," + // 11: orientation
                "'CREATE_TIME' INTEGER," + // 12: create_time
                "'UPDATE_TIME' INTEGER," + // 13: update_time
                "'VERSION_NAME' TEXT," + // 14: version_name
                "'VERSION_CODE' INTEGER," + // 15: version_code
                "'PLAYER_COUNT' INTEGER," + // 16: player_count
                "'HEAT' INTEGER," + // 17: heat
                "'RATE' INTEGER," + // 18: rate
                "'STATUS' INTEGER," + // 19: status
                "'AUTHOR_ID' INTEGER," + // 20: author_id
                "'AUTHOR_TYPE' INTEGER," + // 21: author_type
                "'AUTHOR_NAME' TEXT," + // 22: author_name
                "'EXT_URL' TEXT);"); // 23: ext_url
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WEB_GAME'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, WebGame entity) {
        stmt.clearBindings();

        Long local_id = entity.getLocal_id();
        if (local_id != null) {
            stmt.bindLong(1, local_id);
        }
        stmt.bindLong(2, entity.getItem_id());
        stmt.bindString(3, entity.getRequest_url());
        stmt.bindString(4, entity.getName());

        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }

        Long size = entity.getSize();
        if (size != null) {
            stmt.bindLong(6, size);
        }

        String language = entity.getLanguage();
        if (language != null) {
            stmt.bindString(7, language);
        }

        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }

        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(9, desc);
        }

        Integer charge_type = entity.getCharge_type();
        if (charge_type != null) {
            stmt.bindLong(10, charge_type);
        }

        Integer charge_fee = entity.getCharge_fee();
        if (charge_fee != null) {
            stmt.bindLong(11, charge_fee);
        }

        Boolean orientation = entity.getOrientation();
        if (orientation != null) {
            stmt.bindLong(12, orientation ? 1l : 0l);
        }

        Long create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindLong(13, create_time);
        }

        Long update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindLong(14, update_time);
        }

        String version_name = entity.getVersion_name();
        if (version_name != null) {
            stmt.bindString(15, version_name);
        }

        Integer version_code = entity.getVersion_code();
        if (version_code != null) {
            stmt.bindLong(16, version_code);
        }

        Integer player_count = entity.getPlayer_count();
        if (player_count != null) {
            stmt.bindLong(17, player_count);
        }

        Integer heat = entity.getHeat();
        if (heat != null) {
            stmt.bindLong(18, heat);
        }

        Integer rate = entity.getRate();
        if (rate != null) {
            stmt.bindLong(19, rate);
        }

        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(20, status);
        }

        Long author_id = entity.getAuthor_id();
        if (author_id != null) {
            stmt.bindLong(21, author_id);
        }

        Boolean author_type = entity.getAuthor_type();
        if (author_type != null) {
            stmt.bindLong(22, author_type ? 1l : 0l);
        }

        String author_name = entity.getAuthor_name();
        if (author_name != null) {
            stmt.bindString(23, author_name);
        }

        String ext_url = entity.getExt_url();
        if (ext_url != null) {
            stmt.bindString(24, ext_url);
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public WebGame readEntity(Cursor cursor, int offset) {
        WebGame entity = new WebGame( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // local_id
                cursor.getLong(offset + 1), // item_id
                cursor.getString(offset + 2), // request_url
                cursor.getString(offset + 3), // name
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // icon
                cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // size
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // language
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // type
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // desc
                cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // charge_type
                cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // charge_fee
                cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0, // orientation
                cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12), // create_time
                cursor.isNull(offset + 13) ? null : cursor.getLong(offset + 13), // update_time
                cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // version_name
                cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // version_code
                cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // player_count
                cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17), // heat
                cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18), // rate
                cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // status
                cursor.isNull(offset + 20) ? null : cursor.getLong(offset + 20), // author_id
                cursor.isNull(offset + 21) ? null : cursor.getShort(offset + 21) != 0, // author_type
                cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // author_name
                cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23) // ext_url
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, WebGame entity, int offset) {
        entity.setLocal_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setItem_id(cursor.getLong(offset + 1));
        entity.setRequest_url(cursor.getString(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSize(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setLanguage(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDesc(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCharge_type(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setCharge_fee(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setOrientation(cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0);
        entity.setCreate_time(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
        entity.setUpdate_time(cursor.isNull(offset + 13) ? null : cursor.getLong(offset + 13));
        entity.setVersion_name(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setVersion_code(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setPlayer_count(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setHeat(cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17));
        entity.setRate(cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18));
        entity.setStatus(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
        entity.setAuthor_id(cursor.isNull(offset + 20) ? null : cursor.getLong(offset + 20));
        entity.setAuthor_type(cursor.isNull(offset + 21) ? null : cursor.getShort(offset + 21) != 0);
        entity.setAuthor_name(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setExt_url(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(WebGame entity, long rowId) {
        entity.setLocal_id(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(WebGame entity) {
        if (entity != null) {
            return entity.getLocal_id();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Properties of entity WebGame.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Local_id = new Property(0, Long.class, "local_id", true, "LOCAL_ID");
        public final static Property Item_id = new Property(1, long.class, "item_id", false, "ITEM_ID");
        public final static Property Request_url = new Property(2, String.class, "request_url", false, "REQUEST_URL");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Icon = new Property(4, String.class, "icon", false, "ICON");
        public final static Property Size = new Property(5, Long.class, "size", false, "SIZE");
        public final static Property Language = new Property(6, String.class, "language", false, "LANGUAGE");
        public final static Property Type = new Property(7, String.class, "type", false, "TYPE");
        public final static Property Desc = new Property(8, String.class, "desc", false, "DESC");
        public final static Property Charge_type = new Property(9, Integer.class, "charge_type", false, "CHARGE_TYPE");
        public final static Property Charge_fee = new Property(10, Integer.class, "charge_fee", false, "CHARGE_FEE");
        public final static Property Orientation = new Property(11, Boolean.class, "orientation", false, "ORIENTATION");
        public final static Property Create_time = new Property(12, Long.class, "create_time", false, "CREATE_TIME");
        public final static Property Update_time = new Property(13, Long.class, "update_time", false, "UPDATE_TIME");
        public final static Property Version_name = new Property(14, String.class, "version_name", false, "VERSION_NAME");
        public final static Property Version_code = new Property(15, Integer.class, "version_code", false, "VERSION_CODE");
        public final static Property Player_count = new Property(16, Integer.class, "player_count", false, "PLAYER_COUNT");
        public final static Property Heat = new Property(17, Integer.class, "heat", false, "HEAT");
        public final static Property Rate = new Property(18, Integer.class, "rate", false, "RATE");
        public final static Property Status = new Property(19, Integer.class, "status", false, "STATUS");
        public final static Property Author_id = new Property(20, Long.class, "author_id", false, "AUTHOR_ID");
        public final static Property Author_type = new Property(21, Boolean.class, "author_type", false, "AUTHOR_TYPE");
        public final static Property Author_name = new Property(22, String.class, "author_name", false, "AUTHOR_NAME");
        public final static Property Ext_url = new Property(23, String.class, "ext_url", false, "EXT_URL");
    }

}