package com.coolwallpaper.utils;

import android.widget.Toast;

import com.coolwallpaper.MyApplication;

/**
 * Created by fuchao on 2016/6/1.
 */
public class ToastUtil {

    public static boolean DEBUG = true;

    /**
     * 调试模式的Toast
     *
     * @param msg
     */
    public static void showDebug(String msg) {
        if (DEBUG) {
            Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 正式环境使用的Toast
     *
     * @param msg
     */
    public static void show(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
