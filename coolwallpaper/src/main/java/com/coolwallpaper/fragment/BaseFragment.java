package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.coolwallpaper.constant.AppBus;
import com.lidroid.xutils.ViewUtils;

/**
 * 基本类型Fragment
 * Created by fuchao on 2015/10/30.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册otto
        AppBus.getInstance().register(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //xUtils注解
        ViewUtils.inject(this, view);
    }

    @Override
    public void onDestroy() {
        //取消注册EevntBus
        AppBus.getInstance().unregister(this);
        super.onDestroy();
    }
}
