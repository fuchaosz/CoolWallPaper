package com.coolwallpaper.bean;

import java.io.Serializable;

/**
 * 获取百度图片的请求参数的基类
 * Created by fuchao on 2015/9/29.
 */
public abstract class BaseRequestParam implements Serializable {

    /**
     * 基URL
     */
    protected String baseUrl = "http://image.baidu.com/data/imgs";

    /**
     * 非必须参数，尽在“壁纸”这个条目中用到
     */
    protected String app = "img.browse.channel.wallpaper";

    /**
     * 总的分类，就是百度图片首页下面的大分类条目，但不是每个条目的URL的组装规则都是一样的
     * 注意：这里一般是中文,需要用URLEncoder转换
     */
    protected String col;

    protected String fr = "channel";

    /**
     * 默认是1
     */
    protected int from = 1;

    /**
     * 图片高度
     */
    protected int height = 1080;

    /**
     * 图片宽度
     */
    protected int width = 1920;

    protected String ic = "0";

    /**
     * 输入编码
     */
    protected String ie = "utf-8";

    /**
     * 图片ID
     */
    protected String imageId = "";

    /**
     * 输出编码
     */
    protected String oe = "utf-8";

    protected String p = "channel";

    /**
     * 页码，从0开始
     */
    protected int pn = 0;

    /**
     * 每页显示的图片数
     */
    protected int rn = 36;

    /**
     * 二级分类，例如：壁纸-->风景,tag=风景
     * 注意：这里一般是中文,需要用URLEncoder转换
     */
    protected String tag;

    /**
     * 三级分类，例如:壁纸-->风景-->自然风光,tag3=自然风光
     * 注意：这里一般是中文,需要用URLEncoder转换
     */
    protected String tag3;

    /**
     * 默认构造函数
     */
    public BaseRequestParam() {
    }

    /**
     * 构造函数
     *
     * @param col
     * @param tag
     * @param tag3
     * @param page
     * @param pageSize
     */
    public BaseRequestParam(String col, String tag, String tag3, int page, int pageSize) {
        this.col = col;
        this.tag = tag;
        this.tag3 = tag3;
        this.pn = page;
        this.rn = pageSize;
    }


    /**
     * 获取图片URL。具体的URL生成方法交给子类去实现
     *
     * @return
     */
    public abstract String getUrl();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }
}
