package com.coolwallpaper.utils;

import android.app.Activity;

import com.coolwallpaper.MyApplication;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * UMeng操作的工具
 * Created by fuchao on 2016/4/28.
 */
public class UmengUtil {

    private static UmengUtil util;
    private UMShareAPI mShareAPI;
    private MyUMAuthListener myUMAuthListener;

    //私有构造函数
    private UmengUtil() {
        this.mShareAPI = UMShareAPI.get(MyApplication.getInstance());
        this.myUMAuthListener = new MyUMAuthListener();
        this.initPlatform();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static UmengUtil getInstence() {
        if (util == null) {
            util = new UmengUtil();
        }
        return util;
    }

    //初始化平台
    private void initPlatform() {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        // QQ和Qzone appid appkey
        PlatformConfig.setAlipay("2015111700822536");
        //支付宝 appid
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        //易信 appkey
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        //Twitter appid appkey
        PlatformConfig.setPinterest("1439206");
        //Pinterest appid
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        //来往 appid appkey
    }

    /**
     * QQ登录
     *
     * @param activity
     * @param callback
     */
    public void qqLogin(Activity activity, Callback callback) {
        login(activity, SHARE_MEDIA.RENREN, callback);
    }

    /**
     * 第三方登录
     *
     * @param shareMedia 常量，用于表示哪个第三方,例如：qq, 新浪微博
     */
    private void login(Activity activity, SHARE_MEDIA shareMedia, Callback callback) {
        mShareAPI.doOauthVerify(activity, shareMedia, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (callback != null) {
                    callback.onFailure("onError");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (callback != null) {
                    callback.onFailure("cancel");
                }
            }
        });
    }

    /**
     * umeng的授权回调接口
     */
    private class MyUMAuthListener implements UMAuthListener {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    }

    //接口回调
    public static interface Callback {

        /**
         * 登录成功
         */
        public void onSuccess();

        /**
         * 登录失败
         */
        public void onFailure(String reason);
    }
}
