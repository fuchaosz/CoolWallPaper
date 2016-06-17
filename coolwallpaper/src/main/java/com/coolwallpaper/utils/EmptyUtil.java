package com.coolwallpaper.utils;

import java.util.List;

/**
 * 检查是否为空的工具
 * Created by fuchao on 2016/6/14.
 */
public class EmptyUtil {

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
}
