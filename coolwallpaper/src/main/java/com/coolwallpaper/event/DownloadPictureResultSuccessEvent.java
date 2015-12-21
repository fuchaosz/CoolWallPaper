package com.coolwallpaper.event;

import com.coolwallpaper.bean.BaseRequestParam;

/**
 * 下载PicturenResult成功的消息
 * Created by fuchao on 2015/11/24.
 */
public class DownloadPictureResultSuccessEvent extends BaseEvent {

    private BaseRequestParam requestParam;//查询的参数

    public DownloadPictureResultSuccessEvent(BaseRequestParam requestParam) {
        this.requestParam = requestParam;
    }

    public BaseRequestParam getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(BaseRequestParam requestParam) {
        this.requestParam = requestParam;
    }
}
