package com.coolwallpaper;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;

/**
 * 启动页面
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        HomePageActivity.startActivity(this);
        finish();
    }
}
