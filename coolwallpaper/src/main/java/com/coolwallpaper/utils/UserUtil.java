package com.coolwallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.bean.BaseUserInfo;
import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bean.IUserOperator;

/**
 * 用户本地管理工具,用于处理用户登录成功后保存在本地记录
 * 本类采用SharedPreference保存用户信息
 * Created by fuchao on 2016/6/7.
 */
public class UserUtil implements IUserOperator {

    private static final String PREFERENCE_NAME = "user";//SharedPreferenced文件名称
    private static UserUtil instance;//单例模式
    private SharedPreferences sp;

    //获取单例模式
    public synchronized static UserUtil getInstance() {
        if (instance == null) {
            instance = new UserUtil();
        }
        return instance;
    }

    //私有构造函数
    private UserUtil() {
        //创建SharedPreference
        this.sp = MyApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    //添加用户
    @Override
    public boolean addUser(IUserInfo user) {
        //保存到文件中
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", user.getAccount());
        editor.putString("name", user.getName());
        editor.putString("img", user.getImg());
        editor.putInt("sex", user.getSex());
        editor.putInt("userType", user.getUserType());
        return editor.commit();
    }

    //获取默认的用户，一般只会保存一个用户
    @Override
    public IUserInfo getUser() {
        BaseUserInfo info = null;
        //没有数据，返回null
        if (sp.getString("account", null) == null) {
            info = null;
        }
        //有数据
        else {
            info = new BaseUserInfo();
            info.setAccount(sp.getString("account", null));
            info.setName(sp.getString("name", null));
            info.setImg(sp.getString("img", null));
            info.setSex(sp.getInt("sex", 0));
            info.setUserType(sp.getInt("userType", 0));
        }
        return info;
    }

    //删除用户
    @Override
    public boolean delUser(String account) {
        //没有这个用户
        if (sp.getString("account", null) == null) {
            return false;
        }
        //有这个用户则删除
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }

    @Override
    public boolean updateUser(IUserInfo user) {
        return addUser(user);
    }

}
