package com.coolwallpaper.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.coolwallpaper.R;
import com.coolwallpaper.fragment.ShowPictureListFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页，侧滑菜单采用SlidingMenu
 * Created by fuchao on 2015/8/11.
 */
public class HomePageActivity2 extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "[HomePageActivity2]";
    private MyPagerAdapter adapter;
    private SlidingMenu slidingMenu;//侧滑菜单采用SlidingMenu
    private ResideMenu resideMenu;

    @ViewInject(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewInject(R.id.viewpager_pic)
    ViewPager viewPager;

    //启动方法
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomePageActivity2.class);
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
        //        //配置SlidingMenu
        //        this.slidingMenu = new SlidingMenu(this);
        //        this.slidingMenu.setMode(SlidingMenu.LEFT);
        //        //设置触摸模式，可以选择全屏划出，或者是边缘划出，或者是不可划出
        //        this.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //        this.slidingMenu.setShadowWidth(0);
        //        this.slidingMenu.setShadowDrawable(R.drawable.coolwallpaper_main_bg);
        //        //设置侧滑栏完全展开之后，距离另外一边的距离，单位px，设置的越大，侧滑栏的宽度越小
        //        this.slidingMenu.setBehindOffset(300);
        //        //设置渐变的程度，范围是0-1.0f,设置的越大，则在侧滑栏刚划出的时候，颜色就越暗。1.0f的时候，颜色为全黑
        //        this.slidingMenu.setFadeDegree(0.3f);
        //        this.slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //        this.slidingMenu.setMenu(R.layout.menu_main_left);
        //创建二级标题
        String[] subTagArray = {"雪景", "山水", "田园", "公路", "海底", "宇宙", "夜景", "秋天", "日出", "沙漠", "星空", "自然", "海滩"};
        this.adapter = new MyPagerAdapter(this, "风景", subTagArray);
        this.viewPager.setAdapter(adapter);
        this.tabs.setViewPager(viewPager);
    }

    //添加监听器
    private void addListener() {
        //给viewpager添加滑动监听器
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(TAG, "position = " + position + "  positionOffset = " + positionOffset + "  positionOffsetPixels = " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //滑动到最左边
                if (position == 0) {
                }
                //滑动到最右边
                else if (position == adapter.getCount() - 1) {
                }
                //其他位置
                else {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "state = " + state);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    //定义Adapter
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
