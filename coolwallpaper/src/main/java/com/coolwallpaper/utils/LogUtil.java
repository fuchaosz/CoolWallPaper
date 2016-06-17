package com.coolwallpaper.utils;

import android.util.Log;

/**
 * 日志工具
 * Created by fuchao on 2016/6/7.
 */
public class LogUtil {

    public static String customTagPrefix = "John";

    public static boolean debug = true;
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;

    private LogUtil() {
    }

    public static void d(String content) {
        if (debug && allowD) {
            Log.d(getTag(), content);
        }
    }

    public static void e(String content) {
        if (debug && allowE) {
            Log.e(getTag(), content);
        }
    }

    public static void i(String content) {
        if (debug && allowI) {
            Log.i(getTag(), content);
        }
    }

    public static void v(String content) {
        if (debug && allowV) {
            Log.v(getTag(), content);
        }
    }

    public static void w(String content) {
        if (debug && allowW) {
            Log.w(getTag(), content);
        }
    }

    //带TAG
    public static void d(String TAG, String content) {
        if (debug && allowD) {
            Log.d(getTag(TAG), content);
        }
    }

    public static void e(String TAG, String content) {
        if (debug && allowE) {
            Log.e(getTag(TAG), content);
        }
    }

    public static void i(String TAG, String content) {
        if (debug && allowI) {
            Log.i(getTag(TAG), content);
        }
    }

    public static void v(String TAG, String content) {
        if (debug && allowV) {
            Log.v(getTag(TAG), content);
        }
    }

    public static void w(String TAG, String content) {
        if (debug && allowW) {
            Log.w(getTag(TAG), content);
        }
    }

    //生成TAG,前缀+TAG
    private static String getTag(String tag) {
        StringBuilder builder = new StringBuilder();
        if (customTagPrefix != null && customTagPrefix.length() > 0) {
            builder.append("[" + customTagPrefix + "]");
        }
        if (tag != null && tag.length() > 0) {
            builder.append(tag);
        }
        return builder.toString();
    }

    //生成默认的TAG,前缀+类名+方法名
    private static String getTag() {
        StringBuilder builder = new StringBuilder();
        if (customTagPrefix != null && customTagPrefix.length() > 0) {
            builder.append("[" + customTagPrefix + "]");
        }
        builder.append("[" + getCallerClassName() + "]");
        builder.append("[" + getCallerMethodName() + "]");
        return builder.toString();
    }

    //获取调用者的类名
    private static String getCallerClassName() {
        return Thread.currentThread().getStackTrace()[5].getClassName();
    }

    //获取调用这个的方法名称
    private static String getCallerMethodName() {
        return Thread.currentThread().getStackTrace()[5].getMethodName();
    }
}
