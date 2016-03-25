package com.coolwallpaper.utils;

import com.coolwallpaper.model.LocalPicture;
import com.coolwallpaper.model.LocalPictureDao;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 本地图片操作类,主要是把对本地壁纸数据库表的操作进行了封装
 * Created by fuchao on 2016/3/25.
 */
public class LocalPaperUtil {

    private static LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();

    /**
     * 批量插入本地图片到数据库中
     *
     * @param pathList 本地图片的路径的集合
     */
    public static void insertLocalPaperList(List<String> pathList) {
        //参数不能为空
        if (pathList == null || pathList.size() == 0) {
            return;
        }
        //遍历所有图片
        for (String path : pathList) {
            //插入单个本地图片
            insertLocalPaper(path);
        }
    }

    /**
     * 插入单个的本地图片到数据库
     *
     * @param path 本地图片的绝对路径
     * @return
     */
    public static boolean insertLocalPaper(String path) {
        boolean result = true;
        File file = new File(path);
        //如果文件不存在则返回插入失败
        if (!file.exists()) {
            return false;
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
        try {
            //插入的时候不用判断是否存在，因为数据库设置了path字段唯一，但是一旦重复还是会抛出异常，所以要try-catch
            localPictureDao.insert(localPicture);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有本地图片
     *
     * @return
     */
    public static List<LocalPicture> getLocalPaperList() {
        //访问数据库
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        QueryBuilder qb = localPictureDao.queryBuilder();
        List list = qb.listLazy();
        //筛选
        Iterator<LocalPicture> it = list.iterator();
        //遍历，删除不存在的文件
        while (it.hasNext()) {
            LocalPicture pic = it.next();
            File file = new File(pic.getPath());
            //如果文件不存在则删除
            if (!file.exists()) {
                //从数据库中删除掉
                localPictureDao.delete(pic);
            }
        }
        return qb.list();
    }


    /**
     * 获取所有本地图片的路径
     *
     * @return 所有本地图片的绝对路径
     */
    public static List<String> getLocalPaperPathList() {
        List<String> result = new ArrayList<>();
        //访问数据库
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        QueryBuilder qb = localPictureDao.queryBuilder();
        List list = qb.listLazy();
        //筛选
        Iterator<LocalPicture> it = list.iterator();
        //遍历，删除不存在的文件
        while (it.hasNext()) {
            LocalPicture pic = it.next();
            File file = new File(pic.getPath());
            //如果文件存在则加入结果集
            if (file.exists()) {
                result.add(pic.getPath());
            }
            //图片已经不存在了
            else {
                //从数据库中删除掉
                localPictureDao.delete(pic);
            }
        }
        return result;
    }

    /**
     * 批量删除本地图片
     *
     * @param pathList 本地图片的绝对路径
     */
    public static void deleteLocalPaperList(List<String> pathList) {
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        QueryBuilder qb = localPictureDao.queryBuilder();
        //遍历
        for (String path : pathList) {
            qb.where(LocalPictureDao.Properties.Path.eq(path));
            //如果数据库存在则删除
            localPictureDao.deleteInTx(qb.list());
        }
    }

    /**
     * 删除单个本地图片
     *
     * @param path 本地图片的路径
     */
    public static void deleteLocalPaper(String path) {
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        QueryBuilder qb = localPictureDao.queryBuilder();
        //查出本地图片
        qb.where(LocalPictureDao.Properties.Path.eq(path));
        //如果数据库存在则删除
        localPictureDao.deleteInTx(qb.list());
    }
}
