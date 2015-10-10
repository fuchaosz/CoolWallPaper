package com.coolwallpaper.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类Activity
 * Created by fuchao on 2015/7/9.
 */
public class BaseActivity extends Activity {

    protected List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
        //注册xUtils的view注解
        ViewUtils.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }
}
