package com.coolwallpaper.bmob;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * bmob的登录记录表
 * <p>
 * Created by fuchao on 2016/5/30.
 */
public class MyBmobLogin extends BmobObject {

    /**
     * 登录的用户
     */
    private MyBmobUser user;

    /**
     * 登录
     */
    private Date time;

    /**
     * 登录地点
     */
    private String address;

    /**
     * 登录的IP地址
     */
    private String ip;

    /**
     * 登录类型,
     * 0 表示本应用注册的用户（现阶段没有这个功能）
     * 1 表示qq登录
     * 2 表示新浪登录
     */
    private Integer type;

    public MyBmobLogin(){
        this.setTableName(BmobConst.TB_LOGIN);
    }

    public MyBmobUser getUser() {
        return user;
    }

    public void setUser(MyBmobUser user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
