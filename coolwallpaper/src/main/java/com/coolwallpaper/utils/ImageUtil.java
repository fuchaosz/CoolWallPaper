package com.coolwallpaper.utils;

import android.graphics.Bitmap;

import com.coolwallpaper.MyApplication;
import com.coolwallpaper.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 图片处理工具
 * Created by fuchao on 2015/10/16.
 */
public class ImageUtil {

    private ImageLoaderConfiguration config;
    private DisplayImageOptions options;
    private static ImageUtil imageUtil;

    //私有构造函数
    private ImageUtil() {
        this.initConfig();
        this.initOptions();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ImageUtil getInstance() {
        if (imageUtil == null) {
            imageUtil = new ImageUtil();
        }
        return imageUtil;
    }

    //初始化Config
    private void initConfig() {
        //创建config
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(MyApplication.getInstance());
        //自定义缓存
        builder.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
        //缓存大小
        builder.memoryCacheSize(2 * 1024 * 1024);
        //相同的url只缓存一个图片
        builder.denyCacheImageMultipleSizesInMemory();
        //设置显示图片的线程池大小
        builder.threadPoolSize(5);
        config = builder.build();
    }

    //初始化ImageDisplayOptions
    private void initOptions() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        //加载图片时显示的图片
        builder.showImageOnLoading(R.drawable.coolwallpaper_empty);
        //加载图片失败时显示的图片
        builder.showImageOnFail(R.drawable.coolwallpaper_empty);
        //加载的图片为空的时候显示的图片
        builder.showImageForEmptyUri(R.drawable.coolwallpaper_empty);
        //不启用内存缓存,否则会报oom的错误
        builder.cacheInMemory(false);
        //启用外部缓存
        builder.cacheOnDisk(false);
        //是否考虑JPEG图像EXIF参数（旋转，翻转）
        builder.considerExifParams(true);
        //设置图片的编码类型
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        //图片显示风格
        //builder.displayer(new FadeInBitmapDisplayer(10));
        builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
        options = builder.build();
    }

    /**
     * 获取ImgaeLoader的config
     *
     * @return
     */
    public ImageLoaderConfiguration getImageLoaderConfiguration() {
        return config;
    }

    /**
     * 获取ImageLoader的DisplayOptions
     *
     * @return
     */
    public DisplayImageOptions getDisplayImageOptions() {
        return options;
    }

}
