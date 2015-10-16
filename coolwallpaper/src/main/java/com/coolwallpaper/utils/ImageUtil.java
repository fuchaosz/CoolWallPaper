package com.coolwallpaper.utils;

import com.coolwallpaper.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by fuchao on 2015/10/16.
 */
public class ImageUtil {

    /**
     * 获取ImageLoader的DisplayImageOptions.以便统一管理各种默认的图片
     *
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(R.drawable.lms_spinner);
        builder.showImageOnFail(R.drawable.lms_spinner);
        builder.showImageOnLoading(R.drawable.lms_spinner);
        return builder.build();
    }
}
