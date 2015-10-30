package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureBean;

/**
 * 下载图片失败的消息
 * Created by fuchao on 2015/10/30.
 */
public class DownloadPictureFailureEvent extends BaseEvent{

    private PictureBean pictureBean;

    public DownloadPictureFailureEvent(PictureBean pictureBean){
        this.pictureBean = pictureBean;
    }

    public PictureBean getPictureBean() {
        return pictureBean;
    }
}
