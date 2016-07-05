package com.coolwallpaper;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobUpdate;
import com.coolwallpaper.constant.Constant;
import com.coolwallpaper.model.LocalFavourite;
import com.coolwallpaper.model.LocalFavouriteDao;
import com.coolwallpaper.service.UserFavouriteGetService;
import com.coolwallpaper.utils.CommonUtil;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.LogUtil;
import com.coolwallpaper.utils.UserUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by fuchao on 2015/10/30.
 */
public class MyApplication extends Application {

    public static final String TAG = "CoolWallPaper";
    private static MyApplication myApplication;
    private Set<String> localFavouriteSet;//用户保存在本地壁纸的集合
    private MyReveiver myReveiver = new MyReveiver();//广播接收者

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //设置一下日志的TAG
        Logger.init(TAG);
        //初始化Bmob
        Bmob.initialize(this, Constant.BMOB_APPID);
        //注册广播
        registerMyReceiver();
        //开启线程，做一些初始化工作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取用户本地收藏
                getLocalFavoureiteList();
                //启动服务更新bmob上的收藏
                UserFavouriteGetService.startService(myApplication);
            }
        }).start();
    }

    //注册广播
    private void registerMyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //注册获取bmob上用户收藏服务的广播
        intentFilter.addAction(UserFavouriteGetService.ACTION);
        registerReceiver(myReveiver, intentFilter);
    }

    /**
     * 获取全局的Application
     *
     * @return
     */
    public static MyApplication getInstance() {
        return myApplication;
    }

    //读取本地所有的用户收藏
    public void getLocalFavoureiteList() {
        IUserInfo user = UserUtil.getInstance().getUser();
        //没有用户登录，就不用取出本地收藏了
        if (user == null) {
            return;
        }
        LocalFavouriteDao favouriteDao = DBUtil.getInstance().getLocalFavouriteDao();
        QueryBuilder<LocalFavourite> qb = favouriteDao.queryBuilder();
        qb.where(LocalFavouriteDao.Properties.Account.eq(user.getAccount()));
        List<LocalFavourite> localFavouriteList = new ArrayList<>(qb.list());
        //转换为集合
        if (localFavouriteSet == null) {
            localFavouriteSet = new HashSet<>();
        }
        localFavouriteSet.clear();
        if (localFavouriteList != null) {
            for (LocalFavourite f : localFavouriteList) {
                localFavouriteSet.add(f.getUrl());
            }
        }
        LogUtil.d("在MyApplication中getLocalFavoureiteList()完成，localFavouriteSet.size=" + localFavouriteSet.size());
    }

    //广播接收
    private class MyReveiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //来自获取bmob用户收藏服务的广播
            if (action.equals(UserFavouriteGetService.ACTION)) {
                String result = intent.getStringExtra("result");
                LogUtil.d("收到广播,result = " + result);
                //获取bmob用户收藏成功
                if (result.equals("success")) {
                    //重新获取本地用户收藏
                    getLocalFavoureiteList();
                }
            }
            //来自版本升级的广播
        }
    }

    //单个收藏后刷新，考虑到效率问题，所以不能每次收藏之后都去读数据库
    public void addFavouroiteUrl(String url) {
        if (localFavouriteSet == null) {
            localFavouriteSet = new HashSet<>();
        }
        localFavouriteSet.add(url);
    }

    //单个取消收藏后，不从数据库中获取，而是简单的刷新一下数据
    public void removeFavouriteUrl(String url) {
        //从集合中删除
        if (localFavouriteSet != null) {
            localFavouriteSet.remove(url);
        }
    }

    public Set<String> getLocalFavouriteSet() {
        return localFavouriteSet;
    }

    //检查升级
    public void checkUpdate(Activity activity) {
        BmobQuery<MyBmobUpdate> query = BmobUtil.getMyUpdateQuery();
        query.findObjects(this, new FindListener<MyBmobUpdate>() {
            @Override
            public void onSuccess(List<MyBmobUpdate> list) {
                //如果没有数据
                if (EmptyUtil.isEmpty(list)) {
                    LogUtil.e("Bmob上没有数据");
                    return;
                }
                //取出最后一条数据，也就是最新的版本
                MyBmobUpdate update = list.get(list.size() - 1);
                //获取当前的版本号
                int versionCode = CommonUtil.getVersionCode();
                //获取版本失败了
                if (versionCode == 0) {
                    LogUtil.e("获取版本号失败");
                    return;
                }
                //如果当前版本是最新版本
                if (versionCode == update.getVersionCode()) {
                    LogUtil.i("当前版本是最新版本，不需要更新");
                    return;
                }
                //发现新版本，弹出对话框
                UpdateDialog dialog = new UpdateDialog(activity, update);
                dialog.show();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("获取Bmob上版本信息失败,reason = " + s);
            }
        });
    }
}
