package com.coolwallpaper;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by fuchao on 2015/10/30.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //配置Stetho，用于在Chrome上调试APP
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
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
