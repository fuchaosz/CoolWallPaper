package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.coolwallpaper.constant.AppBus;
import com.lidroid.xutils.ViewUtils;

import butterknife.ButterKnife;

/**
 * 基本类型Fragment
 * Created by fuchao on 2015/10/30.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //xUtils注解
        ViewUtils.inject(this, view);
        //注册butterKnife
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册otto
        AppBus.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //取消注册otto
        AppBus.getInstance().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
