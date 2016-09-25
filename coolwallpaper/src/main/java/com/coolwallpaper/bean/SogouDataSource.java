package com.coolwallpaper.bean;

import java.util.Map;

/**
 * 搜狗数据源
 * Created by admin on 2016/9/22.
 */
public class SogouDataSource implements DataSouece {

    public static String [] key = new String[]{};
    public static String[] title1 = new String[]{"美女", "壁纸", "明星", "家具", "汽车", "LOFTER"};
    public static String[][] title2 = new String[][]{
            {"全部", "车模", "时尚", "文艺", "女神", "甜素纯", "甜美", "清纯", "唯美", "风情", "摄影", "妹纸", "小清新", "高挑", "可爱", "惊艳", "长腿美女"},
            {"全部", "世界风光", "动物", "明星", "影视", "日韩明星", "摄影", "游戏", "军事", "微软", "萌宠", "静物", "卡通", "清新", "优质", "日历", "手绘"},
            {"全部", "宋仲基", "鹿晗", "霍建华", "林心如", "陈学冬", "杨幂", "angelababy", "赵丽颖", "刘诗诗", "刘涛", "唐嫣"},
            {"全部", "新古典", "乡村田园", "现代", "混搭", "美式", "北欧", "复式", "别墅"},
            {"全部", "超跑", "官方车图", "经典名车", "豪车", "车模", "车展"},
            {"全部", "最文艺", "拍私房", "LOFTER少女", "旅行微攻略", "遇见世界", "今天穿什么", "美妆潮流", "慢食堂", "元气早餐", "萌宠"}
    };

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
