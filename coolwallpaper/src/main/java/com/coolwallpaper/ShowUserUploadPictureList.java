package com.coolwallpaper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobUpload;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.NetworkUtil;
import com.coolwallpaper.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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

    private List<Picture> picList;//用户上传的图片列表

    //无网络界面
    @Bind(R.id.ly_no_internet)
    View lyNoInternet;

    //空白界面
    @Bind(R.id.ly_empty)
    View lyEmpty;

    @Bind(R.id.lv_paper)
    PullToRefreshListView lvPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_upload_list);
        this.init();
        this.addListener();
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

    }

    private void addListener() {

    }

    //查询用户上传的图片
    private void queryUserUpload() {
        BmobQuery<MyBmobUpload> query = BmobUtil.getMyUploadQuery();
        query.findObjects(this, new FindListener<MyBmobUpload>() {
            @Override
            public void onSuccess(List<MyBmobUpload> list) {
                //如果为空，表示没有数据
                if (EmptyUtil.isEmpty(list)) {
                    //显示空白界面
                    showEmptyView();
                }
                //有数据
                else {
                    //将Bmob数据转换为JavaBean
                    picList = ConvertUtil.toPictures(list);
                    //显示列表
                    showListView();
                    //
                }
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

    @OnClick({})
    @Override
    public void onClick(View v) {

    }

    private class MyAdapter extends BaseAdapter {

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
                view = LayoutInflater.from(context).inflate(R.layout.picture_item_3, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //取出数据
            Picture picture = pictureList.get(position);

            return view;
        }

        class ViewHolder {
            @Bind(R.id.iv_item_left)
            ImageView ivLeft;
            @Bind(R.id.iv_item_right)
            ImageView ivRight;

            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }
    }
}
