package com.coolwallpaper.bean;

/**
 * 基本的实现IUserInfo的类
 * Created by fuchao on 2016/6/7.
 */
public class BaseUserInfo implements IUserInfo {

    private String account;
    private String name;
    private String img;
    private int sex;
    private int userType;

    public BaseUserInfo() {

    }

    public BaseUserInfo(String account, String name, String img, int sex, int userType) {
        this.account = account;
        this.name = name;
        this.img = img;
        this.sex = sex;
        this.userType = userType;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public int getSex() {
        return sex;
    }

    @Override
    public int getUserType() {
        return userType;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
