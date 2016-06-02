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
import com.coolwallpaper.model.LocalPicture;
import com.coolwallpaper.utils.LocalPaperUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 本地壁纸列表
 * Created by fuchao on 2016/6/1.
 */
public class LocalPaperFragment extends Fragment {

    private LocalAdapter localAdapter;

    @Bind(R.id.grid_view)
    GridView gridView;

    /**
     * 创建实例
     *
     * @return
     */
    public static LocalPaperFragment newInstance() {
        LocalPaperFragment fragment = new LocalPaperFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_paper_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.init();
    }

    private void init() {
        this.localAdapter = new LocalAdapter(getActivity());
        this.gridView.setAdapter(localAdapter);
    }


    //本地壁纸的ListView
    private class LocalAdapter extends BaseAdapter {

        Context context;
        List<LocalPicture> localPaperList;

        public LocalAdapter(Context context) {
            this.context = context;
            this.localPaperList = LocalPaperUtil.getLocalPaperList();
        }

        @Override
        public int getCount() {
            return localPaperList == null ? 0 : localPaperList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.local_paper_item_2, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //绑定数据
            LocalPicture picture = localPaperList.get(position);
            Glide.with(context).load(picture.getPath()).into(holder.ivItem);
            return view;
        }

        class ViewHolder {

            @Bind(R.id.iv_item)
            ImageView ivItem;

            public ViewHolder(View v) {
                ButterKnife.bind(v);
            }
        }
    }
}
