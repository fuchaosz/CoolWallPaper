package com.coolwallpaper.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobUpload;
import com.coolwallpaper.utils.CommonUtil;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.LogUtil;
import com.coolwallpaper.utils.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Bmob上传服务
 * 1 直接在Activity中获取Service的实例，从而直接调用Service的方法
 * 2 当解除绑定后就会根据用户的选择删除掉上传到bmob上的那些被取消的图片
 * 3 更新进度需要监听ACTION_UPDATE_PFROGRESS广播
 * Created by fuchao on 2016/6/28.
 */
public class BmobUploadService extends BaseService {

    public static final String TAG = "[BmobUploadService]";
    public static final String ACTION_UPDATE_PFROGRESS = "action_update_progress";
    private static List<String> pathList = new ArrayList<>();//要上传的图片地址
    private static Map<String, Integer> progressMap = new HashMap<>();//所有图片的上传进度
    private static Map<String, String> pathUrlMap = new HashMap<>();//上传完毕后，本地图片在bmob上的url地址
    private static Set<String> delPathSet = new HashSet<>();//取消上传的文件
    //private ExecutorService executor;//线程池
    private IUserInfo mUser;//用户

    /**
     * 绑定服务
     *
     * @param context
     * @param conn    由调用者实现ServiceConnection
     */
    public static void bindService(Context context, ServiceConnection conn) {
        Intent intent = new Intent(context, BmobUploadService.class);
        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务
     *
     * @param context
     * @param uploadList 要上传的图片的路径集合
     * @param desc       图片的描述
     */
    public static void startService(Context context, ArrayList<String> uploadList, String desc) {
        Intent intent = new Intent(context, BmobUploadService.class);
        intent.putExtra("UPLOADLIST", uploadList);
        intent.putExtra("DESC", desc);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    //取消绑定的时候，删除取消了的图片
    @Override
    public boolean onUnbind(Intent intent) {
        //判断有没有需要删除的图片
        if (delPathSet != null && delPathSet.size() > 0) {
            LogUtil.d("onUnbind()解绑，有需要删除的图片");
            delUploadFile(delPathSet);
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("服务被创建了,id=" + this.toString());
        //创建线程池
        //executor = Executors.newCachedThreadPool();
        //测试代码
//        pathList.add("hello");
//        pathList.add("world");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<String> uploadlist = (ArrayList<String>) intent.getSerializableExtra("UPLOADLIST");
        String desc = intent.getStringExtra("DESC");
        //上传图片
        upload(uploadlist, desc);
        return super.onStartCommand(intent, flags, startId);
    }

    //上传
    public void upload(List<String> uploadList, String desc) {
        //参数不能为空
        if (EmptyUtil.isEmpty(uploadList)) {
            LogUtil.e(TAG, "BmobUploadService upload uploadList = null");
            return;
        }
        //图片描述不能为空
        if (EmptyUtil.isEmpty(desc)) {
            LogUtil.e(TAG, "BmobUploadService upload desc = null");
            return;
        }
        //用户不能为空
        mUser = UserUtil.getInstance().getUser();
        if (mUser == null) {
            LogUtil.e(TAG, "BmobUploadService upload user = null");
            return;
        }
        //将要上传的图片加入列表
        pathList.addAll(uploadList);
        //开启线程
        //executor.execute(new UploadThread(uploadList, desc));
        new UploadThread(uploadList, desc).run();
    }

    //上传线程
    private class UploadThread extends Thread {

        List<String> uploadList;
        String desc;

        public UploadThread(List<String> uploadList, String desc) {
            this.uploadList = uploadList;
            this.desc = desc;
        }

        @Override
        public void run() {
            //判断参数
            if (EmptyUtil.isEmpty(uploadList)) {
                return;
            }
            LogUtil.d("开始上传图片");
            //查询bmob上传表中，所有这个用户上传的图片
            BmobQuery<MyBmobUpload> query = BmobUtil.getMyUploadQuery();
            query.addWhereEqualTo("account", mUser.getAccount());
            query.findObjects(getService(), new FindListener<MyBmobUpload>() {
                @Override
                public void onSuccess(List<MyBmobUpload> list) {
                    //如果不为空，说明这个用户之前上传过了图片
                    if (!EmptyUtil.isEmpty(list)) {
                        LogUtil.d(TAG, "用户上传过了图片");
                        //用户已经上传过的图片的集合
                        Set<String> userUploadedSet = new HashSet<String>();
                        //将上传过的图片名称放入集合中
                        for (MyBmobUpload u : list) {
                            userUploadedSet.add(u.getFileName());
                        }
                        //遍历需要上传的图片
                        Iterator<String> it = uploadList.iterator();
                        while (it.hasNext()) {
                            String tmpPath = it.next();
                            //如果是上传过的图片，则删除掉
                            if (userUploadedSet.contains(FileUtil.getFileName(tmpPath))) {
                                it.remove();
                                //告诉用户，这个图片上传成功了
                                LogUtil.d(TAG, "过滤掉，发送广播,tmpPath = " + tmpPath);
                                updateProgress(tmpPath, 100, 2000);
                            }
                        }
                    }
                    //判断是否还有剩余的
                    if (EmptyUtil.isEmpty(uploadList)) {
                        LogUtil.d(TAG, "本次上传的图片，改用户以前都上传过了");
                        return;
                    }
                    String[] paths = uploadList.toArray(new String[0]);
                    //批量上传图片
                    BmobFile.uploadBatch(BmobUploadService.this, paths, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> list1) {
                            LogUtil.d("上传图片成功 lsit = " + list.size() + "  urls = " + list1.size());
                            //取出最近上传成功的一个文件
                            BmobFile file = list.get(list.size() - 1);
                            pathUrlMap.put(file.getLocalFile().getAbsolutePath(), file.getUrl());
                            //写入bmob表
                            saveUploadInfo(file, desc);
                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {
                            synchronized (progressMap) {
                                progressMap.put(paths[i - 1], i1);
                            }
                            LogUtil.d(String.format("onProgress(%d,%d,%d,%d)", i, i1, i2, i3));
                            LogUtil.d(String.format("pathList.size=" + pathList.size()));
                            //刷新进度
                            updateProgress(paths[i - 1], i1);
                        }

                        @Override
                        public void onError(int i, String s) {
                            LogUtil.d("上传图片失败");
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    LogUtil.e(TAG, "查询图片是否存在时失败，s = " + s);
                }
            });

        }
    }

    //立即刷新进度
    private void updateProgress(String path, int progress) {
        updateProgress(path, progress, 0);
    }

    //刷新进度
    private void updateProgress(String path, int progress, int delay) {
        //发送广播，刷新进度
        Intent intent = new Intent();
        intent.putExtra("PATH", path);
        intent.putExtra("PROGRESS", progress);
        intent.setAction(ACTION_UPDATE_PFROGRESS);
        //立即发送
        if (delay == 0) {
            sendBroadcast(intent);
        }
        //延时发送
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(intent);
                }
            }, delay);
        }

    }

    //删除图片
    private void delUploadFile(Set<String> pathSet) {
        //根据本地路径找到url
        if (pathUrlMap == null) {
            return;
        }
        Iterator<String> it = pathSet.iterator();
        List<String> urlList = new ArrayList<>();
        //遍历所有path，找出对应的url
        while (it.hasNext()) {
            String path = it.next();
            String url = pathUrlMap.get(path);
            if (url != null) {
                urlList.add(url);
            }
        }
        //将List转换为数组
        String[] urls = urlList.toArray(new String[0]);
        //批量删除
        BmobFile.deleteBatch(this, urls, new DeleteBatchListener() {
            @Override
            public void done(String[] failUrls, BmobException e) {
                if (e == null) {
                    LogUtil.d("全部删除成功");
                } else {
                    if (failUrls != null) {
                        LogUtil.d("删除失败个数：" + failUrls.length + "," + e.toString());
                    } else {
                        LogUtil.d("全部文件删除失败：" + e.getErrorCode() + "," + e.toString());
                    }
                }
            }
        });
    }

    //将上传成功的单个图片写入bmob表
    private void saveUploadInfo(BmobFile bmobFile, String desc) {
        //判断参数是否
        if (bmobFile == null || EmptyUtil.isEmpty(desc)) {
            return;
        }
        //转换为bmob上传表的对象
        MyBmobUpload myBmobUpload = new MyBmobUpload();
        myBmobUpload.setAccount(mUser.getAccount());
        myBmobUpload.setUserName(mUser.getName());
        myBmobUpload.setUserImg(mUser.getImg());
        myBmobUpload.setUserSex(mUser.getSex());
        myBmobUpload.setFileUrl(bmobFile.getUrl());
        myBmobUpload.setFileName(bmobFile.getFilename());
        //获取本地图片的宽、高
        int[] picInfo = CommonUtil.getPictureWidthAndHeight(bmobFile.getLocalFile().getAbsolutePath());
        myBmobUpload.setWidth(picInfo[0]);
        myBmobUpload.setHeight(picInfo[1]);
        myBmobUpload.setDesc(desc);
        //保存到bmob表上
        myBmobUpload.save(getService());
    }

    //绑定服务的时候，返回这个变量给客户端
    public class MyBinder extends Binder {

        public BmobUploadService getUploadService() {
            return BmobUploadService.this;
        }
    }

    //获取正在上传的图片的进度
    public static Map<String, Integer> getProgressMap() {
        return progressMap;
    }

    //获取正在上传的图片列表
    public static List<String> getUploadingPathList() {
        return pathList;
    }

    //获取上传完毕后图片的path和bmob上地址url对应的map
    public static Map<String, String> getPathUrlMap() {
        return pathUrlMap;
    }

    //获取删除的列表
    public static Set<String> getDelPathSet() {
        return delPathSet;
    }

    //设置删除的列表
    public static void setDelPathSet(Set<String> delPathSet) {
        BmobUploadService.delPathSet = delPathSet;
    }

    @Override
    public void onDestroy() {
        LogUtil.d("服务被销毁掉了,id=" + this.toString());
        super.onDestroy();
    }
}
