package com.coolwallpaper.utils;

/**
 * 从百度图片获取图片地址
 * Created by John on 2015/8/12.
 */
public class PicUrlUtil {

    public static String WALLPAPER_BASE_URL = "http://image.baidu.com/data/imgs?pn=%d&rn=36&col=%E5%A3%81%E7%BA%B8&tag=%E5%85%A8%E9%83%A8&tag3=&width=1920&height=1080&ic=0&ie=utf8&oe=utf-8&image_id=&fr=channel&p=channel&from=1&app=img.browse.channel.wallpaper";
    private String url;

    //获取Url
    public PicUrlUtil(String url) {
        this.url = url;
    }

    //    //解析出数据
    //    public void getUrl(){
    //        //获取数据
    //        //httpClient.get(url, new AsyncHttpResponseHandler() {
    //            @Override
    //            public void onSuccess(int i, Header[] headers, byte[] bytes) {
    //                //解析数据
    //
    //
    //
    //            }
    //
    //            @Override
    //            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
    //
    //            }
    //        });
    //    }

}
