package com.smkj.zzj.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.qihe.zzj.BaseApplication;


/**
 *
 */
public class MyDBManager {

    private Context mContext;
    private static final String DB_NAME = "test.db";
    private static MyDBManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private MyDBManager(Context context) {
        mContext = BaseApplication.getContext();
    }

    public static MyDBManager getInstace(Context context) {
        if (mDbManager == null) {
            synchronized (MyDBManager.class) {
                if (mDbManager == null) {
                    mDbManager = new MyDBManager(context);
                }
            }
        }
        return mDbManager;
    }

    /*获得可读数据库*/
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase sqLiteDatabase;
        if (mDevOpenHelper != null) {
            sqLiteDatabase = mDevOpenHelper.getReadableDatabase();
            return sqLiteDatabase;
        } else {
            return null;
        }
    }

    /*获得可写数据库*/
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sqLiteDatabase;
        if (mDevOpenHelper != null) {
            sqLiteDatabase = mDevOpenHelper.getWritableDatabase();
            return sqLiteDatabase;
        } else {
            return null;
        }
    }

    public MyDBManager getDevOpenHelper(String dbName) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        return mDbManager;
    }

    public DaoSession getSession() {
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }


}
