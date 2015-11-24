package com.coolwallpaper.service;

import android.app.Service;

import com.coolwallpaper.constant.AppBus;

/**
 * 基础服务,便于以后扩展
 * Created by fuchao on 2015/11/24.
 */
public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //注册otto
        AppBus.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        //取消otto注册
        AppBus.getInstance().unregister(this);
        super.onDestroy();
    }
}
