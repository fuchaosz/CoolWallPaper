package com.coolwallpaper;

import android.app.Application;

import com.coolwallpaper.constant.Constant;
import com.orhanobut.logger.Logger;

import cn.bmob.v3.Bmob;

/**
 * Created by fuchao on 2015/10/30.
 */
public class MyApplication extends Application {

    public static final String TAG = "CoolWallPaper";
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //设置一下日志的TAG
        Logger.init(TAG);
        //初始化Bmob
        Bmob.initialize(this, Constant.BMOB_APPID);
    }

    /**
     * 获取全局的Application
     *
     * @return
     */
    public static MyApplication getInstance() {
        return myApplication;
    }
}
