package com.coolwallpaper;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.coolwallpaper.activity.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 显示图片的Activity
 * Created by John on 2015/9/29.
 */
public class ShowPictureActivity extends BaseActivity {

    @ViewInject(R.id.rv_picture)
    RecyclerView rvPictue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
    }
}
