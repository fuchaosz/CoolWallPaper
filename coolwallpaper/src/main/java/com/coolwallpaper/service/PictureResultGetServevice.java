package com.coolwallpaper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.constant.AppBus;
import com.coolwallpaper.event.DownloadPictureResultFailureEvent;
import com.coolwallpaper.event.DownloadPictureResultSuccessEvent;
import com.coolwallpaper.model.Param;
import com.coolwallpaper.model.ParamDao;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.model.PictureDao;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.PictureParseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.dao.query.QueryBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 从网络获取PictureResult图片的服务
 * Created by fuchao on 2015/12/16.
 */
public class PictureResultGetServevice extends BaseService {

    private OkHttpClient okHttpClient;//采用okHttp来访问网络
    private ExecutorService executor;//线程池

    /**
     * 启动服务的方法
     *
     * @param context
     * @param param   要访问网络的参数
     */
    public static void startService(Context context, BaseRequestParam param) {
        Intent intent = new Intent(context, PictureResultGetServevice.class);
        intent.putExtra("BaseRequestParam", param);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.okHttpClient = new OkHttpClient();
        //创建一个可缓存的线程池
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //取出参数
        BaseRequestParam requestParam = (BaseRequestParam) intent.getSerializableExtra("BaseRequestParam");
        Log.d(TAG, "onStartCommand() title1=" + requestParam.getTitle1() + " title2=" + requestParam.getTitle2());
        //判断一下空指针
        if (requestParam != null) {
            //放进线程池里面访问网络，获取数据
            executor.execute(new GetPictureRunable(okHttpClient, requestParam));
        }
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    //下载图片列表信息的线程体
    public class GetPictureRunable implements Runnable {

        private BaseRequestParam requestParam;
        private OkHttpClient client;
        private List<PictureResult> list;
        private List<Picture> pictureList = new ArrayList<>();

        GetPictureRunable(OkHttpClient client, BaseRequestParam requestParam) {
            this.client = client;
            this.requestParam = requestParam;
        }

        @Override
        public void run() {
            final Request request = new Request.Builder().url(requestParam.getUrl()).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //发送失败消息
                    AppBus.getInstance().post(new DownloadPictureResultFailureEvent(e.toString()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();
                    //解析数据
                    list = PictureParseUtil.parse(jsonStr);
                    //保存数据
                    save();
                    Log.d(TAG, String.format("send Message DownloadPictureResultSuccessEvent() title1=%s title2=%s", requestParam.getTitle1(), requestParam.getTitle2()));
                    //发送成功消息
                    AppBus.getInstance().post(new DownloadPictureResultSuccessEvent(requestParam));
                }
            });
        }

        //保存到数据库中
        private void save() {
            //判断数据是否为空
            if (list == null) {
                return;
            }
            Param param = null;
            //保存Param之前查询有无重复数据
            ParamDao paramDao = DBUtil.getInstance().getParamDao();
            QueryBuilder<Param> qb = paramDao.queryBuilder();
            qb.where(ParamDao.Properties.Title1.eq(requestParam.getTitle1()), ParamDao.Properties.Title2.eq(requestParam.getTitle2()));
            List<Param> paramList = qb.list();
            //如果没有重复的数据
            if (paramList == null || paramList.size() == 0) {
                //创建新的param
                param = new Param();
                param.setTitle1(requestParam.getTitle1());
                param.setTitle2(requestParam.getTitle2());
                //保存到数据库
                paramDao.insert(param);
                //刷新一下，获取id
                param.refresh();
            }
            //之前已经保存了
            else {
                param = paramList.get(0);
            }
            PictureDao pictureDao = DBUtil.getInstance().getPictureDao();
            //转换数据
            for (int i = 0; i < list.size(); i++) {
                Picture tmp = ConvertUtil.toPicture(list.get(i));
                tmp.setParam(param);
                pictureList.add(tmp);
                //插入之前先判断一下有没有这个数据
                QueryBuilder qb2 = pictureDao.queryBuilder();
                qb2.where(PictureDao.Properties.DownloadUrl.eq(tmp.getDownloadUrl()));
                //如果没有查到数据
                if (qb2.count() == 0) {
                    //插入数据
                    pictureDao.insert(tmp);
                }
            }
        }
    }
}
