package com.coolwallpaper.bean;

import java.net.URLEncoder;

/**
 * 获取壁纸的请求参数
 * Created by John on 2015/9/29.
 */
public class WallPaperRequetParam extends BaseRequestParam {


    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(baseUrl);
            builder.append("?app=" + app);
            builder.append("&col=" + URLEncoder.encode(col, "UTF-8"));
            builder.append("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
