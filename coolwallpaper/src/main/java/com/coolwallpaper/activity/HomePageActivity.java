package com.coolwallpaper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;

import com.coolwallpaper.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * 首页
 * Created by fuchao on 2015/8/11.
 */
public class HomePageActivity extends BaseActivity {

    public static final String[] titles = {"推荐", "最热", "最新", "高清", "热搜", "美女"};
    private ResideMenu resideMenu;

    //启动方法
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomePageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.setContentView(R.layout.activity_home_page);
        //初始化
        this.init();
    }

    //初始化
    private void init() {
        //创建ResidMenu
        this.resideMenu = new ResideMenu(this);
        this.resideMenu.setBackground(R.drawable.coolwallpaper_main_bg);
        this.resideMenu.attachToActivity(this);
        //创建MenuItem
        String[] titles = {"title1", "title2", "title3", "title4", "title5", "title6"};
        for (int i = 0; i < titles.length; i++) {
            ResideMenuItem menuItem = new ResideMenuItem(this, R.drawable.icon_order_like, titles[i]);
            resideMenu.addMenuItem(menuItem, ResideMenu.DIRECTION_LEFT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //使用滑动开启/关闭菜单
        return resideMenu.dispatchTouchEvent(ev);
    }

}
