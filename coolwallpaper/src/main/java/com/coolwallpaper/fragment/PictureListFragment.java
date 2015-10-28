package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.coolwallpaper.R;

/**
 * 显示图片列表的fragment
 * Created by fuchao on 2015/10/28.
 */
public class PictureListFragment extends Fragment {

    private ListView lvPicture;

    //创建方法


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_list, container, false);
        lvPicture = (ListView) view.findViewById(R.id.lv_pic);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
