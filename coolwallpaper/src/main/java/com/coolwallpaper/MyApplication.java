package com.coolwallpaper;

import android.app.Application;

import com.orhanobut.logger.Logger;

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
