package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureBean;

import java.util.List;

/**
 * 更新图片列表事件
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureListEvent extends BaseEvent {

    private List<PictureBean> beanList;

    public UpdatePictureListEvent(List<PictureBean> beanList) {
        this.beanList = beanList;
    }

    public List<PictureBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<PictureBean> beanList) {
        this.beanList = beanList;
    }
}
