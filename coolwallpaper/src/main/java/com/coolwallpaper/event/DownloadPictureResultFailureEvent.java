package com.coolwallpaper.event;

/**
 * PictureResult下载失败事件
 * Created by fuchao on 2015/11/24.
 */
public class DownloadPictureResultFailureEvent extends BaseEvent {

    private String reason;//失败原因

    public DownloadPictureResultFailureEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
