package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureResult;

import java.util.List;

/**
 * 下载PicturenBean成功的消息
 * Created by fuchao on 2015/11/24.
 */
public class DownloadPictureBeanSuccessEvent extends BaseEvent {

    private List<PictureResult> beanList;

    public DownloadPictureBeanSuccessEvent(List<PictureResult> beanList) {
        this.beanList = beanList;
    }

    public List<PictureResult> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<PictureResult> beanList) {
        this.beanList = beanList;
    }
}
