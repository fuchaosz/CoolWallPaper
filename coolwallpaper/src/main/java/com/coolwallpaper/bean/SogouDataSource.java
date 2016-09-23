package com.coolwallpaper.bean;

import java.util.Map;

/**
 * 搜狗数据源
 * Created by admin on 2016/9/22.
 */
public class SogouDataSource implements DataSouece {


    public static String[] title1 = new String[]{"美女", "搞笑", "壁纸", "明星", "家具", "汽车", "艺术云图", "LOFTER"};
    public static String[][] title2 = new String[][]{
            {"", "", "", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", "", "", ""}};

    @Override
    public int getTitleNum() {
        return title1.length;
    }

    /**
     * 根据序号返回一级标题
     *
     * @param index
     * @return
     */
    @Override
    public String getTitle1(int index) {
        if (index < 0 || index >= getTitleNum()) {
            throw new IndexOutOfBoundsException();
        }
        return title1[index];
    }

    /**
     * 根据序号返回二级标题
     *
     * @param index 序列号
     * @return
     */
    @Override
    public String[] getTitle2(int index) {
        if (index < 0 || index >= getTitleNum()) {
            throw new IndexOutOfBoundsException();
        }
        return title2[index];
    }

    /**
     * 根据一级标题找到二级标题
     *
     * @param title1Key 查询一级标题关键字
     * @return title1Key对应的二级标题
     */
    @Override
    public String[] getTitle2(String title1Key) {
        int index = -1;
        //遍历找到
        for (int i = 0; i < title1.length; i++) {
            if (title1[i].equals(title1Key)) {
                index = i;
                break;
            }
        }
        //存在一级标题
        if (index != -1) {
            return title2[index];
        }
        //title1Key标题不存在
        else {
            return null;
        }

    }

    @Override
    public String[] getAllTitle1() {
        return title1;
    }

    @Override
    public Map<String, String[]> getAllTitles() {
        //暂未实现
        return null;
    }
}
