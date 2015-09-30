package com.coolwallpaper.utils;

import com.coolwallpaper.bean.PictureBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片JSON解析工具
 * Created by fuchao on 2015/9/30.
 */
public class PictureParseUtil {

    public static List<PictureBean> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        List<PictureBean> beanList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

        }
        return beanList;
    }

    public static PictureBean parsePictureBean(JSONObject jo) {
        PictureBean bean = new PictureBean();
        //bean.setId(Gson);
        return bean;
    }


}
