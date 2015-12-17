package com.coolwallpaper.utils;

import com.coolwallpaper.bean.PictureResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片JSON解析工具
 * Created by fuchao on 2015/9/30.
 */
public class PictureParseUtil {

    /**
     * 解析从网络上下载的壁纸的Json数据
     *
     * @param jsonArrayStr json字符串
     * @return
     */
    public static List<PictureResult> parse(String jsonArrayStr) {
        if (jsonArrayStr == null) {
            return null;
        }
        List<PictureResult> beanList = new ArrayList<>();
        try {
            //注意：图片都保存在imgs属性中
            JSONObject jo = new JSONObject(jsonArrayStr);
            JSONArray jsonArray = new JSONArray(jo.getString("imgs"));
            Gson gson = new Gson();
            //遍历，使用Gson将json直接解析成bean
            for (int i = 0; i < jsonArray.length(); i++) {
                String tmp = jsonArray.get(i).toString();
                PictureResult bean = gson.fromJson(tmp, PictureResult.class);
                beanList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }

}
