package com.coolwallpaper.event;

import com.coolwallpaper.model.Picture;

/**
 * 下载图片失败的消息
 * Created by fuchao on 2015/10/30.
 */
public class DownloadPictureFailureEvent extends BaseEvent{

    private Picture pictureBean;

    public DownloadPictureFailureEvent(Picture pictureBean){
        this.pictureBean = pictureBean;
    }

    public Picture getPictureBean() {
        return pictureBean;
    }
}
