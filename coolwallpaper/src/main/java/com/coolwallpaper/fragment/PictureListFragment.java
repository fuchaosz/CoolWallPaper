package com.coolwallpaper.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.coolwallpaper.R;
import com.coolwallpaper.bean.PictureBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;
import java.util.List;

/**
 * 显示图片列表的fragment
 * Created by fuchao on 2015/10/28.
 */
public class PictureListFragment extends Fragment {

    private ListView lvPicture;
    private List<PictureBean> beanList;
    private ImageLoader imageLoader;
    private PicAdapter adapter;

    //创建方法
    public static PictureListFragment newInstance(List<PictureBean> beanList) {
        PictureListFragment fragment = new PictureListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BEAN_LIST", (Serializable) beanList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_list, container, false);
        lvPicture = (ListView) view.findViewById(R.id.lv_pic);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.beanList = (List<PictureBean>) getArguments().getSerializable("BEAN_LIST");
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        this.adapter = new PicAdapter(getActivity(), beanList);
        this.lvPicture.setAdapter(adapter);
    }

    //列表数据适配器
    private class PicAdapter extends BaseAdapter {

        private List<PictureBean> beanList;
        private Context context;

        public PicAdapter(Context context, List<PictureBean> beanList) {
            this.context = context;
            this.beanList = beanList;
        }

        @Override
        public int getCount() {
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
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.fragment_pic_lsit_item, null);
                holder = new ViewHolder();
                holder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //绑定数据
            imageLoader.displayImage(beanList.get(position).getSmallImageUrl(), holder.ivImage);
            return view;
        }

        public class ViewHolder {
            ImageView ivImage;
        }
    }
}
