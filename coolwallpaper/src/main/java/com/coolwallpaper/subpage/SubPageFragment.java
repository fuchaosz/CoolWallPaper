package com.coolwallpaper.subpage;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.coolwallpaper.R;
import com.coolwallpaper.fragment.BaseFragment;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 二级主页,点击menu上面的按钮跳转
 * Created by fuchao on 2016/1/6.
 */
public class SubPageFragment extends BaseFragment implements View.OnClickListener {

    private String title1;//一级标题
    private String[] subTitles;//二级标题
    private PaperViewPagerAdapter adapter;

    @Bind(R.id.tb_indicator)
    TitlePageIndicator indicator;

    @Bind(R.id.vp_paper)
    ViewPager viewPager;

    @Bind(R.id.ly_title)
    View lyTitle;

    /**
     * 创建实例的方法
     *
     * @param title1 一级标题
     * @return
     */
    public static SubPageFragment newInstance(String title1) {
        SubPageFragment fragment = new SubPageFragment();
        Bundle data = new Bundle();
        data.putString("titl1", title1);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_page, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.title1 = getArguments().getString("titl1");
        this.init();
        this.addListener();
    }

    //初始化
    private void init() {
        //测试用数据
        subTitles = new String[]{"雪景", "山水", "田园", "公路", "海底", "宇宙", "夜景", "秋天", "日出", "沙漠", "星空", "自然", "海滩"};
        adapter = new PaperViewPagerAdapter(getActivity(), title1, subTitles);
        viewPager.setAdapter(adapter);
        //设置指示器
        indicator.setViewPager(viewPager);
    }

    //添加监听器
    private void addListener() {
        //给列表添加滑动监听器
        this.viewPager.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //隐藏标题栏
    private void hideTitleBar() {
        Animation animation = new TranslateAnimation(0, 0, 0, -lyTitle.getWidth());
        animation.setFillAfter(true);
        lyTitle.startAnimation(animation);
    }

    //viewpager适配器
    class PaperViewPagerAdapter extends FragmentStatePagerAdapter {

        private String title;
        private String[] subTitles;
        private List<Fragment> fragmentList = new ArrayList<>();

        public PaperViewPagerAdapter(Context context, String title, String[] subTitles) {
            super(getChildFragmentManager());
            this.title = title;
            this.subTitles = subTitles;
            for (int i = 0; i < subTitles.length; i++) {
                Fragment fg = PaperListFragment.newInstance(title, subTitles[i]);
                fragmentList.add(fg);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return subTitles[position];
        }
    }
}
