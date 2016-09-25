package com.coolwallpaper.utils;

import android.support.annotation.NonNull;

import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.bean.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.library.common.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片JSON解析工具
 * Created by fuchao on 2015/9/30.
 */
public class PictureParseUtil {

    /**
     * 解析从百度上下载的壁纸的Json数据
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
            JSONArray jsonArray = new JSONArray(jo.getString("all_items"));
            Gson gson = new Gson();
            //遍历，使用Gson将json直接解析成bean
            for (int i = 0; i < jsonArray.length(); i++) {
                //判断json是否为空
                JSONObject joTmp = jsonArray.getJSONObject(i);
                if (joTmp.length() == 0) {
                    //json为空则跳过
                    continue;
                }
                //处理一下tags标签
                JSONArray arrayTags = joTmp.getJSONArray("tags");
                String tmpTags = "";
                if (arrayTags != null) {
                    for (int j = 0; j < arrayTags.length(); j++) {
                        String tagStr = arrayTags.getString(j);
                        //将每个tag用空格隔开
                        tmpTags = tmpTags + " " + tagStr;
                    }
                    //最后去除掉末尾的空格
                    tmpTags = tmpTags.trim();
                }
                //替换掉原来的tags
                joTmp.put("tags",tmpTags);
                String tmp = joTmp.toString();
                PictureResult bean = gson.fromJson(tmp, PictureResult.class);
                beanList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }

    /**
     * 解析从搜狗图片搜索后的结果
     *
     * @param jsonObjectStr 搜狗图片搜索返回的原始JSON数据
     * @return
     */
    public static List<SearchResult> parseSearchResult(@NonNull String jsonObjectStr) {
        List<SearchResult> beanList = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(jsonObjectStr);
            if (jo == null) {
                return beanList;
            }
            //图片数组在items中
            JSONArray jsonArray = jo.getJSONArray("items");
            if (jsonArray == null) {
                return beanList;
            }
            Gson gson = new Gson();
            //遍历
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject joTmp = jsonArray.getJSONObject(i);
                SearchResult bean = gson.fromJson(joTmp.toString(), SearchResult.class);
                beanList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }

}
