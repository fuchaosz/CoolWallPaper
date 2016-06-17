package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

    private static final int PAGE_SIZE = 10;//每页条数
    private static final int ACTION_FIRST_LOAD = 1;//第一次加载
    private static final int ACTION_LOAD_MORE = 2;//加载更多
    private MyBmobUser user;//who's favourite
    private List<MyBmobFavourite> favouriteList;//picture list
    private MyAdapter adapter;//favourite picture gridview's adapter
    private int currentPage;//当前页数

    @Bind(R.id.pull_grid_view)
    PullToRefreshGridView gridView;

    //空白界面
    @Bind(R.id.ly_empty)
    View lyEmpty;

    //错误界面
    @Bind(R.id.ly_error)
    View lyError;

    /**
     * Get fragment instance
     *
     * @param user bmobuser who's favourite
     * @return
     */
    public static FavouriteFragment newInstance(MyBmobUser user) {
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
        //创建适配器
        adapter = new MyAdapter(getActivity(), null);
        gridView.setAdapter(adapter);
        //刷新
        refresh();
    }

    //add listener
    private void addListener() {
        //add pull up listener
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                //刷新数据
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                //加载下一页
                currentPage++;
                queryFavourite(currentPage, ACTION_LOAD_MORE);
            }
        });
    }

    //刷新页面
    private void refresh() {
        currentPage = 0;
        //查询我的收藏
        queryFavourite(0, ACTION_FIRST_LOAD);
    }


    //get my favourite paper
    private void queryFavourite(int page, int action) {
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("user", user);
        //设置每页显示的数据条数
        query.setLimit(PAGE_SIZE);
        //设置页数
        query.setSkip(page * PAGE_SIZE);
        //get all my favourite picture
        query.findObjects(getActivity(), new FindListener<MyBmobFavourite>() {
            @Override
            public void onSuccess(List<MyBmobFavourite> list) {
                favouriteList = list;
                //数据为空
                if (list == null && list.size() == 0) {
                    //如果是第一次加载，则表示没有数据
                    if (action == ACTION_FIRST_LOAD) {
                        //显示空白界面
                        showEmpty();
                    }
                    //如果是加载更多，则表示已经全部加载完了
                    else if (action == ACTION_LOAD_MORE) {
                        ToastUtil.show("数据全部加载完毕了");
                        currentPage--;
                    }
                }
                //数据不为空
                else {
                    //显示界面
                    showContent();
                    //更新界面
                    adapter.setFavouriteList(favouriteList);
                    adapter.notifyDataSetChanged();
                }
                //停止刷新
                gridView.onRefreshComplete();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show("获取收藏的图片失败" + s);
                //显示错误界面
                showError();
            }
        });
    }

    //显示列表界面
    private void showContent() {
        gridView.setVisibility(View.VISIBLE);
        lyEmpty.setVisibility(View.GONE);
        lyError.setVisibility(View.GONE);
    }

    //show empty view when there is no data
    private void showEmpty() {
        gridView.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.VISIBLE);
        lyError.setVisibility(View.GONE);
    }

    //显示错误界面
    private void showError() {
        gridView.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.GONE);
        lyError.setVisibility(View.VISIBLE);
    }


    //adapter for list
    public static class MyAdapter extends BaseAdapter {

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
            Glide.with(context).load(favourite.getThumbUrl()).into(holder.ivItem);
            return view;
        }

        public void setFavouriteList(List<MyBmobFavourite> favouriteList) {
            this.favouriteList = favouriteList;
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
