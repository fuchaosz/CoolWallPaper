package com.coolwallpaper.fragment;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coolwallpaper.R;
import com.coolwallpaper.ShowPictureDetailActivity;
import com.coolwallpaper.model.Picture;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * 图片详情界面的Fragment
 * Created by fuchao on 2016/4/5.
 */
public class PictureDetailFragment extends BaseFragment implements View.OnClickListener {

    private Picture picture;
    private Matrix matrix;
    private int currentProgress = 50;
    private float maxMoveLength;//最大可以移动的距离
    private ShowPictureDetailActivity activity;

    //图片控件
    @Bind(R.id.iv_image)
    ImageView ivImage;

    //进度条
    @Bind(R.id.pb_progress)
    ProgressBar progressBar;

    //空白页面
    @Bind(R.id.ly_empty)
    View lyEmpty;

    //空白页面上的刷新按钮
    @Bind(R.id.btn_refresh)
    Button btnRefresh;

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
        //隐藏空白页面
        this.lyEmpty.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_refresh, R.id.iv_image})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击图片
            case R.id.iv_image:
                //显示覆盖的界面
                activity.showMaskMenu();
                break;
            //空白页面上的刷新按钮
            case R.id.btn_refresh:
                //再次显示图片
                showPicture(picture);
                break;
        }
    }

    //显示大图
    private void showPicture(final Picture picture) {
        Glide.with(getActivity()).load(picture.getDownloadUrl()).override(200, 200).into(new SimpleTarget<GlideDrawable>() {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
                //隐藏seekBar
                ((ShowPictureDetailActivity) getActivity()).hideSeekBar();
            }

            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                //显示图片
                ivImage.setImageDrawable(glideDrawable);
                //第一次加载的话要放大图片
                scalePictureToScreenHeight();
                //显示seekBar
                ((ShowPictureDetailActivity) getActivity()).showSeekBar();
                //隐藏进度条
                progressBar.setVisibility(View.GONE);
                //隐藏空白页
                lyEmpty.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Logger.d("大图加载失败,去加载小图,url = " + picture.getThumbUrl());
                //如果大图加载失败了，那么必须加载小图
                showThumbnail(picture);
            }
        });
    }

    //显示小图,大图加载失败的时候要显示小图
    private void showThumbnail(final Picture picture) {
        Glide.with(getActivity()).load(picture.getThumbUrl()).into(new SimpleTarget<GlideDrawable>() {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
                //隐藏seekBar
                ((ShowPictureDetailActivity) getActivity()).hideSeekBar();
            }

            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                //显示图片
                ivImage.setImageDrawable(glideDrawable);
                //第一次加载的话要放大图片
                scalePictureToScreenHeight();
                //显示seekBar
                ((ShowPictureDetailActivity) getActivity()).showSeekBar();
                //隐藏进度条
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Logger.d("小图也加载失败了,url = " + picture.getThumbUrl());
                Toast.makeText(getActivity(), "图片加载失败了", Toast.LENGTH_SHORT).show();
                //显示空白页
                lyEmpty.setVisibility(View.VISIBLE);
            }
        });
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
