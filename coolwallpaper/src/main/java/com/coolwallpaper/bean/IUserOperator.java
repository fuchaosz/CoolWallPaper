package com.coolwallpaper.bean;

/**
 * 定义操作用户的类
 * Created by fuchao on 2016/6/7.
 */
public interface IUserOperator {

    /**
     * 保存用户
     *
     * @param user
     * @return 是否保存成功
     */
    public boolean addUser(IUserInfo user);

    /**
     * 获取用户
     *
     * @return 本地用户
     */
    public IUserInfo getUser();

    /**
     * 删除用户
     *
     * @param account 用户账号
     * @return
     */
    public boolean delUser(String account);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 更新是否成功
     */
    public boolean updateUser(IUserInfo user);
}
