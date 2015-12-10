package com.coolwallpaper.bean;

import java.io.Serializable;

/**
 * 图片Bean
 * Created by fuchao on 2015/9/30.
 */
public class PictureBean implements Serializable {


    /**
     * thumbURL : http://img2.imgtn.bdimg.com/it/u=1331604501,2780813711&fm=21&gp=0.jpg
     * middleURL : http://img2.imgtn.bdimg.com/it/u=1331604501,2780813711&fm=21&gp=0.jpg
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

    private String thumbURL;
    private String middleURL;
    private boolean hasLarge;
    private String hoverURL;
    private int pageNum;
    private String objURL;
    private String fromURL;
    private String fromURLHost;
    private int width;
    private int height;
    private String type;
    private String di;
    private String bdImgnewsDate;
    private String fromPageTitle;

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public void setMiddleURL(String middleURL) {
        this.middleURL = middleURL;
    }

    public void setHasLarge(boolean hasLarge) {
        this.hasLarge = hasLarge;
    }

    public void setHoverURL(String hoverURL) {
        this.hoverURL = hoverURL;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setObjURL(String objURL) {
        this.objURL = objURL;
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

    public void setDi(String di) {
        this.di = di;
    }

    public void setBdImgnewsDate(String bdImgnewsDate) {
        this.bdImgnewsDate = bdImgnewsDate;
    }

    public void setFromPageTitle(String fromPageTitle) {
        this.fromPageTitle = fromPageTitle;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public String getMiddleURL() {
        return middleURL;
    }

    public boolean isHasLarge() {
        return hasLarge;
    }

    public String getHoverURL() {
        return hoverURL;
    }

    public int getPageNum() {
        return pageNum;
    }

    public String getObjURL() {
        return objURL;
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

    public String getDi() {
        return di;
    }

    public String getBdImgnewsDate() {
        return bdImgnewsDate;
    }

    public String getFromPageTitle() {
        return fromPageTitle;
    }
}
