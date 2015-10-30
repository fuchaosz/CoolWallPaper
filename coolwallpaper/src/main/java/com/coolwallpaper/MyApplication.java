package com.coolwallpaper;

import android.app.Application;

/**
 * Created by fuchao on 2015/10/30.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
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
