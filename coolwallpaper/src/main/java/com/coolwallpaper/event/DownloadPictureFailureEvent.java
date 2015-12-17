package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureResult;

/**
 * 下载图片失败的消息
 * Created by fuchao on 2015/10/30.
 */
public class DownloadPictureFailureEvent extends BaseEvent{

    private PictureResult pictureBean;

    public DownloadPictureFailureEvent(PictureResult pictureBean){
        this.pictureBean = pictureBean;
    }

    public PictureResult getPictureBean() {
        return pictureBean;
    }
}
