package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobUser;

/**
 * Bmob对应的用户表,bmob对应的类都会以MyBmob开头
 * Created by fuchao on 2016/3/29.
 */
public class MyBmobUser extends BmobUser {

    /**
     * 账号，用户唯一标识
     */
    private String account;

    /**
     * 用户性别，0为男，1位女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户头像
     */
    private String imgUrl;

    public MyBmobUser() {
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
