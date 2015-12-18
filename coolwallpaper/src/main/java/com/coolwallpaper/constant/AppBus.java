package com.coolwallpaper.constant;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * otto事件总线的单例类。自带Handler，保证任何时候发送的消息都是发送到主线程中去执行的
 * Created by fuchao on 2015/11/23.
 */
public final class AppBus extends Bus {

    private static AppBus bus = new AppBus();
    private final Handler handler = new Handler(Looper.getMainLooper());

    //单例模式构造函数
    private AppBus() {
    }

    //重写post方法，保证消息一定是在主线程中接收
    @Override
    public void post(final Object event) {
        //当前线程是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        }
        //当前线程不是主线程
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AppBus.super.post(event);
                }
            });
        }

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
