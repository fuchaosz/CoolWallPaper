package com.coolwallpaper.bean;

import java.net.URLEncoder;

/**
 * 搜狗图片搜索的参数
 * Created by fuchao on 2016/4/19.
 */
public class SearchRequestParam extends SogouBaseRequestParam {

    protected String _asf = "pic.sogou.com";
    protected int mode = 1;
    protected int mood = 0;
    protected int picformat = 0;
    protected String query;//查询关键字，注意：编码格式gbk-2312
    protected String reqFrom = "result";
    protected String reqType = "ajax";
    protected int tn = 0;//默认为0

    /**
     * 构造函数
     *
     * @param query 查询关键字
     */
    public SearchRequestParam(String query) {
        this.query = query;
        //搜索时用的url和一般的不同
        this.baseUrl = "http://pic.sogou.com/pics";
    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(baseUrl);
            //单纯的搜关键字出来的图片不好看
            builder.append("?query=" + URLEncoder.encode(query.trim(), "GBK"));
            builder.append("&mood=" + mood);
            builder.append("&picformat=" + picformat);
            builder.append("&mode=" + mode);
            builder.append("&_asf=" + _asf);
            builder.append("&start=" + start);
            builder.append("&reqType=" + reqType);
            builder.append("&tn=" + tn);
            builder.append("&reqFrom=" + reqFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String get_asf() {
        return _asf;
    }

    public void set_asf(String _asf) {
        this._asf = _asf;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getPicformat() {
        return picformat;
    }

    public void setPicformat(int picformat) {
        this.picformat = picformat;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getReqFrom() {
        return reqFrom;
    }

    public void setReqFrom(String reqFrom) {
        this.reqFrom = reqFrom;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public int getTn() {
        return tn;
    }

    public void setTn(int tn) {
        this.tn = tn;
    }
}
