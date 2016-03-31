package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobQuery;

/**
 * 操作Bmob的工具
 * Created by fuchao on 2016/3/29.
 */
public class BmobUtil {

    /**
     * 获取用户表查询对象
     *
     * @return BmobQuery对象
     */
    public static BmobQuery<MyBmobUser> getMyUserQuery() {
        return new BmobQuery<MyBmobUser>(BmobConst.TB_USER);
    }

    /**
     * 获取下载表查询对象
     *
     * @return
     */
    public static BmobQuery<MyBmobPicture> getMyPictureQuery() {
        return new BmobQuery<MyBmobPicture>(BmobConst.TB_PICTURE);
    }

}
