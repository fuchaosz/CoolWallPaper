package com.coolwallpaper.event;

import com.coolwallpaper.bean.PictureResult;

/**
 * 更新图片的事件，用于显示新的图片
 * Created by fuchao on 2015/10/30.
 */
public class UpdatePictureEvent extends BaseEvent{

    private PictureResult pictureBean;

    public UpdatePictureEvent(PictureResult pictureBean) {
        this.pictureBean = pictureBean;
    }

    public PictureResult getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(PictureResult pictureBean) {
        this.pictureBean = pictureBean;
    }
}
