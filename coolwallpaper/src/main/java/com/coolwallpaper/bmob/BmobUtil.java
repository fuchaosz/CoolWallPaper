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

    /**
     * 获取收藏表查询对象
     *
     * @return BmobQuery对象
     */
    public static BmobQuery<MyBmobFavourite> getMyFavouriteQuery() {
        return new BmobQuery<MyBmobFavourite>(BmobConst.TB_FAVORITE);
    }

    /**
     * 获取登录表查询对象
     *
     * @return BmobQuery对象
     */
    public static BmobQuery<MyBmobLogin> getMyLoginQuery() {
        return new BmobQuery<MyBmobLogin>(BmobConst.TB_LOGIN);
    }

    /**
     * 获取Bmob上传表查询对象
     *
     * @return BmobQuery对象
     */
    public static BmobQuery<MyBmobUpload> getMyUploadQuery() {
        return new BmobQuery<MyBmobUpload>(BmobConst.TB_UPLOAD);
    }

    /**
     * 获取Bmob版本更新表查询对象
     *
     * @return BmobQuery对象
     */
    public static BmobQuery<MyBmobUpdate> getMyUpdateQuery() {
        return new BmobQuery<MyBmobUpdate>(BmobConst.TB_UPDATE);
    }
}
