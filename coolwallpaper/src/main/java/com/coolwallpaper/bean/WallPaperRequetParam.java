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
     * 构造函数.默认每页30个,默认显示全部
     *
     * @param page 起始页数
     */
    public WallPaperRequetParam(int page) {
        this("", "", page, 30);
    }

    /**
     * 构造函数
     *
     * @param title1 一级标题,例如:风景
     * @param title2 二级标题,例如:雪景
     * @param page
     */
    public WallPaperRequetParam(String title1, String title2, int page, int pageSize) {
        this.title1 = title1;
        this.title2 = title2;
        this.pn = page;
        this.rn = pageSize;
    }


    @Override
    public String getUrl() {
        //组装参数,组合成URL
        StringBuilder builder = new StringBuilder();
        word = "壁纸 " + title1 + " " + title2;
        try {
            builder.append(baseUrl);
            builder.append("?tn=" + tn);
            builder.append("&ipn=" + ipn);
            builder.append("&fp=" + fp);
            builder.append("&cl=" + cl);
            builder.append("&lm=" + lm);
            builder.append("&ie=" + ie);
            builder.append("&oe=" + oe);
            builder.append("&st=" + st);
            builder.append("&z=" + z);
            builder.append("&ic=" + ic);
            builder.append("&word=" + URLEncoder.encode(word.trim(), "UTF-8"));
            builder.append("&face=" + face);
            builder.append("&istype=" + istype);
            builder.append("&nc=" + nc);
            builder.append("&pn=" + pn);
            builder.append("&rn=" + rn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
