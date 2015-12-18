package com.coolwallpaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolwallpaper.R;
import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.constant.TestURL;
import com.coolwallpaper.event.DownloadPictureResultSuccessEvent;
import com.coolwallpaper.model.Param;
import com.coolwallpaper.model.ParamDao;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.model.PictureDao;
import com.coolwallpaper.service.PictureResultGetServevice;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.ImageUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.otto.Subscribe;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 最底层的单个Fragement，只显示图片列表
 * Created by fuchao on 2015/11/18.
 */
public class ShowPictureListFragment extends BaseFragment {

    private String TAG = "[ShowPictureListFragment]";
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private PictureGridAdapter adapter;
    private ProgressDialog progressDialog;
    private String title1;
    private String title2;
    private List<Picture> pictureList;

    @ViewInject(R.id.gv_pic)
    PullToRefreshGridView gridView;

    /**
     * 创建实例方法
     *
     * @param title1 一级标题,例如:风景
     * @param title2 二级标题,例如:雪景
     * @return
     */
    public static ShowPictureListFragment newInstance(String title1, String title2) {
        ShowPictureListFragment fragment = new ShowPictureListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE_1", title1);
        bundle.putString("TITLE_2", title2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pic_grid, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        this.title1 = bundle.getString("TITLE_1");
        this.title2 = bundle.getString("TITLE_2");
        //初始化
        this.init();
        //添加监听器
        this.addListener();
        //测试代码
        //this.gridView.setAdapter(new TestAdapter());
    }

    //初始化
    private void init() {
        this.imageLoader.init(ImageUtil.getInstance().getImageLoaderConfiguration());
        this.options = ImageUtil.getInstance().getDisplayImageOptions();
        //this.gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        this.gridView.setMode(PullToRefreshBase.Mode.DISABLED);
        ILoadingLayout loadLayout = gridView.getLoadingLayoutProxy();
        loadLayout.setPullLabel("上拉加载");
        loadLayout.setRefreshingLabel("正在加载...");
        loadLayout.setReleaseLabel("释放加载");
        //查询数据库数据
        pictureList = this.queryDB();
        //创建适配器
        this.adapter = new PictureGridAdapter(getActivity(), pictureList);
        this.gridView.setAdapter(adapter);
        //如果查询出来的数据为空则启动服务
        if (pictureList == null || pictureList.size() == 0) {
            //构造网络参数
            BaseRequestParam requestParam = new WallPaperRequetParam();
            requestParam.setTitle1(title1);
            requestParam.setTitle2(title2);
            //启动下载图片列表的服务
            PictureResultGetServevice.startService(getActivity(), requestParam);
        }
    }

    //从数据库中查询图片
    private List<Picture> queryDB() {
        List<Picture> result = null;
        //查询参数
        ParamDao paramDao = DBUtil.getInstance().getParamDao();
        QueryBuilder qb = paramDao.queryBuilder();
        qb.where(ParamDao.Properties.Title1.eq(title1), ParamDao.Properties.Title2.eq(title2));
        List paramList = qb.list();
        //如果没有查到参数，说明这个参数还没有查询过
        if (paramList == null || paramList.size() == 0) {
            return null;
        }
        //取出第一个param
        Param param = (Param) paramList.get(0);
        //接着根据Param查图片
        PictureDao pictureDao = DBUtil.getInstance().getPictureDao();
        qb = pictureDao.queryBuilder();
        qb.where(PictureDao.Properties.ParamId.eq(param.getId()));
        result = qb.list();
        return result;
    }

    //添加监听器
    private void addListener() {
        //this.gridView.setOnScrollListener(new AutoLoadListener());
        //        //添加刷新监听
        //        this.gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
        //            @Override
        //            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
        //                requetParam.setPn(++currentPage);
        //                queryPicture();
        //            }
        //        });
        //        //添加item监听
        //        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                // 跳转到图片详情
        //                PictureBean tmpBean = adapter.getBeanList().get(position);
        //                ShowPictureDetailActivity.startActivity(getActivity(), tmpBean, beanList);
        //            }
        //        });
    }

    //显示图片
    private void showPicture(List<Picture> beanList) {
        //若为空则创建adaper
        if (adapter == null) {
            adapter = new PictureGridAdapter(getActivity(), beanList);
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

        private List<Picture> beanList;
        private Context context;

        //构造函数
        public PictureGridAdapter(Context context, List<Picture> beanList) {
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
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            Picture bean = beanList.get(position);
            //绑定数据
            imageLoader.displayImage(bean.getThumbUrl(), holder.ivPic, options);
            holder.tvDesc.setText(bean.getDesc());
            return view;
        }

        class ViewHolder implements ImageLoadingListener {

            public ImageView ivPic;
            public TextView tvDesc;
            public ProgressBar progressBar;

            public ViewHolder(View view) {
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
                //默认隐藏ProgressBar
                progressBar.setVisibility(View.GONE);
            }

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

        public List<Picture> getBeanList() {
            return beanList;
        }

        public void setBeanList(List<Picture> beanList) {
            this.beanList = beanList;
        }
    }

    //otto接收事件
    @Subscribe
    public void onDownloadSuccess(DownloadPictureResultSuccessEvent event) {
        pictureList = event.getPictureList();
        adapter.setBeanList(pictureList);
        adapter.notifyDataSetChanged();
    }

    //测试用适配器
    private class TestAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return TestURL.urls.length;
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.picture_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            //加载图片
            String url = TestURL.urls[position];
            imageLoader.displayImage(url, holder.ivPic, options);
            return view;
        }

        private class ViewHolder {

            public ImageView ivPic;
            public TextView tvDesc;
            public ProgressBar progressBar;

            public ViewHolder(View view) {
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
                //默认隐藏ProgressBar
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
