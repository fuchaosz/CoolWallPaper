package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.coolwallpaper.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页上的Fragment
 * Created by fuchao on 2015/11/17.
 */
public class HomePageFragment extends BaseFragment {

    private String tag;//二级标题
    private String[] subTagArray;//三级标题列表
    private MyPagerAdapter adapter;

    @ViewInject(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewInject(R.id.viewpager_pic)
    ViewPager pager;

    /**
     * 创建Fragment方法
     *
     * @param tag         二级标题，例如：风景
     * @param subTagArray 三级标题列表，例如: 自然风光
     * @return
     */
    public static HomePageFragment newInstance(String tag, String[] subTagArray) {
        HomePageFragment fragemnet = new HomePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", tag);
        bundle.putStringArray("SUB_TAG_ARRAY", subTagArray);
        fragemnet.setArguments(bundle);
        return fragemnet;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //取出参数
        Bundle bundle = getArguments();
        this.tag = bundle.getString("TAG");
        this.subTagArray = bundle.getStringArray("SUB_TAG_ARRAY");
        //初始化
        this.init();
        //添加监听器
        this.addListener();
    }

    //初始化
    private void init() {
        //创建PagerAdapter
        this.adapter = new MyPagerAdapter(getActivity(), tag, subTagArray);
        //设置adapter
        this.pager.setAdapter(adapter);
        this.tabs.setViewPager(pager);
    }

    //添加监听器
    private void addListener() {

    }

    //自定义适配器
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private Context context;
        private List<Fragment> fragmentList = new ArrayList<>();
        private String[] subTagArray;
        private String tag;

        public MyPagerAdapter(Context context, String tag, String[] subTagArray) {
            super(getChildFragmentManager());
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
