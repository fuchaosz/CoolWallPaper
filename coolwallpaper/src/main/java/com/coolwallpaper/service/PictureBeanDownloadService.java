package com.coolwallpaper.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.constant.AppBus;
import com.coolwallpaper.event.DownloadPictureResultFailureEvent;
import com.coolwallpaper.event.NetworkConnectionTimeOutEvent;
import com.coolwallpaper.utils.PictureParseUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * PictureBean下载服务.注意：是下载的图片URL地址，而不是图片文件
 * 返回结果采用的是otto的方式
 * Created by fuchao on 2015/11/24.
 */
public class PictureBeanDownloadService extends BaseService {

    private BaseRequestParam param;//图片URL的请求参数
    private OkHttpClient okHttpClient;//访问网络采用okhttp
    private List<PictureResult> beanList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 启动服务的方法
     *
     * @param context
     * @param param
     */
    public static void startService(Context context, BaseRequestParam param) {
        Intent intent = new Intent(context, PictureBeanDownloadService.class);
        intent.putExtra("PARAM", param);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.okHttpClient = new OkHttpClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取参数
        this.param = (BaseRequestParam) intent.getSerializableExtra("PARAM");
        //开始下载
        this.startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    //开始下载
    private void startDownload() {
        final Request request = new Request.Builder().url(param.getUrl()).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                downloadFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                if (response.isSuccessful()) {
                    //解析数据
                    beanList = PictureParseUtil.parse(response.body().toString());
                    downloadSuccess();
                }
                //请求超时
                else if (response.code() == 408) {
                    downloadOutTime();
                }
                //请求失败
                else {
                    downloadFailure(response.body().toString());
                }
            }
        });
    }

    //下载成功
    private void downloadSuccess() {
        //发送下载成功消息
        //AppBus.getInstance().post(new DownloadPictureResultSuccessEvent(beanList));
    }

    //下载失败
    private void downloadFailure(String reason) {
        //发送下载失败的消息
        AppBus.getInstance().post(new DownloadPictureResultFailureEvent(reason));
    }

    //访问网络超时
    private void downloadOutTime() {
        //发送网络超时消息
        AppBus.getInstance().post(new NetworkConnectionTimeOutEvent());
    }
}
