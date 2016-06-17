package com.coolwallpaper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.coolwallpaper.MyApplication;

/**
 * 网络工具
 * Created by fuchao on 2016/6/14.
 */
public class NetworkUtil {

    /**
     * 判断当前网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        //获取连接管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //当前网络是连接的
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
