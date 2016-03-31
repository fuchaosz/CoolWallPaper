package com.coolwallpaper;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.event.UpdatePictureEvent;
import com.coolwallpaper.fragment.PictureListFragment;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.FileUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.common.util.ToastUtils;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    private Picture pictureBean;
    private int position;
    private ImageLoader imageLoader;
    private Matrix matrix;
    private float maxMoveLength;//最大可以移动的距离
    private int currentProgress = 50;//当前进度
    private PictureListFragment fragment;//左边的图片的列表
    private List<Picture> beanList;//图片列表
    private Drawable favoriteAddDrawable;//没有收藏的时候显示的drawable
    private Drawable favoriteRemoveDrawable;//收藏了之后显示的drawable
    private MyAdapter adapter;

    @ViewInject(R.id.iv_image)
    ImageView ivImage;

    //浮动层菜单
    @ViewInject(R.id.ly_picture_detail_menu)
    View lyPictureDetailMenu;

    @ViewInject(R.id.pb_progress)
    ProgressBar progressBar;

    @ViewInject(R.id.sb_seekbar)
    SeekBar seekBar;

    //左边的Fragment
    @ViewInject(R.id.fl_left_container)
    View flContainer;

    //收藏图片
    @ViewInject(R.id.iv_favorite)
    ImageView ivFavorite;

    //滑动界面
    @Bind(R.id.hs_scrollview)
    HorizontalScrollView srcollView;

    /**
     * 启动方法，要显示的图片
     *
     * @param context
     * @param postion  当前显示的图片在列表中的位置
     * @param beanList 图片列表
     */
    public static void startActivity(Context context, int postion, List<Picture> beanList) {
        Intent intent = new Intent(context, ShowPictureDetailActivity.class);
        intent.putExtra("POSITION", postion);
        intent.putExtra("BEAN_LIST", (Serializable) beanList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        this.position = getIntent().getIntExtra("POSITION", 0);
        this.beanList = (List<Picture>) getIntent().getSerializableExtra("BEAN_LIST");
        this.pictureBean = beanList.get(position);
        //初始化
        this.init();
        //添加监听器
        this.addListener();
    }

    //初始化
    private void init() {
        //创建ImageLoader
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //显示图片
        //this.showPicture(true);
        //this.showPictureWithGlide(true);
        //添加图片列表的Fragment
        this.fragment = PictureListFragment.newInstance(beanList);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fl_left_container, fragment);
        transaction.commit();
        //创建收藏图片的Drawable
        this.favoriteAddDrawable = getResources().getDrawable(R.drawable.btn_add_favorate_selector);
        this.favoriteRemoveDrawable = getResources().getDrawable(R.drawable.btn_remove_favorate_selector);
        //默认为未收藏的按钮
        this.ivFavorite.setImageDrawable(favoriteAddDrawable);
        //创建adapter
        adapter = new MyAdapter(this, beanList);
        srcollView.add(adapter);
    }

    //添加监听器
    private void addListener() {
        //进度条
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //移动图片
                movePicture(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.iv_image).setOnClickListener(this);
        findViewById(R.id.ly_picture_detail_menu).setOnClickListener(this);
        findViewById(R.id.ly_similar_pic).setOnClickListener(this);
        findViewById(R.id.ly_title).setOnClickListener(this);
        findViewById(R.id.ly_favorite_pic).setOnClickListener(this);
    }

    //查询下载量和收藏量
    private void queryMyBmobPicture() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击图片
            case R.id.iv_image:
                //弹出浮动层菜单
                lyPictureDetailMenu.setVisibility(View.VISIBLE);
                ToastUtils.show(this, "点击了图片");
                break;
            //顶部向左的箭头
            case R.id.btn_left:
                this.finish();
                break;
            //点击浮动层
            case R.id.ly_picture_detail_menu:
                onDetailMenuClick();
                break;
            //点击相似图片
            case R.id.ly_similar_pic:
                //显示图片列表fragment
                showFragmentView();
                break;
            //收藏图片
            case R.id.ly_favorite_pic:
                doFavorite();
                break;
        }
    }

    //显示图片
    private void showPicture(final boolean isInit) {
        this.imageLoader.stop();
        this.imageLoader.displayImage(pictureBean.getDownloadUrl(), ivImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressBar.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //调整图片，使得图片的高度与屏幕一样高
                if (isInit) {
                    //只在第一次放大图片，之后就不需要放大了
                    scalePictureToScreenHeight();
                }
                seekBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                //刷新界面
                ivImage.invalidate();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    //显示图片，使用Glide
    private void showPictureWithGlide(final boolean isInit) {
        Glide.with(this).load(pictureBean.getDownloadUrl()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                //显示图片
                ivImage.setImageDrawable(resource);
                //第一次加载的话要放大图片
                if (isInit) {
                    scalePictureToScreenHeight();
                }
                //隐藏进度条
                seekBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
            }
        });
        //.into(ivImage);
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
    private void movePicture(int progress) {
        //计算需要移动的比例
        float moveDistance = (progress - currentProgress) / 50f * maxMoveLength;
        matrix.postTranslate(-moveDistance, 0);
        ivImage.setImageMatrix(matrix);
        currentProgress = progress;
    }

    //浮动层点击事件
    private void onDetailMenuClick() {
        //如果Fragment显示了，则隐藏掉
        if (flContainer.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.coolwallpaper_slid_out_to_left);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    flContainer.clearAnimation();
                    flContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            flContainer.clearAnimation();
            flContainer.startAnimation(animation);
        }
        //Fragment没有显示，则隐藏掉浮动层
        else {
            lyPictureDetailMenu.setVisibility(View.GONE);
        }
    }

    //显示左边的fragment
    private void showFragmentView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.coolwallpaper_slid_in_from_left);
        animation.setFillAfter(true);
        flContainer.clearAnimation();
        flContainer.startAnimation(animation);
        flContainer.setVisibility(View.VISIBLE);
    }

    //订阅图片刷新事件
    @Subscribe
    public void updatePicture(UpdatePictureEvent event) {
        this.pictureBean = event.getPictureBean();
        showPicture(false);
    }

    //订阅下载文件成功的事件
    @Subscribe
    public void downloadSuccessEvent(DownloadPictureSuccessEvent event) {
        ToastUtils.show(this, "图片成功收藏到本地" + event.getSavePath());
    }

    //订阅下载文件失败的事件
    @Subscribe
    public void downloadFailuerEvent(DownloadPictureFailureEvent event) {
        ToastUtils.show(this, "图片 " + event.getPictureBean().getDesc() + " 收藏失败");
        //设置按钮为未收藏的状态
        this.ivFavorite.setImageDrawable(favoriteAddDrawable);
    }

    //点击了收藏按钮
    private void doFavorite() {
        //如果是没有收藏的状态
        if (ivFavorite.getDrawable() == favoriteAddDrawable) {
            //设置按钮为已经收藏的状态
            this.ivFavorite.setImageDrawable(favoriteRemoveDrawable);
            //下载图片
            FileUtil.getInstance().downloadPictureFile(pictureBean);
        }
        //已经收藏的状态
        else {
            //设置按钮为未收藏的状态
            this.ivFavorite.setImageDrawable(favoriteAddDrawable);
            //删除收藏的图片
            FileUtil.getInstance().deleteDownloadPictureFile(FileUtil.getFileName(pictureBean.getDownloadUrl()));
            ToastUtils.show(this, "取消收藏成功");
        }
    }

    //适配器
    class MyAdapter extends PagerAdapter {

        Context context;
        List<Picture> pictureList;

        public MyAdapter(Context context, List<Picture> pictureList) {
            this.context = context;
            this.pictureList = pictureList;
        }

        @Override
        public int getCount() {
            return pictureList == null ? 0 : pictureList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.activity_pic_detail_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //加载图片
            Picture picture = pictureList.get(position);
            Glide.with(context).load(pictureBean.getDownloadUrl()).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    //显示图片
                    ivImage.setImageDrawable(resource);
                    //第一次加载的话要放大图片
                    scalePictureToScreenHeight();
                    //隐藏进度条
                    seekBar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    //显示进度条
                    progressBar.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.GONE);
                }
            });

            return view;
        }

        class ViewHolder {

            @Bind(R.id.iv_image)
            ImageView ivImage;

            ViewHolder(View view) {
                ButterKnife.bind(view);
            }
        }
    }
}
