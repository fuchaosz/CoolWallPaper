package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by fuchao on 2016/3/29.
 */
public class MyBmobFavourite extends BmobObject {

    //用户
    private MyBmobUser user;

    //收藏的图片
    private MyBmobPicture picture;

    public MyBmobFavourite() {
        this.setTableName(BmobConst.TB_FAVORITE);
    }

    public MyBmobUser getUser() {
        return user;
    }

    public void setUser(MyBmobUser user) {
        this.user = user;
    }

    public MyBmobPicture getPicture() {
        return picture;
    }

    public void setPicture(MyBmobPicture picture) {
        this.picture = picture;
    }
}
