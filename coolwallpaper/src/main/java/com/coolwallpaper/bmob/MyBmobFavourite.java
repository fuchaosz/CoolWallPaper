package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by fuchao on 2016/3/29.
 */
public class MyBmobFavourite extends BmobObject {

    //用户账号
    private String account;

    //用户昵称
    private String name;

    //图片url
    private String url;

    //缩略图
    private String thumbUrl;

    //图片宽度
    private int width;

    //图片高度
    private int height;

    //图片描述
    private String describe;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
