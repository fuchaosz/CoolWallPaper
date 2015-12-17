package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureResult;

import cn.trinea.android.common.util.FileUtils;

/**
 * 图片文件下载成功消息
 * Created by fuchao on 2015/10/30.
 */
public class DownloadPictureSuccessEvent extends BaseEvent {

    private PictureResult pictureBean;//下载的PictureBean
    private String savePath;//文件在本地的保存路径，绝对路径

    public DownloadPictureSuccessEvent(PictureResult pictureBean, String savePath) {
        this.pictureBean = pictureBean;
        this.savePath = savePath;
    }

    public PictureResult getPictureBean() {
        return pictureBean;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFileName() {
        return FileUtils.getFileName(savePath);
    }
}
