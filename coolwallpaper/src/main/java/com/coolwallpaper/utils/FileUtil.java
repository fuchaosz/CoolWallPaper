package com.coolwallpaper.utils;

import android.os.Environment;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.constant.AppBus;
import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.model.LocalPicture;
import com.coolwallpaper.model.LocalPictureDao;
import com.coolwallpaper.model.Picture;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 文件操作类,只于文件有关
 * Created by fuchao on 2015/10/30.
 */
public class FileUtil {

    private static FileUtil fileUtil;
    public String DIRECTORY_DOWNLOAD = "/mnt/sdcard/coolwallpaper/download";//下载文件的保存目录
    public String DIRECTORY_FAVORITE = "/mnt/sdcard/coolwallpaper/favorite";//收藏文件的保存目录
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
                //下载成功，保存到本地数据库
                saveLocalPicture(filePath);
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

    /**
     * 删除文件
     *
     * @param path 文件的绝对路径
     * @return 是否删除成功，文件不存在也是删除成功了
     */
    public boolean deleteFile(String path) {
        boolean result = true;
        File file = new File(path);
        //文件存在
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 批量删除文件
     *
     * @param filePathList 要删除的文件的绝对路径的集合
     */
    public void deleteFileList(List<String> filePathList) {
        if (filePathList == null || filePathList.size() == 0) {
            return;
        }
        //遍历删除
        for (String path : filePathList) {
            deleteFile(path);
        }
    }


    /**
     * 保存本地图片到数据库,注意只有在下载成功才可以调用这个方法
     *
     * @param path 下载成功的图片的路径
     */
    public void saveLocalPicture(String path) {
        File file = new File(path);
        //如果文件不存在或者是个目录，则直接
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        long size = file.getTotalSpace();//获取文件大小
        String name = file.getName();//获取文件的名称
        Date createTime = new Date(file.lastModified());//获取最后修改时间
        //保存到数据库
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        //创建bean
        LocalPicture localPicture = new LocalPicture();
        localPicture.setPath(path);
        localPicture.setName(name);
        localPicture.setSize(size);
        localPicture.setCrateTime(createTime);
        //保存到数据库
        localPictureDao.insert(localPicture);
    }

}
