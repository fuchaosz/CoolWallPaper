package com.coolwallpaper.event;

import com.coolwallpaper.model.Picture;

/**
 * 更新图片的事件，用于显示新的图片
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureEvent extends BaseEvent {

    private Picture pictureBean;

    public UpdatePictureEvent(Picture pictureBean) {
        this.pictureBean = pictureBean;
    }

    public Picture getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(Picture pictureBean) {
        this.pictureBean = pictureBean;
    }
}
