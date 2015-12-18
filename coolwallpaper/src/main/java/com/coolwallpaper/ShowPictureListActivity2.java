package com.coolwallpaper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.coolwallpaper.fragment.ShowPictureListFragment;

/**
 * 测试
 * Created by fuchao on 2015/12/18.
 */
public class ShowPictureListActivity2 extends Activity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShowPictureListActivity2.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic_2);
        Fragment fragment = ShowPictureListFragment.newInstance("风景", "雪景");
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fl_content, fragment).commit();
    }
}
