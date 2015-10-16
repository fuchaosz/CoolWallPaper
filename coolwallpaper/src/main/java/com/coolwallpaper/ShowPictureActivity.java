package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.PictureBean;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.utils.PictureParseUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * 显示图片的Activity
 * Created by fuchao on 2015/9/29.
 */
public class ShowPictureActivity extends BaseActivity {

    private HttpUtils httpUtils;//访问网络的工具
    private BitmapUtils bitmapUtils;//显示图片的工具
    private WallPaperRequetParam requetParam;
    private PictureListAdapter adapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();//图片处理
    private DisplayImageOptions options;//加载图片的选项

    //图片列表
    @ViewInject(R.id.rv_picture)
    RecyclerView rvPictue;

    //启动方法
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShowPictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        this.requetParam = new WallPaperRequetParam();
        this.bitmapUtils = new BitmapUtils(this);
        this.httpUtils = new HttpUtils();
        //初始化
        this.init();
        //查询图片
        this.queryPicture();
    }

    //初始化
    private void init() {
        //给RecycleView设置LayoutManager，否则会报错
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rvPictue.setLayoutManager(linearLayoutManager);
        this.rvPictue.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //创建图片加载工具
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(R.drawable.lms_spinner);
        builder.showImageOnFail(R.drawable.lms_spinner);
        builder.showImageOnLoading(R.drawable.lms_spinner);
        options = builder.build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }


    //访问网络,查询图片
    private void queryPicture() {
        httpUtils.send(HttpRequest.HttpMethod.GET, requetParam.getUrl(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String jsonStr = responseInfo.result;
                //解析数据
                List<PictureBean> beanList = PictureParseUtil.parse(jsonStr);
                //显示数据
                showPicture(beanList);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //显示图片
    private void showPicture(List<PictureBean> beanList) {
        //若为空则创建adaper
        if (adapter == null) {
            adapter = new PictureListAdapter(beanList);
            rvPictue.setAdapter(adapter);
        }
        //已经创建了adapter
        else {
            adapter.setBeanList(beanList);
            adapter.notifyDataSetChanged();
        }
    }

    //图片的适配器
    public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.ViewHolder> {

        private List<PictureBean> beanList;

        /**
         * 构造函数
         *
         * @param beanList 图片列表
         */
        public PictureListAdapter(List<PictureBean> beanList) {
            this.beanList = beanList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PictureBean bean = beanList.get(position);
            imageLoader.displayImage(bean.getSmallImageUrl(), holder.ivPic, options);
            holder.tvDesc.setText(bean.getDesc());
        }

        @Override
        public int getItemCount() {
            return beanList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView ivPic;//显示图片的view
            public TextView tvDesc;//图片描述

            public ViewHolder(View view) {
                super(view);
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            }
        }

        public void setBeanList(List<PictureBean> beanList) {
            this.beanList = beanList;
        }
    }
}
