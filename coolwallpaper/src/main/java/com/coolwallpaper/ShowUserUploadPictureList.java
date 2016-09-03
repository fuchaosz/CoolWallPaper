package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobUpload;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.NetworkUtil;
import com.coolwallpaper.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 显示用户上传的图片
 * Created by fuchao on 16-8-31.
 */
public class ShowUserUploadPictureList extends BaseActivity implements View.OnClickListener {

    private int PAGE_SIZE = 2;
    private int currentPage = 1;
    private List<Picture> picList;//用户上传的图片列表
    private MyAdapter adapter;//图片列表适配器


    //无网络界面
    @Bind(R.id.ly_no_internet)
    View lyNoInternet;

    //空白界面
    @Bind(R.id.ly_empty)
    View lyEmpty;

    @Bind(R.id.lv_paper)
    PullToRefreshGridView lvPaper;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShowUserUploadPictureList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_upload_list);
        this.init();
        this.addListener();
        //查询数据
        queryUserUpload(currentPage);
    }

    private void init() {
        //判断又有没有网络
        if (!NetworkUtil.isNetworkAvailable()) {
            //显示无网络界面
            lyNoInternet.setVisibility(View.VISIBLE);
            //隐藏内容界面
            lyEmpty.setVisibility(View.GONE);
            lvPaper.setVisibility(View.GONE);
            return;
        }
        //有网络
        else {
            lyNoInternet.setVisibility(View.GONE);
            lyEmpty.setVisibility(View.GONE);
            lvPaper.setVisibility(View.VISIBLE);
        }
        //创建适配器
        adapter = new MyAdapter(this, picList);
        lvPaper.setAdapter(adapter);
        //允许上拉，但不允许下拉
        lvPaper.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void addListener() {
        //下拉刷新，上拉加载
//        lvPaper.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //加载更多
//                queryUserUpload(currentPage);
//            }
//        });
    }

    //查询用户上传的图片
    private void queryUserUpload(int page) {
        BmobQuery<MyBmobUpload> query = BmobUtil.getMyUploadQuery();
        query.setLimit(PAGE_SIZE);
        query.setSkip((page - 1) * PAGE_SIZE);
        query.findObjects(this, new FindListener<MyBmobUpload>() {
            @Override
            public void onSuccess(List<MyBmobUpload> list) {
                //如果为空
                if (EmptyUtil.isEmpty(list)) {
                    //如果是第一页，则表示没有任何数据
                    if (currentPage == 1) {
                        //显示空白界面
                        showEmptyView();
                    }
                    //不是第一页，说明数据都加载完了
                    else {
                        //不能再上拉了
                        lvPaper.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
                //有数据
                else {
                    //将Bmob数据转换为JavaBean
                    picList = ConvertUtil.toPictures(list);
                    //显示列表
                    showListView();
                    //更新数据
                    adapter.setPictureList(picList);
                    adapter.notifyDataSetChanged();
                    //页数向前加1
                    currentPage++;
                }
                //停止刷新操作
                lvPaper.onRefreshComplete();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show("查询用户上传图片失败，请检查你的网络!");
            }
        });
    }

    //显示空白界面
    private void showEmptyView() {
        lyNoInternet.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.VISIBLE);
        lvPaper.setVisibility(View.GONE);
    }

    //显示无网络界面
    private void showNoInternet() {
        lyNoInternet.setVisibility(View.VISIBLE);
        lyEmpty.setVisibility(View.GONE);
        lvPaper.setVisibility(View.GONE);
    }

    //显示列表界面
    private void showListView() {
        lyNoInternet.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.GONE);
        lvPaper.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ly_menu_left})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //向左的箭头
            case R.id.ly_menu_left:
                finish();
                break;
        }
    }

    public class MyAdapter extends BaseAdapter {

        List<Picture> pictureList;
        Context context;

        public MyAdapter(Context context, List<Picture> pictureList) {
            this.context = context;
            this.pictureList = pictureList;
        }

        @Override
        public int getCount() {
            return pictureList == null ? 0 : pictureList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.picture_item_2, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //取出数据
            Picture picture = pictureList.get(position);
            //显示图片
            Glide.with(context).load(picture.getDownloadUrl()).into(holder.ivItem);
            //添加图片点击监听器
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到图片详情页面
                    ShowPictureDetailActivity.startActivity(context, position, picList);
                }
            });
            return view;
        }

        class ViewHolder {
            @Bind(R.id.iv_item)
            ImageView ivItem;

            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }

        public List<Picture> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<Picture> pictureList) {
            this.pictureList = pictureList;
        }
    }
}
