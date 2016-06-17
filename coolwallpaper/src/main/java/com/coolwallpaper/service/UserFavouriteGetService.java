package com.coolwallpaper.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.model.LocalFavourite;
import com.coolwallpaper.model.LocalFavouriteDao;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.LogUtil;
import com.coolwallpaper.utils.UserUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 获取bmob上当前登录的用户收藏表
 * Created by fuchao on 2016/6/12.
 */
public class UserFavouriteGetService extends BaseService {

    private static String TAG = "[UserFavouriteGetService]";
    public static String ACTION = "UserFavouriteGetService.action";//广播字符串
    private IUserInfo mUser;//本地保存的用户

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
        Intent intent = new Intent(context, UserFavouriteGetService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "UserFavouriteGetService服务启动");
        //首先获取本地用户
        mUser = UserUtil.getInstance().getUser();
        //本地没有用户，说明用户没有登录过
        if (mUser == null) {
            LogUtil.d(TAG, "没有本地用户");
            return super.onStartCommand(intent, flags, startId);
        }
        //本地用户存在，则查询bmob上用户的收藏
        queryBmobFavourite();
        return super.onStartCommand(intent, flags, startId);
    }

    //继续获取bmob上用户的收藏
    private void queryBmobFavourite() {
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("account", mUser.getAccount());
        query.findObjects(this, new FindListener<MyBmobFavourite>() {
            @Override
            public void onSuccess(List<MyBmobFavourite> list) {
                //判断是否为空
                if (list == null || list.size() == 0) {
                    LogUtil.d(TAG, "查询到的用户收藏的图片为0");
                    //删除所有本地收藏数据
                    removeAllLocalFavourite();
                    //发送保存成功的消息
                    sendSuccessBroadCast();
                    return;
                }
                LogUtil.d(TAG, "Bmob上查询到的用户的收藏为,size = " + list.size());
                //保存到本地数据库
                saveMyFavouriteList(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //保存查询到数据到本地数据库
    private void saveMyFavouriteList(List<MyBmobFavourite> list) {
        //首先删除所有本地收藏表数据
        removeAllLocalFavourite();
        LogUtil.d(TAG, "开始插入bmob上获取的数据,size = " + list.size());
        LocalFavouriteDao dao = DBUtil.getInstance().getLocalFavouriteDao();
        //遍历从bmob取回的数据
        for (int i = 0; i < list.size(); i++) {
            MyBmobFavourite bmobFavourite = list.get(i);
            //bmob对象转换为本地数据库对象
            LocalFavourite localFavourite = ConvertUtil.toLocalFavourite(bmobFavourite);
            //保存
            dao.insert(localFavourite);
        }
        LogUtil.d(TAG, "插入bmob上获取的数据到本地数据库成，下面发送消息");
        //发送保存成功的消息
        sendSuccessBroadCast();
    }

    //删除所有本地数据库收藏的壁纸
    private void removeAllLocalFavourite() {
        LocalFavouriteDao dao = DBUtil.getInstance().getLocalFavouriteDao();
        QueryBuilder<LocalFavourite> qb = dao.queryBuilder();
        //删除所有这个用户的本地收藏
        qb.where(LocalFavouriteDao.Properties.Account.eq(mUser.getAccount()));
        List<LocalFavourite> localFavourites = qb.list();
        //数据库中存在这个用户的本地收藏
        if (localFavourites != null && localFavourites.size() > 0) {
            LogUtil.d(TAG, "全部删除本地用户收藏，size = " + localFavourites.size());
            //全部删除
            dao.deleteInTx(localFavourites);
        }
    }

    //获取用户收藏成功，发送广播
    private void sendSuccessBroadCast() {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("result", "success");
        sendBroadcast(intent);
    }

    //获取用户收藏失败，发送广播
    private void sendFailureBroadCast() {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("result", "failure");
        sendBroadcast(intent);
    }
}
