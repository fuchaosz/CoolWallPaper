package com.coolwallpaper.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * json转换工具
 * Created by fuchao on 2016/6/12.
 */
public class JsonUtil {

    private static Gson gson = new Gson();

    /**
     * 将Json数组转换为List
     *
     * @param jsonArrayStr 必须是JsonArray的字符串
     * @param t            类型
     * @param <T>          JavaBean类型
     * @return JavaBean列表
     */
    public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> t) {
        Type type = new TypeToken<T>() {
        }.getType();
        List<T> list = null;
        try {
            list = gson.fromJson(jsonArrayStr, type);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

}
