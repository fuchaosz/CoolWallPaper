package com.coolwallpaper.bean;

/**
 * 搜狗图片的基本请求参数.搜狗图片比百度图片好用多了，没有无效图片,外链一目了然,百度图片搜索后居然还加密，fuck
 * Created by fuchao on 2016/4/19.
 */
public abstract class SogouBaseRequestParam extends BaseRequestParam {

    protected String baseUrl = "http://pic.sogou.com/pics/channel/getAllRecomPicByTag.jsp";

    /**
     * 范围.例如：壁纸、明星
     */
    protected String category;

    /**
     * 二级标题,例如：壁纸下面的世界风光栏目，category = 壁纸 ,tag = 世界风光
     */
    protected String tag;

    /**
     * 宽度
     */
    protected int width;

    /**
     * 高度
     */
    protected int height;

    /**
     * 每页显示的图片数量,默认15
     */
    protected int len = 15;

    /**
     * 起始的位置
     */
    protected int start = 0;

    /**
     * 获取url的方法，由子类去实现
     *
     * @return
     */
    public abstract String getUrl();

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getTitle1() {
        return category;
    }

    @Override
    public String getTitle2() {
        return tag;
    }

    @Override
    public int getPageSize() {
        return len;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.len = pageSize;
    }

    @Override
    public int getPage() {
        return start / len;
    }

    @Override
    public void setPage(int page) {
       if(page < 0){
           throw  new IllegalArgumentException("page < 0");
       }
        start = page * len;
    }
}
