package com.coolwallpaper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.event.UpdatePictureEvent;
import com.coolwallpaper.fragment.PictureDetailFragment;
import com.coolwallpaper.fragment.PictureListFragment;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.FileUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    private Picture pictureBean;
    private int position;
    private Matrix matrix;
    private float maxMoveLength;//最大可以移动的距离
    private int currentProgress = 50;//当前进度
    private PictureListFragment fragment;//左边的图片的列表
    private List<Picture> beanList;//图片列表
    private Drawable favoriteAddDrawable;//没有收藏的时候显示的drawable
    private Drawable favoriteRemoveDrawable;//收藏了之后显示的drawable
    private MyAdapter adapter;

    //浮动层菜单
    @ViewInject(R.id.ly_picture_detail_menu)
    View lyPictureDetailMenu;

    @ViewInject(R.id.sb_seekbar)
    SeekBar seekBar;

    //左边的Fragment
    @ViewInject(R.id.fl_left_container)
    View flContainer;

    //收藏图片
    @ViewInject(R.id.iv_favorite)
    ImageView ivFavorite;

    //详情中可以互动的viewpager
    @Bind(R.id.viewpager)
    ViewPager viewPager;

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
        viewPager.setAdapter(adapter);
        //设置选中的页数
        viewPager.setCurrentItem(position);
    }

    //添加监听器
    private void addListener() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.ly_picture_detail_menu).setOnClickListener(this);
        findViewById(R.id.ly_similar_pic).setOnClickListener(this);
        findViewById(R.id.ly_title).setOnClickListener(this);
        findViewById(R.id.ly_favorite_pic).setOnClickListener(this);
        //进度条
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //移动图片
                PictureDetailFragment fragment = adapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null) {
                    fragment.movePicture(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //给viewpgaer添加点击监听
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果浮层不可见
                if (lyPictureDetailMenu.getVisibility() != View.VISIBLE) {
                    //显示浮层
                    showMaskMenu();
                }
            }
        });
    }

    //查询下载量和收藏量
    private void queryMyBmobPicture() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        this.beanList = event.getBeanList();
        this.position = event.getPosition();
        //重新设置adapter
        adapter = new MyAdapter(this, beanList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    //订阅下载文件成功的事件
    @Subscribe
    public void downloadSuccessEvent(DownloadPictureSuccessEvent event) {
        Toast.makeText(this, "图片成功收藏到本地" + event.getSavePath(), Toast.LENGTH_SHORT).show();
    }

    //订阅下载文件失败的事件
    @Subscribe
    public void downloadFailuerEvent(DownloadPictureFailureEvent event) {
        Toast.makeText(this, "图片 " + event.getPictureBean().getDesc() + " 收藏失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
        }
    }

    //适配器
    class MyAdapter extends FragmentPagerAdapter {

        Context context;
        List<PictureDetailFragment> fragmentList = new ArrayList<>();
        List<Picture> pictureList;

        public MyAdapter(Context context, List<Picture> pictureList) {
            super(getFragmentManager());
            this.context = context;
            this.pictureList = pictureList;
            //创建Fragment
            if (pictureList != null) {
                for (Picture p : pictureList) {
                    fragmentList.add(PictureDetailFragment.newInstance(p));
                }
            }
        }

        @Override
        public int getCount() {
            return pictureList == null ? 0 : pictureList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        //获取指定的fragment
        public PictureDetailFragment getFragment(int position) {
            PictureDetailFragment fragment = null;
            if (fragmentList != null && position < fragmentList.size()) {
                fragment = fragmentList.get(position);
            }
            return fragment;
        }
    }

    //显示seekBar
    public void showSeekBar() {
        seekBar.setVisibility(View.VISIBLE);
    }

    //隐藏seekBar
    public void hideSeekBar() {
        seekBar.setVisibility(View.GONE);
    }

    //显示浮层
    public void showMaskMenu() {
        //弹出浮动层菜单
        lyPictureDetailMenu.setVisibility(View.VISIBLE);
        Toast.makeText(this, "点击了图片", Toast.LENGTH_SHORT).show();
    }
}
