package com.coolwallpaper.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 图片Bean
 * Created by fuchao on 2015/9/30.
 */
public class PictureBean implements Serializable {

    /**
     * 图片ID，太长了所以用String
     */
    private String id;

    /**
     * 图片描述
     */
    private String desc;

    /**
     * 所有标签,是JSON数组，例如："tags": ["微软壁纸"]
     */
    private List<String> tags;

    /**
     * 日期,格式：2015-09-30
     */
    private String date;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * 和downloadUrl好像是一样的
     */
    private String imageUrl;

    /**
     * 图片宽度
     */
    private String imageWidth;

    /**
     * 图片高度
     */
    private String imageHeight;

    /**
     * 缩略图url。对应字段：thumbLargeUrl
     */
    @SerializedName(value = "thumbLargeUrl")
    private String smallImageUrl;

    /**
     * 缩略图宽度  thumbLargeWidth
     */
    @SerializedName(value = "thumbLargeWidth")
    private int smallImageWidth;

    /**
     * 缩略图长度  thumbLargeTnWidth
     */
    @SerializedName(value = "thumbLargeHeight")
    private int smallImageHeight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public int getSmallImageWidth() {
        return smallImageWidth;
    }

    public void setSmallImageWidth(int smallImageWidth) {
        this.smallImageWidth = smallImageWidth;
    }

    public int getSmallImageHeight() {
        return smallImageHeight;
    }

    public void setSmallImageHeight(int smallImageHeight) {
        this.smallImageHeight = smallImageHeight;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
