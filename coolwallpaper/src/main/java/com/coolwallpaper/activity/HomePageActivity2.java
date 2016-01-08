package com.coolwallpaper.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.coolwallpaper.R;
import com.coolwallpaper.subpage.SubPageFragment;

import butterknife.Bind;

/**
 * 首页
 * Created by fuchao on 2015/8/11.
 */
public class HomePageActivity2 extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.fl_content)
    View flContent;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomePageActivity2.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_2);
        this.init();
        this.addListener();
    }

    //初始化
    private void init() {
        //添加一个默认的Fragment
        FragmentManager fm = getFragmentManager();
        SubPageFragment fragment = SubPageFragment.newInstance("风景");
        fm.beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    //添加监听器
    private void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
