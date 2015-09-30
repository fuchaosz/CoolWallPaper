package com.coolwallpaper.bean;

import java.net.URLEncoder;

/**
 * 获取壁纸的请求参数
 * Created by John on 2015/9/29.
 */
public class WallPaperRequetParam extends BaseRequestParam {

    /**
     * 默认构造函数,显示壁纸--全部
     */
    public WallPaperRequetParam() {
        this(0);
    }

    /**
     * 构造函数.默认每页30个
     *
     * @param page 起始页数
     */
    public WallPaperRequetParam(int page) {
        this("全部", "", page, 30);
    }

    /**
     * 构造函数
     *
     * @param title1
     * @param title2
     * @param page
     */
    public WallPaperRequetParam(String title1, String title2, int page, int pageSize) {
        super("壁纸", title1, title2, page, pageSize);
    }


    @Override
    public String getUrl() {
        //组装参数,组合成URL
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(baseUrl);
            builder.append("?app=" + app);
            builder.append("&col=" + URLEncoder.encode(col, "UTF-8"));
            builder.append("&fr=" + fr);
            builder.append("&from=" + from);
            builder.append("&height=" + height);
            builder.append("&ic=" + ic);
            builder.append("&ie=" + ie);
            builder.append("&image_id=" + imageId);
            builder.append("&oe=" + oe);
            builder.append("&p=" + p);
            builder.append("&pn=" + pn);
            builder.append("&rn=" + rn);
            builder.append("&tag=" + tag);
            builder.append("&tag3=" + tag3);
            builder.append("&width=" + width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
