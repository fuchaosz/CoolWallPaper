package com.coolwallpaper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.coolwallpaper.bmob.MyBmobUpdate;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 版本更新服务
 * Created by fuchao on 2016/7/4.
 */
public class UpdateService extends Service {

    private static final String TAG = "[UpdateService]";
    //下载失败的action
    public static final String ACTION_DOWNLOAD_FAILURE = "UpdateService.action.download_failure";
    //下载成功的action
    public static final String ACTION_DOWNLOAD_SUCCESS = "UpdateService.action.download_success";
    //下载进度的action
    public static final String ACTION_DOWNLOAD_PROGRESS = "UpdateService.action.download_progress";

    private MyBmobUpdate update;//bmob升级表对象

    /**
     * 启动方法
     *
     * @param context
     * @param update  Bmob的升级表对象
     */
    public static void startService(Context context, MyBmobUpdate update) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("UPDATE", update);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //取出参数
        this.update = (MyBmobUpdate) intent.getSerializableExtra("UPDATE");
        if (update != null) {
            //启动下载线程
            new DownloadThread(update.getDownloadUrl()).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //下载线程
    private class DownloadThread extends Thread {

        private String url;

        public DownloadThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            //判断url是否符合规范
            if (url == null || !url.startsWith("http")) {
                downloadFaiure("url为空或格式不对");
                return;
            }
            //确保目录是存在的
            FileUtil.getInstance();
            //如果目标文件存在则删除
            File tmpFile = new File(FileUtil.DIRECTORY_DOWNLOAD, FileUtil.getFileName(url));
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            //开始下载
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.d(TAG, "下载失败");
                    downloadFaiure("下载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    //进度
                    int progress = 0;
                    long total = response.body().contentLength();
                    long sum = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        File file = new File(FileUtil.DIRECTORY_DOWNLOAD, FileUtil.getFileName(url));
                        fos = new FileOutputStream(file);
                        //读入文件
                        while ((len = is.read(buf)) != -1) {
                            //写入文件
                            fos.write(buf, 0, len);
                            sum += len;
                            //计算进度
                            progress = (int) ((sum * 1.0f / total) * 100);
                            //更新进度
                            updateProgress(progress, total, sum);
                        }
                        //下载成功
                        downloadSuccess();
                    } catch (Exception e) {
                        //下载失败
                        downloadFaiure("写入文件异常");
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                            if (is != null) {
                                is.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    //下载失败
    private void downloadFaiure(String reason) {
        //发送下载失败的广播
        Intent intent = new Intent(ACTION_DOWNLOAD_FAILURE);
        intent.putExtra("reason", reason);
        sendBroadcast(intent);
    }

    //更新进度
    private void updateProgress(int progress, long totalSize, long downloadSize) {
        //发送下载进度广播
        Intent intent = new Intent(ACTION_DOWNLOAD_PROGRESS);
        intent.putExtra("progress", progress);
        intent.putExtra("downloadSize", downloadSize);
        intent.putExtra("totalSize", totalSize);
        sendBroadcast(intent);
    }

    //下载成功的广播
    private void downloadSuccess() {
        //发送下载成功的广播
        Intent intent = new Intent(ACTION_DOWNLOAD_SUCCESS);
        sendBroadcast(intent);
    }
}
