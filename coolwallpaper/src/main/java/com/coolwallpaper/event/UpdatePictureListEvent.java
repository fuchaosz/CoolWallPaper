package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureResult;

import java.util.List;

/**
 * 更新图片列表事件
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureListEvent extends BaseEvent {

    private List<PictureResult> beanList;

    public UpdatePictureListEvent(List<PictureResult> beanList) {
        this.beanList = beanList;
    }

    public List<PictureResult> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<PictureResult> beanList) {
        this.beanList = beanList;
    }
}
