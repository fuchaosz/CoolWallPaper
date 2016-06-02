package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobUser;

import cn.bmob.v3.BmobQuery;

/**
 * 我的收藏界面
 * Created by fuchao on 2016/6/2.
 */
public class FavouriteFragment extends Fragment {

    private MyBmobUser user;
    private

    /**
     * Get fragment instance
     *
     * @param user bmobuser who's favourite
     * @return
     */
    public static FavouriteFragment getInstance(MyBmobUser user) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle data = new Bundle();
        data.putSerializable("USER", user);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle data = getArguments();
        this.user = (MyBmobUser) data.getSerializable("USER");
    }

    private void init() {

    }

    //get my favourite paper
    private void queryFavourite() {
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("user",user);
        query.find
    }
}
