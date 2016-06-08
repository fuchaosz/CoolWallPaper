package com.coolwallpaper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具
 * Created by fuchao on 2016/6/8.
 */
public class TimeUtil {

    private static String DEFAULT_FORMATE = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMATE);

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @return 默认时间格式 2016-6-8 19:00:00
     */
    public static String toString(Date date) {
        return df.format(date);
    }

    /**
     * 将字符串转换为Date对象
     *
     * @param timeStr 时间格式必须是 yyyy-MM-dd HH:mm:ss
     * @return 若发生异常则返回null
     */
    public static Date toDate(String timeStr) {
        Date date = null;
        try {
            date = df.parse(timeStr);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }
}
