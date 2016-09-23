package com.coolwallpaper.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 访问网络之后返回的图片的结果，采用Gson解析
 * Created by fuchao on 2015/12/16.
 */
public class PictureResult implements Serializable {
    /**
     * 缩略图
     */
    @SerializedName("thumbUrl")
    private String thumbUrl;

    /**
     * 原图地址
     */
    @SerializedName("pic_url")
    private String downloadUrl;

    /**
     * 图片来源
     */
    @SerializedName("page_url")
    private String fromUrl;

    /**
     * 原图的宽度
     */
    private int width;

    /**
     * 原图的高度
     */
    private int height;

    /**
     * 图片描述，这里是图片来源的页面的标题
     */
    @SerializedName("tags")
    private String desc;

    public PictureResult() {
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
