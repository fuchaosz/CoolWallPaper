package com.coolwallpaper.bean;

import java.util.Map;

/**
 * 数据源接口
 * Created by admin on 2016/9/22.
 */
public interface DataSouece {

    /**
     * 获取一级标题的数目
     * @return
     */
    public int getTitleNum();

    /**
     * 获取一级标题
     * @param index
     * @return
     */
    public String getTitle1(int index);

    /**
     * 获取二级标题
     * @param index 序列号
     * @return
     */
    public String[] getTitle2(int index);

    /**
     * 通过一级标题获取二级标题
     * @param title1 一级标题
     * @return 二级标题
     */
    public String[] getTitle2(String title1);

    /**
     * 获取所有一级标题
     * @return
     */
    public String[] getAllTitle1();

    /**
     * 获取所有一级标题对应的二级标题
     * @return
     */
    public Map<String,String[]> getAllTitles();
}
