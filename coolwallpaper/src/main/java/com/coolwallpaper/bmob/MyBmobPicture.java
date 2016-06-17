package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Bmob图片表
 * Created by fuchao on 2016/3/29.
 */
public class MyBmobPicture extends BmobObject {

    private String url;//图片url

    private String thumbUrl;//缩略图url

    private Integer width;//宽度

    private Integer height;//高度

    private String describe;//描述

    private Integer downloadCount;//下载数量

    private Integer wallPaperCount;//设为壁纸的次数

    private Integer favouriteCount;//收藏的次数

    public MyBmobPicture() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getWallPaperCount() {
        return wallPaperCount;
    }

    public void setWallPaperCount(Integer wallPaperCount) {
        this.wallPaperCount = wallPaperCount;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer favouriteCount) {
        this.favouriteCount = favouriteCount;
    }
}
