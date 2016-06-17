package com.coolwallpaper.utils;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.bean.SearchResult;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobPicture;
import com.coolwallpaper.model.LocalFavourite;
import com.coolwallpaper.model.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换工具
 * Created by fuchao on 2015/12/17.
 */
public class ConvertUtil {

    /**
     * PictureResult转换为数据库实体Picture
     *
     * @param result
     * @return
     */
    public static Picture toPicture(PictureResult result) {
        if (result == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setThumbUrl(result.getThumbUrl());
        picture.setDownloadUrl(result.getDownloadUrl());
        picture.setFromUrl(result.getFromUrl());
        picture.setWidth(result.getWidth());
        picture.setHeight(result.getHeight());
        picture.setDesc(result.getDesc());
        return picture;
    }

    /**
     * 将搜索的结果转换为数据库实体Picture
     *
     * @param searchResult 搜狗壁纸搜索的结果
     * @return 数据库的实体对象
     */
    public static Picture toPicture(SearchResult searchResult) {
        if (searchResult == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setThumbUrl(searchResult.getThumbUrl());
        picture.setDownloadUrl(searchResult.getPicUrl());
        picture.setFromUrl(searchResult.getPageUrl());
        picture.setWidth(searchResult.getWidth());
        picture.setHeight(searchResult.getHeight());
        picture.setDesc(searchResult.getTitle());
        return picture;
    }

    /**
     * 批量转换搜索结果为数据库实体对象
     *
     * @param searchResultList 搜狗壁纸的搜索结果
     * @return 数据库实体Picture对象
     */
    public static List<Picture> toPictureList(List<SearchResult> searchResultList) {
        if (searchResultList == null) {
            return null;
        }
        List<Picture> pictureList = new ArrayList<>();
        for (SearchResult r : searchResultList) {
            pictureList.add(toPicture(r));
        }
        return pictureList;
    }

    /**
     * 将Picture转换为MyBmobPicture.MyBmobPicture是Bmob上的表
     *
     * @param picture 本地数据库保存的壁纸表
     * @return Bmob上保存的壁纸表对象
     */
    public static MyBmobPicture toMyBmobPicture(Picture picture) {
        MyBmobPicture myBmobPicture = new MyBmobPicture();
        myBmobPicture.setUrl(picture.getDownloadUrl());
        myBmobPicture.setThumbUrl(picture.getThumbUrl());
        myBmobPicture.setWidth(picture.getWidth());
        myBmobPicture.setHeight(picture.getHeight());
        myBmobPicture.setDescribe(picture.getDesc());
        myBmobPicture.setDownloadCount(0);
        myBmobPicture.setWallPaperCount(0);
        myBmobPicture.setFavouriteCount(0);
        return myBmobPicture;
    }

    /**
     * 将Bmob上的用户收藏，转换为本地数据库收藏对象
     *
     * @param myBmobFavourite bmob用户收藏对象
     * @return 本地数据库用户收藏对象
     */
    public static LocalFavourite toLocalFavourite(MyBmobFavourite myBmobFavourite) {
        LocalFavourite localFavourite = new LocalFavourite();
        localFavourite.setAccount(myBmobFavourite.getAccount());
        localFavourite.setUrl(myBmobFavourite.getUrl());
        localFavourite.setName(myBmobFavourite.getName());
        return localFavourite;
    }

    /**
     * 将本地图片picture对象转换为bmob上收藏图片对象
     *
     * @param picture
     * @return
     */
    public static MyBmobFavourite toMyBmobFavourite(IUserInfo user, Picture picture) {
        MyBmobFavourite favourite = new MyBmobFavourite();
        favourite.setAccount(user.getAccount());
        favourite.setName(user.getName());
        favourite.setUrl(picture.getDownloadUrl());
        favourite.setThumbUrl(picture.getThumbUrl());
        favourite.setWidth(picture.getWidth());
        favourite.setHeight(picture.getHeight());
        favourite.setDescribe(picture.getDesc());
        return favourite;
    }
}
