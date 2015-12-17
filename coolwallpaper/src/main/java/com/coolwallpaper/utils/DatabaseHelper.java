package com.coolwallpaper.utils;

import android.database.sqlite.SQLiteDatabase;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.constant.ConstDB;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * 数据库操作类。使用Ormlite框架
 * Created by fuchao on 2015/12/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    //    private static DatabaseHelper singleInstance;
    //    private Dao<PictureResult, Integer> pictureDao;
    
    //私有构造函数
    private DatabaseHelper() {
        super(MyApplication.getInstance(), ConstDB.DB_NAME, null, 1);
    }

    //    //获取单例
    //    public static DatabaseHelper getInstance() {
    //        if (singleInstance == null) {
    //            singleInstance = new DatabaseHelper();
    //        }
    //        return singleInstance;
    //    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PictureResult.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, PictureResult.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    /**
    //     * 获取操作PictureBean的类，PictureDao
    //     *
    //     * @return
    //     */
    //    public Dao<PictureResult, Integer> getPictureDao() {
    //        if (pictureDao == null) {
    //            pictureDao = getPictureDao();
    //        }
    //        return pictureDao;
    //    }
    //
    //    //释放资源
    //    @Override
    //    public void close() {
    //        super.close();
    //        pictureDao = null;
    //    }
}
