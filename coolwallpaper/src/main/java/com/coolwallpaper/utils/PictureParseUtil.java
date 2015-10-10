package com.coolwallpaper.utils;

import com.coolwallpaper.bean.PictureBean;
import com.google.gson.Gson;

import org.json.JSONArray;

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
    public static List<PictureBean> parse(String jsonArrayStr) {
        if (jsonArrayStr == null) {
            return null;
        }
        List<PictureBean> beanList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayStr);
            Gson gson = new Gson();
            //遍历，使用Gson将json直接解析成bean
            for (int i = 0; i < jsonArray.length(); i++) {
                PictureBean bean = gson.fromJson(jsonArray.get(i).toString(), PictureBean.class);
                beanList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }

}
