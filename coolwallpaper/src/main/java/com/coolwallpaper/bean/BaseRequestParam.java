package com.coolwallpaper.bean;

import java.io.Serializable;

/**
 * 获取百度图片的请求参数的基类
 * 开发中途，百度图片改版，URL格式全部变了，于是重写写了该类，但基本的思路没变，继承关系不变
 * Created by fuchao on 2015/12/11.
 */
public abstract class BaseRequestParam implements Serializable {

    /**
     * 基URL,不同的图片来源有不同的baseUrl
     */
    protected String baseUrl = "";

    /**
     * 一级标签
     */
    protected String title1;

    /**
     * 二级标签
     */
    protected String title2;

    /**
     * 当前页数，第一页是0
     */
    protected int page = 0;

    /**
     * 每页显示的图片数
     */
    protected int pageSize = 30;


    /**
     * 默认构造函数
     */
    public BaseRequestParam() {
    }

    /**
     * 获取url的方法，留给子类去实现
     *
     * @return
     */
    public abstract String getUrl();

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
