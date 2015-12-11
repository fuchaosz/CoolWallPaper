package com.coolwallpaper.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 图片Bean
 * Created by fuchao on 2015/9/30.
 */
public class PictureBean implements Serializable {

    /**
     * thumbURL : http://img2.imgtn.bdimg.com/it/u=1331604501,2780813711&fm=21&gp=0.jpg
     * hasLarge : true
     * hoverURL : http://img2.imgtn.bdimg.com/it/u=1331604501,2780813711&fm=23&gp=0.jpg
     * pageNum : 60
     * objURL : http://www.bz55.com/uploads/allimg/150408/139-15040Q15052.jpg
     * fromURL : http://www.bz55.com/qichebizhi/19112.html
     * fromURLHost : www.bz55.com
     * width : 1680
     * height : 1050
     * type : jpg
     * di : 97263019700
     * bdImgnewsDate : 1970-01-01 08:00
     * fromPageTitle : 经典<strong>兰博基尼</strong>埃文塔多跑车图片win7 壁纸
     */

    /**
     * 缩略图
     */
    @SerializedName("hoverURL")
    private String thumbURL;

    /**
     * 原图地址
     */
    @SerializedName("objURL")
    private String downloadUrl;

    /**
     * 图片来源
     */
    private String fromURL;

    /**
     * 图片来源网址的host
     */
    private String fromURLHost;

    /**
     * 原图的宽度
     */
    private int width;

    /**
     * 原图的高度
     */
    private int height;

    /**
     * 原图的类型，例如:type
     */
    private String type;

    /**
     * 图片描述，这里是图片来源的页面的标题
     */
    @SerializedName("fromPageTitle")
    private String desc;

    public PictureBean() {
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public void setFromURL(String fromURL) {
        this.fromURL = fromURL;
    }

    public void setFromURLHost(String fromURLHost) {
        this.fromURLHost = fromURLHost;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public String getFromURL() {
        return fromURL;
    }

    public String getFromURLHost() {
        return fromURLHost;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
