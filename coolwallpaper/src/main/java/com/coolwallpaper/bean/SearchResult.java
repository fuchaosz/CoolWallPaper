package com.coolwallpaper.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 搜狗图片搜索后返回的结果
 * Created by fuchao on 2016/4/19.
 */
public class SearchResult implements Serializable {

    /**
     * 缩略图
     */
    private String thumbUrl;

    /**
     * 缩略图宽度
     */
    @SerializedName("thumb_width")
    private int thumbWidth;

    /**
     * 缩略图高度
     */
    @SerializedName("thumb_height")
    private int thumbHeight;

    /**
     * 图片实际地址
     */
    @SerializedName("pic_url")
    private String picUrl;

    /**
     * 大图的宽度
     */
    private int width;

    /**
     * 大图的高度
     */
    private int height;

    /**
     * 图片所在页面地址
     */
    @SerializedName("page_url")
    private String pageUrl;

    /**
     * 图片标题
     */
    private String title;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
