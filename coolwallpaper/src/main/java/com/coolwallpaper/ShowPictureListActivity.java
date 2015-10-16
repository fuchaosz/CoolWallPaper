package com.coolwallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.PictureBean;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.utils.ImageUtil;
import com.coolwallpaper.utils.PictureParseUtil;
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
 * 图片列表
 * Created by fuchao on 2015/10/16.
 */
public class ShowPictureListActivity extends BaseActivity {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private PictureGridAdapter adapter;
    private HttpUtils httpUtils;
    private BaseRequestParam requetParam;
    private ProgressDialog progressDialog;

    @ViewInject(R.id.gv_pic)
    GridView gridViewPic;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShowPictureListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_grid);
        //初始化
        this.init();
        //添加监听器
        this.addListener();
        //访问网络，获取图片
        this.queryPicture();
    }

    //初始化
    private void init() {
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.options = ImageUtil.getDisplayImageOptions();
        this.httpUtils = new HttpUtils();
        this.requetParam = new WallPaperRequetParam();
    }

    //添加监听器
    private void addListener(){
    }

    //查询图片
    private void queryPicture() {
        this.httpUtils.send(HttpRequest.HttpMethod.GET, requetParam.getUrl(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String jsonStr = responseInfo.result;
                //解析数据
                List<PictureBean> list = PictureParseUtil.parse(jsonStr);
                showPicture(list);
                //关闭对话框
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onStart() {
                //显示进度条
                progressDialog = ProgressDialog.show(ShowPictureListActivity.this, "正在加载", "请等待....");
                progressDialog.show();
            }
        });
    }

    //显示图片
    private void showPicture(List<PictureBean> beanList) {
        //若为空则创建adaper
        if (adapter == null) {
            adapter = new PictureGridAdapter(this, beanList);
            gridViewPic.setAdapter(adapter);
        }
        //已经创建了adapter
        else {
            adapter.setBeanList(beanList);
            adapter.notifyDataSetChanged();
        }
    }

    //适配器
    private class PictureGridAdapter extends BaseAdapter {

        private List<PictureBean> beanList;
        private Context context;

        //构造函数
        public PictureGridAdapter(Context context, List<PictureBean> beanList) {
            this.context = context;
            this.beanList = beanList;
        }

        @Override
        public int getCount() {
            if (beanList == null) {
                return 0;
            }
            return beanList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.picture_item, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                holder.tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            PictureBean bean = beanList.get(position);
            //绑定数据
            imageLoader.displayImage(bean.getSmallImageUrl(), holder.ivPic, options);
            holder.tvDesc.setText(bean.getDesc());
            return view;
        }

        class ViewHolder {
            public ImageView ivPic;
            public TextView tvDesc;
        }

        public void setBeanList(List<PictureBean> beanList) {
            this.beanList = beanList;
        }
    }

}
