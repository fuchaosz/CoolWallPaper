package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobUser;

/**
 * Bmob对应的用户表,bmob对应的类都会以MyBmob开头
 * Created by fuchao on 2016/3/29.
 */
public class MyBmobUser extends BmobUser {

    /**
     * 用户性别，true为女，false为男
     */
    private Boolean sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户头像
     */
    private String imgUrl;

    public MyBmobUser() {
        //表名和类名称不一致的时候要指定表名称
        this.setTableName(BmobConst.TB_USER);
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
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
}
