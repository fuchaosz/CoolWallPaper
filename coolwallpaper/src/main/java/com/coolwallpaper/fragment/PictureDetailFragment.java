package com.coolwallpaper.fragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.coolwallpaper.R;
import com.coolwallpaper.ShowPictureDetailActivity;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import butterknife.Bind;

/**
 * 图片详情界面的Fragment
 * Created by fuchao on 2016/4/5.
 */
public class PictureDetailFragment extends BaseFragment implements View.OnClickListener {

    private Picture picture;
    private Matrix matrix;
    private float maxMoveLength;//最大可以移动的距离
    private int currentProgress = 50;//当前进度
    private ShowPictureDetailActivity activity;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //图片控件
    @Bind(R.id.iv_image)
    ImageView ivImage;

    //进度条
    @Bind(R.id.pb_progress)
    ProgressBar progressBar;

    /**
     * 创建PictureDetailFragment的方法
     *
     * @param picture
     * @return
     */
    public static PictureDetailFragment newInstance(Picture picture) {
        PictureDetailFragment fragment = new PictureDetailFragment();
        Bundle data = new Bundle();
        data.putSerializable("PICTURE", picture);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pic_detail_item, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //取出数据
        Bundle data = getArguments();
        this.picture = (Picture) data.getSerializable("PICTURE");
        this.activity = (ShowPictureDetailActivity) getActivity();
        //初始化
        this.init();
        //显示图片
        this.showPicture(picture);
    }

    //初始化
    private void init() {
        this.imageLoader.init(ImageUtil.getInstance().getImageLoaderConfiguration());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击图片
            case R.id.iv_image:
                //显示覆盖的界面
                activity.showMaskMenu();
                break;
        }
    }

    //显示大图
    private void showPicture(final Picture picture) {
        //使用ImageLoader加载小图片
        this.imageLoader.displayImage(picture.getThumbUrl(), ivImage, ImageUtil.getInstance().getDisplayImageOptions(), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });

        //        Glide.with(this).load(picture.getDownloadUrl()).into(new SimpleTarget<GlideDrawable>() {
        //            @Override
        //            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
        //                Logger.d("onResourceReady()");
        //                //显示图片
        //                ivImage.setImageDrawable(resource);
        //                //第一次加载的话要放大图片
        //                scalePictureToScreenHeight();
        //                //显示seekBar
        //                ((ShowPictureDetailActivity) getActivity()).showSeekBar();
        //                //隐藏进度条
        //                progressBar.setVisibility(View.GONE);
        //            }
        //
        //            @Override
        //            public void onLoadStarted(Drawable placeholder) {
        //                super.onLoadStarted(placeholder);
        //                Logger.d("onLoadStarted()");
        //                //显示进度条
        //                progressBar.setVisibility(View.VISIBLE);
        //                //隐藏seekBar
        //                ((ShowPictureDetailActivity) getActivity()).hideSeekBar();
        //            }
        //
        //            //加载失败
        //            @Override
        //            public void onLoadFailed(Exception e, Drawable errorDrawable) {
        //                super.onLoadFailed(e, errorDrawable);
        //                Logger.d("onLoadFailed()");
        //                Logger.d("图片加载失败了,url = " + picture.getDownloadUrl());
        //                AppMsg.makeText(getActivity(), "图片加载失败了", AppMsg.STYLE_ALERT);
        //                //如果大图加载失败了，那么必须加载小图
        //                showThumbnail();
        //            }
        //        });
    }

    //显示小图,大图加载失败的时候要显示小图
    private void showThumbnail() {
        //使用ImageLoader加载小图片
        this.imageLoader.displayImage(picture.getThumbUrl(), ivImage, ImageUtil.getInstance().getDisplayImageOptions(), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });

        //        Glide.with(this).load(picture.getThumbUrl()).into(new SimpleTarget<GlideDrawable>() {
        //            @Override
        //            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
        //                Logger.d("onResourceReady()");
        //                //显示图片
        //                ivImage.setImageDrawable(resource);
        //                //第一次加载的话要放大图片
        //                scalePictureToScreenHeight();
        //                //显示seekBar
        //                ((ShowPictureDetailActivity) getActivity()).showSeekBar();
        //                //隐藏进度条
        //                progressBar.setVisibility(View.GONE);
        //            }
        //
        //            @Override
        //            public void onLoadStarted(Drawable placeholder) {
        //                super.onLoadStarted(placeholder);
        //                Logger.d("onLoadStarted()");
        //                //显示进度条
        //                progressBar.setVisibility(View.VISIBLE);
        //                //隐藏seekBar
        //                ((ShowPictureDetailActivity) getActivity()).hideSeekBar();
        //            }
        //
        //            //加载失败
        //            @Override
        //            public void onLoadFailed(Exception e, Drawable errorDrawable) {
        //                super.onLoadFailed(e, errorDrawable);
        //                Logger.d("onLoadFailed()");
        //                Logger.d("图片加载失败了,url = " + picture.getDownloadUrl());
        //                AppMsg.makeText(getActivity(), "图片加载失败了", AppMsg.STYLE_ALERT);
        //            }
        //
        //        });
    }

    //放大图片
    private void scalePictureToScreenHeight() {
        this.matrix = ivImage.getImageMatrix();
        //获取ImageView的大小
        int viewHeight = ivImage.getHeight() - ivImage.getPaddingBottom() - ivImage.getPaddingTop();
        int viewWidth = ivImage.getWidth() - ivImage.getPaddingLeft() - ivImage.getPaddingRight();
        //获取图片的实际大小
        int drawWidth = ivImage.getDrawable().getIntrinsicWidth();
        int drawHeight = ivImage.getDrawable().getIntrinsicHeight();
        //图片缩放，保证高度铺满整个屏幕
        float scale = viewHeight * 1.0f / drawHeight;
        matrix.postScale(scale, scale);
        ivImage.setImageMatrix(matrix);
        //计算出放大之后的图片的宽度,高度就是屏幕的高度
        float widthAfterScale = drawWidth * scale;
        //最大移动距离
        this.maxMoveLength = (widthAfterScale - viewWidth) / 2;
        //将图片移动到中间去
        matrix.postTranslate(-maxMoveLength, 0);
        ivImage.setImageMatrix(matrix);
    }

    //移动图片
    public void movePicture(int progress) {
        //计算需要移动的比例
        float moveDistance = (progress - currentProgress) / 50f * maxMoveLength;
        matrix.postTranslate(-moveDistance, 0);
        ivImage.setImageMatrix(matrix);
        currentProgress = progress;
    }
}
