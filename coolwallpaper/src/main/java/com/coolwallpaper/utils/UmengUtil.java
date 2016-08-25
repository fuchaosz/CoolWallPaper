package com.coolwallpaper.utils;

import android.app.Activity;
import android.content.Intent;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bean.QQUserInfo;
import com.coolwallpaper.bean.SinaUserInfo;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.Map;

/**
 * UMeng操作的工具
 * Created by fuchao on 2016/4/28.
 */
public class UmengUtil {

    private static UmengUtil util;
    private UMShareAPI mShareAPI;

    //私有构造函数
    private UmengUtil() {
        this.mShareAPI = UMShareAPI.get(MyApplication.getInstance());
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
     * 要获得授权后的数据，必须要在Activity的onActivityResult中调用该方法
     *
     * @param requestCode 和onActivityResult中调用该方法的参数一样
     * @param resultCode
     * @param data
     */
    public void setActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * QQ登录
     *
     * @param activity
     * @param callback
     */
    public void qqLogin(Activity activity, Callback callback) {
        login(activity, SHARE_MEDIA.QQ, callback);
    }

    /**
     * 新浪微博登录
     *
     * @param activity
     * @param callBack
     */
    public void sinaLogin(Activity activity, Callback callBack) {
        login(activity, SHARE_MEDIA.SINA, callBack);
    }

    /**
     * Get user info who login with qq
     *
     * @param activity
     * @param callBack callback method to get user info
     */
    public void getQQUserInfo(Activity activity, InfoCallBack callBack) {
        getUserInfo(activity, SHARE_MEDIA.QQ, callBack);
    }

    /**
     * 获取新浪微博登录的用户的信息
     *
     * @param activity
     * @param callBack
     */
    public void getSinaUserInfo(Activity activity, InfoCallBack callBack) {
        getUserInfo(activity, SHARE_MEDIA.SINA,callBack);
    }

    /**
     * 第三方登录
     *
     * @param shareMedia 常量，用于表示哪个第三方,例如：qq, 新浪微博
     * @param callback   登录是否成功的回调接口
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
     * 获取用户信息
     *
     * @param activity
     * @param platform 平台表示，SHARE_MEDIA类型
     * @param callBack 用户信息回调接口
     */
    private void getUserInfo(Activity activity, SHARE_MEDIA platform, InfoCallBack callBack) {
        mShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                IUserInfo userInfo = null;
                //PARES THE INFO FROM QQ PLATFORM
                switch (share_media) {
                    case QQ:
                        userInfo = new QQUserInfo(map);
                        break;
                    //新浪微博
                    case SINA:
                        userInfo = new SinaUserInfo();
                        break;
                }
                //call back to method caller
                if (callBack != null) {
                    callBack.getUserInfo(userInfo);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (callBack != null) {
                    callBack.getUserInfo(null);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (callBack != null) {
                    callBack.getUserInfo(null);
                }
            }
        });
    }

    //分享
    public void share(Activity activity, String title, String content, String url, Callback callback) {
        UMImage image = new UMImage(activity, url);
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN};
        new ShareAction(activity).setDisplayList(displaylist).withText(content).withTitle(title).withTargetUrl(url).withMedia(image).setListenerList(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable.toString());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (callback != null) {
                    callback.onFailure("share canceled");
                }
            }
        }).open();
    }

    //登录接口回调
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

    //用户信息获取接口
    public static interface InfoCallBack {

        /**
         * 用户信息回调接口
         *
         * @param userInfo 用户信息，获取失败则返回null
         */
        public void getUserInfo(IUserInfo userInfo);
    }
}
