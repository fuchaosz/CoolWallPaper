package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureBean;

/**
 * 更新图片的事件，用于显示新的图片
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureEvent extends BaseEvent{

    private PictureBean pictureBean;

    public UpdatePictureEvent(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
    }

    public PictureBean getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
    }
}
