package com.coolwallpaper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coolwallpaper.R;
import com.coolwallpaper.ShowPictureDetailActivity;
import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.constant.AppBus;
import com.coolwallpaper.event.DownloadPictureResultSuccessEvent;
import com.coolwallpaper.event.LoadingFinishEvent;
import com.coolwallpaper.model.Param;
import com.coolwallpaper.model.ParamDao;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.model.PictureDao;
import com.coolwallpaper.service.PictureResultGetServevice;
import com.coolwallpaper.utils.DBUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * SubPageFragment显示壁纸列表
 * Created by fuchao on 2016/1/6.
 */
public class PaperListFragment extends BaseFragment implements View.OnClickListener {

    private PictureGridAdapter adapter;
    private String title1;
    private String title2;
    private List<Picture> pictureList;
    private BaseRequestParam requestParam;//访问网络的参数

    @Bind(R.id.lv_paper)
    PullToRefreshListView listView;

    /**
     * 创建实例方法
     *
     * @param title1 一级标题,例如:风景
     * @param title2 二级标题,例如:雪景
     * @return
     */
    public static PaperListFragment newInstance(String title1, String title2) {
        PaperListFragment fragment = new PaperListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE_1", title1);
        bundle.putString("TITLE_2", title2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paper_list, null);
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
    }

    //初始化
    private void init() {
        this.listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ILoadingLayout loadLayout = listView.getLoadingLayoutProxy();
        loadLayout.setPullLabel("上拉加载");
        loadLayout.setRefreshingLabel("正在加载...");
        loadLayout.setReleaseLabel("释放加载");
        //查询数据库数据
        pictureList = this.queryDB();
        //构造网络参数
        requestParam = new WallPaperRequetParam();
        requestParam.setTitle1(title1);
        requestParam.setTitle2(title2);
        //如果查询出来的数据为空则启动服务
        if (pictureList == null || pictureList.size() == 0) {
            //启动下载图片列表的服务
            PictureResultGetServevice.startService(getActivity(), requestParam);
        }
        //数据库有数据
        else {
            //设置页数,从0开始,注意pn是当前的页数，如果当前有30条数据，当前就pn就是0
            requestParam.setPn(pictureList.size() - requestParam.getRn());
            //创建适配器
            this.adapter = new PictureGridAdapter(getActivity(), pictureList);
            //设置适配器
            this.listView.setAdapter(adapter);
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
        //添加刷新监听
        this.listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requestParam.setPn(requestParam.getPn() + requestParam.getRn());
                //访问网络获取图片
                PictureResultGetServevice.startService(getActivity(), requestParam);
            }
        });
        //添加item监听
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到图片详情
                ShowPictureDetailActivity.startActivity(getActivity(), position, pictureList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            return beanList.size() / 2 + 1;
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
            //第一张显示大图
            if (position == 0) {
                view = LayoutInflater.from(context).inflate(R.layout.picture_item_2, null);
                Picture bean = pictureList.get(position);
                final ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
                Glide.with(getActivity()).load(bean.getThumbUrl()).placeholder(R.drawable.coolwallpaper_empty).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //当第一幅图片加载上去之后，就发消息加载完毕
                        AppBus.getInstance().post(new LoadingFinishEvent());
                        ivItem.setImageDrawable(resource);
                    }
                });
            }
            //其余
            else {
                //注意：view不为空，但是tag是空的时候，那么
                if (view == null || view.getTag() == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.picture_item_3, null);
                    holder = new ViewHolder(view);
                    view.setTag(holder);
                }
                holder = (ViewHolder) view.getTag();
                int sum = (position + 1) * 2 - 1;//全部填满时候总的图片数
                int start = 2 * position - 1;//本层的起始位置
                //判断有没有越界
                if (sum <= pictureList.size()) {
                    //没有越界，可以显示两幅图
                    Glide.with(getActivity()).load(pictureList.get(start).getThumbUrl()).placeholder(R.drawable.coolwallpaper_empty).into(holder.ivLeft);
                    Glide.with(getActivity()).load(pictureList.get(start + 1).getThumbUrl()).placeholder(R.drawable.coolwallpaper_empty).into(holder.ivRight);
                    Logger.d("url = " + pictureList.get(start).getThumbUrl());
                    Logger.d("url = " + pictureList.get(start + 1).getThumbUrl());
                }
                //越界了，只能显示一幅图
                else {
                    Glide.with(getActivity()).load(pictureList.get(start).getThumbUrl()).placeholder(R.drawable.coolwallpaper_empty).into(holder.ivLeft);
                    Logger.d("url = " + pictureList.get(start).getThumbUrl());
                }
            }
            return view;
        }

        class ViewHolder {

            public ImageView ivLeft;
            public ImageView ivRight;

            public ViewHolder(View view) {
                ivLeft = (ImageView) view.findViewById(R.id.iv_item_left);
                ivRight = (ImageView) view.findViewById(R.id.iv_item_right);
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
        BaseRequestParam tmpParam = event.getRequestParam();
        Log.d(TAG, String.format("recv Message DownloadPictureResultSuccessEvent() title1=%s title2=%s ", tmpParam.getTitle1(), tmpParam.getTitle2()));
        Log.d(TAG, String.format("current title1=%s title2=%s", tmpParam.getTitle1(), tmpParam.getTitle2()));
        if (tmpParam != null && tmpParam.getUrl().equals(requestParam.getUrl())) {
            //查询数据
            pictureList = this.queryDB();
            if (pictureList != null) {
                //发送加载成功消息
                AppBus.getInstance().post(new LoadingFinishEvent());
                //还没有加载第一页
                if (adapter == null) {
                    //创建适配器
                    this.adapter = new PictureGridAdapter(getActivity(), pictureList);
                    //设置适配器
                    this.listView.setAdapter(adapter);
                }
                //已经有数据了
                else {
                    adapter.setBeanList(pictureList);
                    adapter.notifyDataSetChanged();
                }
                //停止刷新
                listView.onRefreshComplete();
            }

        }
    }
}
