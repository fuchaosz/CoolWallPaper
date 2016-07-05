package com.coolwallpaper.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Bmob升级管理表
 * Created by fuchao on 2016/7/4.
 */
public class MyBmobUpdate extends BmobObject {

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本号
     */
    private Integer versionCode;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 新版特点,更新信息
     */
    private String updateInfo;

    /**
     * 是否强制升级
     */
    private Boolean forceUpdate;

    /**
     * 安装包的大小
     */
    private Integer size;

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
