package com.coolwallpaper.event;

/**
 * 网络连接超时事件
 * Created by fuchao on 2015/11/24.
 */
public class NetworkConnectionTimeOutEvent extends BaseEvent {

    public String url;//连接超时的地址

    public NetworkConnectionTimeOutEvent() {
    }

    public NetworkConnectionTimeOutEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
