package com.coolwallpaper.bean;

import java.net.URLEncoder;

/**
 * 获取壁纸的请求参数
 * Created by John on 2015/9/29.
 * 该为从搜狗壁纸获取壁纸
 * Modify by fuchao on 2015/9/22
 */
public class WallPaperRequetParam extends SogouBaseRequestParam {

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
        this.category = title1;
        this.tag = title2;
        this.start = page;
        this.len = pageSize;
    }


    @Override
    public String getUrl() {
        //组装参数,组合成URL
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(baseUrl);
            builder.append("?category="+URLEncoder.encode(category,"UTF-8"));
            builder.append("&tag=" + URLEncoder.encode(tag,"UTF-8"));
            builder.append("&start=" + start);
            builder.append("&len=" + len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
