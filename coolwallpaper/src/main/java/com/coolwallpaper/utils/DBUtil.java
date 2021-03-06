package com.coolwallpaper.utils;

import android.database.sqlite.SQLiteDatabase;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.constant.ConstDB;
import com.coolwallpaper.model.DaoMaster;
import com.coolwallpaper.model.DaoSession;
import com.coolwallpaper.model.LocalFavouriteDao;
import com.coolwallpaper.model.LocalPictureDao;
import com.coolwallpaper.model.ParamDao;
import com.coolwallpaper.model.PictureDao;

/**
 * 对GreenDao再做一下封装,这个值封装最基本的数据库类
 * Created by fuchao on 2015/12/15.
 */
public class DBUtil {

    private static DBUtil dbUtil;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private PictureDao pictureDao;//数据库实体类的操作类
    private ParamDao paramDao;//参数的操作类
    private LocalPictureDao localPictureDao;//本地图片实体类操作类
    private SQLiteDatabase db;//实际的数据库
    private LocalFavouriteDao localFavouriteDao;//本地收藏表

    /**
     * 私有构造函数
     */
    private DBUtil() {
        this.init();
    }

    //获取单例
    public static DBUtil getInstance() {
        if (dbUtil == null) {
            dbUtil = new DBUtil();
        }
        return dbUtil;
    }

    //初始化数据库
    private void init() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getInstance(), ConstDB.DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取PictureDao,采用懒加载的方式
     *
     * @return
     */
    public synchronized PictureDao getPictureDao() {
        if (pictureDao == null) {
            pictureDao = daoSession.getPictureDao();
        }
        return pictureDao;
    }

    /**
     * 获取ParamDao，采用懒加载的方式
     *
     * @return
     */
    public synchronized ParamDao getParamDao() {
        if (paramDao == null) {
            paramDao = daoSession.getParamDao();
        }
        return paramDao;
    }

    /**
     * 获取LocalPictureDao，采用懒加载的方式
     *
     * @return
     */
    public synchronized LocalPictureDao getLocalPictureDao() {
        if (localPictureDao == null) {
            localPictureDao = daoSession.getLocalPictureDao();
        }
        return localPictureDao;
    }

    public synchronized LocalFavouriteDao getLocalFavouriteDao() {
        if (localFavouriteDao == null) {
            localFavouriteDao = daoSession.getLocalFavouriteDao();
        }
        return localFavouriteDao;
    }
}
