package com.coolwallpaper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.fragment.FavouriteFragment;
import com.coolwallpaper.fragment.LocalPaperFragment;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的收藏
 * Created by fuchao on 2016/5/31.
 */
public class MyFavouritePaper extends BaseActivity implements View.OnClickListener {

    private MyBmobUser user;//登录成功的用户
    private List<MyBmobFavourite> favouritePaperList;//用户收藏的图片
    private MyAdapter adapter;
    private List<Fragment> fragmentList;//本地壁纸和收藏的壁纸
    private String[] titles = new String[]{"本地壁纸", "收藏壁纸"};//viewpager上面的标题

    @Bind(R.id.title_indictor)
    PageIndicator pageIndicator;

    @Bind(R.id.vp)
    ViewPager viewPager;

    /**
     * 启动方法
     *
     * @param context
     * @param user    登录成功后的用户
     */
    public static void startActivity(Context context, MyBmobUser user) {
        Intent intent = new Intent(context, MyFavouritePaper.class);
        intent.putExtra("USER", user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = (MyBmobUser) getIntent().getSerializableExtra("USER");
        setContentView(R.layout.activity_my_favourite);
        this.init();
    }

    //初始化
    private void init() {
        //创建fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(LocalPaperFragment.newInstance());
        fragmentList.add(FavouriteFragment.newInstance(user));
        //创建adaper
        this.adapter = new MyAdapter(fragmentList, titles);
        this.viewPager.setAdapter(adapter);
    }

    @OnClick({R.id.ly_local_paper, R.id.ly_change, R.id.ly_upload})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //本地图片
            case R.id.ly_local_paper:
                //跳转到本地图片页面
                LocalPaperActivity.startActivity(this);
                break;
            //自动刷新
            case R.id.ly_change:
                break;
            //我的上传
            case R.id.ly_upload:
                break;
        }
    }

    //ViewPager的适配器
    private class MyAdapter extends android.support.v13.app.FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titles;

        public MyAdapter(List<Fragment> fragmentList, String[] titles) {
            super(getFragmentManager());
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public android.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

}
