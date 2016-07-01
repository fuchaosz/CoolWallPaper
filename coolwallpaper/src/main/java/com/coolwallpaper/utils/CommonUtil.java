package com.coolwallpaper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 通用工具
 * Created by fuchao on 2016/6/27.
 */
public class CommonUtil {

    /**
     * 获取本地图片的宽高
     *
     * @param path 本地图片路径
     * @return 数组，[0]是宽，[1]是高
     */
    public static int[] getPictureWidthAndHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //最关键在此，把options.inJustDecodeBounds = true,这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        int[] out = new int[2];
        out[0] = options.outWidth;
        out[1] = options.outHeight;
        return out;
    }

}
