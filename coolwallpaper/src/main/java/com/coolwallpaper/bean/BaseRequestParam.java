package com.coolwallpaper.bean;

import java.io.Serializable;

/**
 * 获取百度图片的请求参数的基类
 * 开发中途，百度图片改版，URL格式全部变了，于是重写写了该类，但基本的思路没变，继承关系不变
 * Created by fuchao on 2015/12/11.
 */
public abstract class BaseRequestParam implements Serializable {

    /**
     * 基URL
     */
    protected String baseUrl = "http://image.baidu.com/search/acjson";

    protected String cl = "2";

    protected String face = "0";

    protected String fp = "result";

    protected String ic = "0";

    protected String ie = "utf-8";

    protected String ipn = "rj";

    protected String istype = "2";

    protected String lm = "-1";

    protected String nc = "1";

    protected String oe = "utf-8";

    /**
     * 页码，从0开始
     */
    protected int pn = 0;

    /**
     * 每页显示的图片数
     */
    protected int rn = 30;

    protected String s = "0";

    protected String st = "-1";

    protected String tn = "resultjson_com";

    /**
     * 图片宽度
     */
    protected String width = "";

    /**
     * 关键词，注意：多层次的关键词用空格分开
     */
    protected String word = "";

    protected String z = "0";

    /**
     * 一级标题，例如：风景
     */
    protected String title1;

    /**
     * 二级标题，例如：雪景
     */
    protected String title2;

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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getIpn() {
        return ipn;
    }

    public void setIpn(String ipn) {
        this.ipn = ipn;
    }

    public String getIstype() {
        return istype;
    }

    public void setIstype(String istype) {
        this.istype = istype;
    }

    public String getLm() {
        return lm;
    }

    public void setLm(String lm) {
        this.lm = lm;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getOe() {
        return oe;
    }

    public void setOe(String oe) {
        this.oe = oe;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

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
}
