package com.coolwallpaper.bean;

import java.io.Serializable;

/**
 * 第三方登录后用户信息接口
 * Created by fuchao on 2016/5/27.
 */
public interface IUserInfo extends Serializable{

    /**
     * 获取用户登录账号
     *
     * @return
     */
    public String getAccount();

    /**
     * 用户姓名或昵称
     *
     * @return
     */
    public String getName();

    /**
     * 头像图片
     *
     * @return
     */
    public String getImg();

    /**
     * 性别
     *
     * @return 0 男 1女
     */
    public int getSex();

    /**
     * 用户登录的类别
     *
     * @return 详细见Constant.USER_TYPE常量
     */
    public int getUserType();
}
