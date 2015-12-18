package com.coolwallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.utils.ImageUtil;
import com.coolwallpaper.utils.PictureParseUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    private int currentPage;
    private List<PictureResult> beanList;

    @ViewInject(R.id.gv_pic)
    PullToRefreshGridView gridView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        //访问网络，获取图片
        this.queryPicture();
    }

    //初始化
    private void init() {
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.options = ImageUtil.getInstance().getDisplayImageOptions();
        this.httpUtils = new HttpUtils();
        this.requetParam = new WallPaperRequetParam();
        this.gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ILoadingLayout loadLayout = gridView.getLoadingLayoutProxy();
        loadLayout.setPullLabel("上拉加载");
        loadLayout.setRefreshingLabel("正在加载...");
        loadLayout.setReleaseLabel("释放加载");
    }

    //添加监听器
    private void addListener() {
        //this.gridView.setOnScrollListener(new AutoLoadListener());
        //添加刷新监听
        this.gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                requetParam.setPn(++currentPage);
                queryPicture();
            }
        });
        //添加item监听
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到图片详情
                PictureResult tmpBean = adapter.getBeanList().get(position);
                ShowPictureDetailActivity.startActivity(ShowPictureListActivity.this, tmpBean, beanList);
            }
        });
    }

    //查询图片
    private void queryPicture() {
        this.httpUtils.send(HttpRequest.HttpMethod.GET, requetParam.getUrl(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String jsonStr = responseInfo.result;
                //解析数据
                beanList = PictureParseUtil.parse(jsonStr);
                showPicture(beanList);
                //关闭对话框
                //if (progressDialog != null && progressDialog.isShowing()) {
                //    progressDialog.dismiss();
                //}
                //停止刷新
                gridView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onStart() {
                //显示进度条
                //progressDialog = ProgressDialog.show(ShowPictureListActivity.this, "正在加载", "请等待....");
                //progressDialog.show();
            }
        });
    }

    //显示图片
    private void showPicture(List<PictureResult> beanList) {
        //若为空则创建adaper
        if (adapter == null) {
            adapter = new PictureGridAdapter(this, beanList);
            gridView.setAdapter(adapter);
        }
        //已经创建了adapter
        else {
            //添加数据
            adapter.getBeanList().addAll(beanList);
            adapter.notifyDataSetChanged();
        }
    }

    //适配器
    private class PictureGridAdapter extends BaseAdapter {

        private List<PictureResult> beanList;
        private Context context;

        //构造函数
        public PictureGridAdapter(Context context, List<PictureResult> beanList) {
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
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            PictureResult bean = beanList.get(position);
            //绑定数据
            imageLoader.displayImage(bean.getThumbUrl(), holder.ivPic, holder);
            holder.tvDesc.setText(bean.getDesc());
            return view;
        }

        class ViewHolder implements ImageLoadingListener {

            public ImageView ivPic;
            public TextView tvDesc;
            public ProgressBar progressBar;

            @Override
            public void onLoadingStarted(String s, View view) {
                this.ivPic.setImageDrawable(null);
                this.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                this.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }

        }

        public void setBeanList(List<PictureResult> beanList) {
            this.beanList = beanList;
        }

        public List<PictureResult> getBeanList() {
            return beanList;
        }
    }

}
