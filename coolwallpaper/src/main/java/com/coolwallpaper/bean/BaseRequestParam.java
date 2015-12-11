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
    protected String baseUrl = "http://image.baidu.com/search/avatarjson";

    /**
     * 表示是壁纸
     */
    protected String cg = "wallpaper";

    protected String fr;

    protected String gsm = "3c";

    protected String height = "";

    protected String ic = "";

    /**
     * 输入的字符编码格式
     */
    protected String ie = "utf-8";

    protected String itg = "1";

    protected int lm = -1;

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

    protected String tn = "resultjsonavatarnew";

    /**
     * 图片宽度
     */
    protected String width = "";

    /**
     * 关键词，注意：多层次的关键词用空格分开
     */
    protected String word = "";

    protected String z = "";

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

    public String getCg() {
        return cg;
    }

    public void setCg(String cg) {
        this.cg = cg;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public String getItg() {
        return itg;
    }

    public void setItg(String itg) {
        this.itg = itg;
    }

    public int getLm() {
        return lm;
    }

    public void setLm(int lm) {
        this.lm = lm;
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
