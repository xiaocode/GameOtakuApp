package com.gameotaku.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gameotaku.app.Injector;
import com.gameotaku.app.db.dao.DaoMaster;
import com.gameotaku.app.db.dao.DaoSession;

import javax.inject.Inject;

/**
 * Created by Vincent on 2014/5/29.
 */
public class DbHelper {
    private static DbHelper instance;
    @Inject
    Context mContext;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase db;

    public DbHelper() {
        Injector.inject(this);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "dudulu", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public synchronized static DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
