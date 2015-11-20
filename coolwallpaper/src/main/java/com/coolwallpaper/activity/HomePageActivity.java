package com.coolwallpaper.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.coolwallpaper.R;
import com.coolwallpaper.fragment.ShowPictureListFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by fuchao on 2015/8/11.
 */
public class HomePageActivity extends BaseActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private Fragment fragment;
    private MyPagerAdapter adapter;

    @ViewInject(R.id.ly_container)
    View lyContainer;

    @ViewInject(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewInject(R.id.viewpager_pic)
    ViewPager viewPager;

    //启动方法
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomePageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home_page);
        //初始化
        this.init();
        //添加监听器
        this.addListener();
    }

    //初始化
    private void init() {
        //创建ResidMenu
        this.resideMenu = new ResideMenu(this);
        this.resideMenu.setBackground(R.drawable.coolwallpaper_main_bg);
        this.resideMenu.attachToActivity(this);
        this.resideMenu.setScaleValue(0.5f);
        //关闭左滑右滑开关
        this.resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        this.resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        //创建MenuItem
        this.createResideMenu();
        //创建二级标题
        String[] subTagArray = {"美轮美奂", "花草植物", "国外风光", "唯美意境", "旅游风光", "海底世界", "冰天雪地", "山水相映", "海滩沙滩", "在路上", "自然风光", "沙漠戈壁", "璀璨星空"};
        //        //创建Fragment
        //        this.fragment = HomePageFragment.newInstance("风景", subTagArray);
        //        //添加到container
        //        FragmentManager fm = getFragmentManager();
        //        FragmentTransaction ft = fm.beginTransaction();
        //        ft.add(R.id.ly_container, fragment);
        //        ft.commit();
        this.adapter = new MyPagerAdapter(this, "风景", subTagArray);
        this.viewPager.setAdapter(adapter);
        this.tabs.setViewPager(viewPager);
    }

    //创建侧滑菜单
    private void createResideMenu() {
        //创建左边的菜单
        String[] titles = {"我的壁纸", "本机相册", "我要供图", "检查升级", "更多设置"};
        int[] icons = {R.drawable.icon_download, R.drawable.icon_pic, R.drawable.icon_pen, R.drawable.icon_upload, R.drawable.icon_moreset};
        for (int i = 0; i < titles.length; i++) {
            ResideMenuItem menuItem = new ResideMenuItem(this, icons[i], titles[i]);
            this.resideMenu.addMenuItem(menuItem, ResideMenu.DIRECTION_LEFT);
        }
        //创建右边的菜单cmd
        String[] titlesRight = {"首页", "热门", "风景", "美女", "明星", "创意", "名车", "影视", "游戏", "动漫"};
        int[] iconsRight = {R.drawable.icon_home, R.drawable.icon_hot, R.drawable.icon_scenery, R.drawable.icon_girl, R.drawable.icon_star, R.drawable.icon_idea, R.drawable.icon_motorbike, R.drawable.icon_movie, R.drawable.icon_game, R.drawable.icon_anime};
        for (int i = 0; i < titlesRight.length; i++) {
            ResideMenuItem menuItem = new ResideMenuItem(this, iconsRight[i], titlesRight[i]);
            menuItem.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText("hello");
            menuItem.addView(tv);
            this.resideMenu.addMenuItem(menuItem, ResideMenu.DIRECTION_RIGHT);
        }
    }

    //添加监听器
    private void addListener() {
    }

    //    @Override
    //    public boolean dispatchTouchEvent(MotionEvent ev) {
    //        //使用滑动开启/关闭菜单
    //        return resideMenu.dispatchTouchEvent(ev);
    //    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    //定义Adapter
    //private class MyPagerAdapter extends FragmentStatePagerAdapter {
    private class MyPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {

        private Context context;
        private List<Fragment> fragmentList = new ArrayList<>();
        private String[] subTagArray;
        private String tag;

        public MyPagerAdapter(Context context, String tag, String[] subTagArray) {
            super(getFragmentManager());
            this.context = context;
            this.tag = tag;
            this.subTagArray = subTagArray;
            if (subTagArray != null) {
                for (int i = 0; i < subTagArray.length; i++) {
                    Fragment fragment = ShowPictureListFragment.newInstance(tag, subTagArray[i]);
                    fragmentList.add(fragment);
                }
            }
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return subTagArray[position];
        }
    }

}
