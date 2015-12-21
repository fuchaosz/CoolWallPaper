package com.coolwallpaper.utils;

import android.os.Environment;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.constant.AppBus;
import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.model.Picture;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 文件操作类
 * Created by fuchao on 2015/10/30.
 */
public class FileUtil {

    private static FileUtil fileUtil;
    private String DIRECTORY_DOWNLOAD = "/mnt/sdcard/coolwallpaper/download";//下载文件的保存目录
    private String DIRECTORY_FAVORITE = "/mnt/sdcard/coolwallpaper/favorite";//收藏文件的保存目录
    private HttpUtils httpUtils;

    //获取单例
    public static FileUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        return fileUtil;
    }

    //构造函数
    private FileUtil() {
        //初始化
        this.init();
    }

    //初始化
    private void init() {
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //sdcard不存在或者不可读写
        if (!sdcardExist) {
            //把保存文件的目录放到内部存储
            DIRECTORY_DOWNLOAD = MyApplication.getInstance().getFilesDir().getAbsolutePath() + "/download";
            DIRECTORY_FAVORITE = MyApplication.getInstance().getFilesDir().getAbsolutePath() + "/favorite";
        }
        //创建目录
        File dirDownload = new File(DIRECTORY_DOWNLOAD);
        File dirFavorite = new File(DIRECTORY_FAVORITE);
        if (!dirDownload.exists() || !dirDownload.isDirectory()) {
            dirDownload.mkdirs();
        }
        if (!dirFavorite.exists() || !dirFavorite.isDirectory()) {
            dirFavorite.mkdirs();
        }
        //创建文件下载工具
        this.httpUtils = new HttpUtils();
    }

    /**
     * 获取文件名
     * 例如：http://hao.123.com/a.jpg 返回 a.jpg
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * 下载图片
     * 下载成功之后会发送DownloadPictureSuccessEvent事件
     * 下载失败之后会发送DownloadPictureFailureEvent事件
     *
     * @param pictureBean
     */
    public void downloadPictureFile(final Picture pictureBean) {
        //获取文件下载url
        String url = pictureBean.getDownloadUrl();
        String fielNameStr = url.substring(url.lastIndexOf("/"), url.length());
        final String filePath = DIRECTORY_DOWNLOAD + fielNameStr;
        //下载文件
        HttpHandler httpHandler = httpUtils.download(url, filePath, true, true, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //发送下载成功消息
                AppBus.getInstance().post(new DownloadPictureSuccessEvent(pictureBean, filePath));
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //发送下载失败的消息
                AppBus.getInstance().post(new DownloadPictureFailureEvent(pictureBean));
            }
        });
    }

    /**
     * 删除下载的图片
     *
     * @param fileName 要删除的文件名
     * @return
     */
    public boolean deleteDownloadPictureFile(String fileName) {
        boolean result = true;
        String path = DIRECTORY_DOWNLOAD + "/" + fileName;
        File file = new File(path);
        //文件存在
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

}
