package com.coolwallpaper.event;

import com.coolwallpaper.model.Picture;

import java.util.List;

/**
 * 下载PicturenResult成功的消息
 * Created by fuchao on 2015/11/24.
 */
public class DownloadPictureResultSuccessEvent extends BaseEvent {

    private List<Picture> pictureList;

    public DownloadPictureResultSuccessEvent(List<Picture> beanList) {
        this.pictureList = beanList;
    }

    public List<Picture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }
}
