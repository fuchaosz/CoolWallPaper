package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coolwallpaper.BaseActivity;
import com.coolwallpaper.R;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 我的收藏界面
 * Created by fuchao on 2016/6/2.
 */
public class FavouriteFragment extends Fragment {

    private MyBmobUser user;//who's favourite
    private List<MyBmobFavourite> favouriteList;//picture list
    private MyAdapter adapter;//favourite picture gridview's adapter

    @Bind(R.id.pull_grid_view)
    PullToRefreshGridView gridView;

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
        return inflater.inflate(R.layout.fragment_favourite_list, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle data = getArguments();
        this.user = (MyBmobUser) data.getSerializable("USER");
        this.init();
        this.addListener();
    }

    //do some init work
    private void init() {
        //get favourite paper
        queryFavourite();
        adapter = new MyAdapter(getActivity(), null);
        gridView.setAdapter(adapter);
    }

    //add listener
    private void addListener() {
        //add pull up listener
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });
    }

    //get my favourite paper
    private void queryFavourite() {
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("user", user);
        //get all my favourite picture
        query.findObjects(getActivity(), new FindListener<MyBmobFavourite>() {
            @Override
            public void onSuccess(List<MyBmobFavourite> list) {
                favouriteList = list;
                //crate adapter
                adapter = new MyAdapter(getActivity(), favouriteList);
                //set gridview
                gridView.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show("获取收藏的图片失败" + s);

            }
        });
    }

    //refresh after get data
    private void refresh() {

    }

    //show empty view when there is no data
    private void showEmpty(){

    }



    //adapter for list
    private class MyAdapter extends BaseAdapter {

        Context context;
        List<MyBmobFavourite> favouriteList;

        public MyAdapter(Context context, List<MyBmobFavourite> favouriteList) {
            this.context = context;
            this.favouriteList = favouriteList;
        }

        @Override
        public int getCount() {
            return favouriteList == null ? 0 : favouriteList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.local_paper_item_2, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //fetch data
            MyBmobFavourite favourite = favouriteList.get(position);
            //show picture's thumbnail
            Glide.with(context).load(favourite.getPicture().getThumbUrl()).into(holder.ivItem);
            return view;
        }

        class ViewHolder {
            @Bind(R.id.iv_item)
            ImageView ivItem;

            public ViewHolder(View view) {
                ButterKnife.bind(view);
            }
        }
    }
}
