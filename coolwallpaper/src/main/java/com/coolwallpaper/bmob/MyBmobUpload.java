package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 用户上传图片
 * Created by fuchao on 2016/6/28.
 */
public class MyBmobUpload extends BmobObject {

    /**
     * 账号
     */
    private String account;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userImg;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户上传的图片在bmob上的地址
     */
    private String fileUrl;

    /**
     * 文件名,注意文件名和url的结尾是不一样的,不能根据url得出文件名
     */
    private String fileName;

    /**
     * 图片的宽
     */
    private Integer width;

    /**
     * 图片的高
     */
    private Integer height;

    /**
     * 图片描述
     */
    private String desc;

    public MyBmobUpload() {

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
