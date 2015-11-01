package com.coolwallpaper.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.coolwallpaper.R;
import com.special.ResideMenu.ResideMenu;

/**
 * 首页
 * Created by fuchao on 2015/8/11.
 */
public class HomePageActivity extends BaseActivity {

    public static final String[] titles = {"推荐", "最热", "最新", "高清", "热搜", "美女"};
    private ResideMenu resideMenu;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_home_page);

    }

}
