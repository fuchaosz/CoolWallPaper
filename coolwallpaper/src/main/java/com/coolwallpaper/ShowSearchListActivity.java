package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolwallpaper.bean.SearchRequestParam;
import com.coolwallpaper.bean.SearchResult;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.PictureParseUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 显示搜索结果
 * Created by fuchao on 2016/4/26.
 */
public class ShowSearchListActivity extends BaseActivity {

    private MyAdapter adapter;
    private String queryStr;
    private OkHttpClient httpClient;
    private List<Picture> pictureList = new ArrayList<>();//搜索结果
    private SearchRequestParam param;//搜索参数
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新搜索数据
            if (msg.what == 0) {
                //更新数据
                List<Picture> tmp = (List<Picture>) msg.obj;
                adapter.setPictureList(tmp);
                adapter.notifyDataSetChanged();
                //停止刷新
                pullListView.onRefreshComplete();
            }
        }
    };

    @Bind(R.id.lv_paper)
    PullToRefreshListView pullListView;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    /**
     * 启动方法
     *
     * @param context
     * @param queryStr
     */
    public static void startActivity(Context context, String queryStr) {
        Intent intent = new Intent(context, ShowSearchListActivity.class);
        intent.putExtra("QUERY_STR", queryStr);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_paper_list);
        this.queryStr = getIntent().getStringExtra("QUERY_STR");
        this.init();
        this.addListener();
    }

    private void init() {
        httpClient = new OkHttpClient();
        adapter = new MyAdapter(this, pictureList);
        tvTitle.setText(queryStr);
        //设置列表
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//禁止下拉
        pullListView.setAdapter(adapter);
        //创建搜索参数
        param = new SearchRequestParam(queryStr);
        param.setLen(20);//设置每页显示20张图片
        param.setStart(0);//默认从0开始
        //搜索
        querySearchResult(param);
    }

    private void addListener() {
        //给列表添加监听器
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下一页
                param.setStart(param.getStart() + param.getLen());
                //搜索结果
                querySearchResult(param);
            }
        });
    }

    @OnClick({R.id.ly_left})
    public void onClick(View v) {
        switch (v.getId()) {
            //标题栏返回按钮
            case R.id.ly_left:
                finish();
                break;
        }
    }

    //访问网络，获取搜索的结果
    private void querySearchResult(SearchRequestParam param) {
        Request request = new Request.Builder().url(param.getUrl()).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "搜索壁纸失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                //解析
                List<SearchResult> searchResultList = PictureParseUtil.parseSearchResult(jsonStr);
                //将搜索结果转换为数据库实体
                pictureList.addAll(ConvertUtil.toPictureList(searchResultList));
                //发送消息，在UI线程中更新列表
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = pictureList;
                handler.sendMessage(msg);
            }
        });
    }

    //适配器
    public class MyAdapter extends BaseAdapter {

        Context context;
        List<Picture> pictureList;

        public MyAdapter(Context context, List<Picture> pictureList) {
            this.context = context;
            this.pictureList = pictureList;
        }

        @Override
        public int getCount() {
            return pictureList == null ? 0 : pictureList.size() / 2;
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
                view = LayoutInflater.from(context).inflate(R.layout.picture_item_4, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Picture picture1 = pictureList.get(position * 2);
            Picture picture2 = pictureList.get(position * 2 + 1);
            Glide.with(context).load(picture1.getDownloadUrl()).placeholder(R.drawable.coolwallpaper_empty).into(holder.ivLeft);
            Glide.with(context).load(picture2.getDownloadUrl()).placeholder(R.drawable.coolwallpaper_empty).into(holder.ivRight);
            //添加监听器
            holder.ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到图片详情
                    ShowPictureDetailActivity.startActivity(getActivity(), position * 2, pictureList);
                }
            });
            holder.ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到图片详情
                    ShowPictureDetailActivity.startActivity(getActivity(), position * 2 + 1, pictureList);
                }
            });
            return view;
        }

        class ViewHolder {
            @Bind(R.id.iv_item_left)
            ImageView ivLeft;
            @Bind(R.id.iv_item_right)
            ImageView ivRight;

            ViewHolder(View v) {
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
