package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureBean;

import java.util.List;

/**
 * 下载PicturenBean成功的消息
 * Created by fuchao on 2015/11/24.
 */
public class DownloadPictureBeanSuccessEvent extends BaseEvent {

    private List<PictureBean> beanList;

    public DownloadPictureBeanSuccessEvent(List<PictureBean> beanList) {
        this.beanList = beanList;
    }

    public List<PictureBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<PictureBean> beanList) {
        this.beanList = beanList;
    }
}
