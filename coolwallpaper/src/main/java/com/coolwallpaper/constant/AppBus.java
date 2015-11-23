package com.coolwallpaper.constant;

import com.squareup.otto.Bus;

/**
 * otto事件总线的单例类
 * Created by fuchao on 2015/11/23.
 */
public class AppBus extends Bus {

    private static AppBus bus = new AppBus();

    //单例模式构造函数
    private AppBus() {

    }

    /**
     * 获取单例的方法
     *
     * @return
     */
    public static AppBus getInstance() {
        return bus;
    }


}
