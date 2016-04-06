package com.coolwallpaper.event;

import com.coolwallpaper.model.Picture;

import java.util.List;

/**
 * 更新图片的事件，用于显示新的图片
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureEvent extends BaseEvent {

    private List<Picture> beanList;//图片集合
    private int position;//开始显示的列表

    public UpdatePictureEvent(List<Picture> beanList, int position) {
        this.beanList = beanList;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Picture> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<Picture> beanList) {
        this.beanList = beanList;
    }
}
