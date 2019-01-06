package com.richie.utils.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.richie.utils.greendao.generated.DaoMaster;
import com.richie.utils.greendao.generated.DaoSession;

/**
 * @author Richie on 2019.01.06
 */
public class GreenDaoUtil {
    private static final String DATABASE_NAME = "test.db";
    private DaoSession mDaoSession;

    private GreenDaoUtil() {
    }

    public static GreenDaoUtil getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void init(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(appContext, DATABASE_NAME);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private static class InstanceHolder {
        private static final GreenDaoUtil INSTANCE = new GreenDaoUtil();
    }
}
