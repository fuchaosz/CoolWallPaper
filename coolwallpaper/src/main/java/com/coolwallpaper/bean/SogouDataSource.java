package com.coolwallpaper.bean;

import java.util.Map;

/**
 * 搜狗数据源
 * Created by admin on 2016/9/22.
 */
public class SogouDataSource implements DataSouece{


    public static String[] title1 = new String[]{"美女","搞笑","壁纸","明星","家具","","","","",""},

    @Override
    public int getTitleNum() {
        return 0;
    }

    @Override
    public String getTitle1(int index) {
        return null;
    }

    @Override
    public String[] getTitle2(int index) {
        return new String[0];
    }

    @Override
    public String[] getAllTitle1() {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getAllTitles() {
        return null;
    }
}
