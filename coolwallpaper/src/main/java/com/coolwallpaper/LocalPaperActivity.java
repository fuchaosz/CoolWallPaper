package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 本地壁纸
 * Created by fuchao on 2016/2/25.
 */
public class LocalPaperActivity extends BaseActivity implements View.OnClickListener {

    private LocalPaperAdapter locaPaperAdapter;//普通状态下的适配器
    private LocalPaperSelectAdapter localPaperSelectAdapter;//多选时的适配器
    private List<String> pathList = new ArrayList<>();

    @Bind(R.id.ly_left)
    View lyLeft;

    @Bind(R.id.ly_del)
    View lyDel;

    @Bind(R.id.gv_paper)
    GridView gvPaper;

    //空白页
    @Bind(R.id.ly_empty)
    View lyEmpty;

    //底部控制删除的view
    @Bind(R.id.ly_del_confirm)
    View lyDelConfirm;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LocalPaperActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_paper);
        this.init();
    }

    private void init() {
        //获取下载的壁纸
        File file = new File(FileUtil.getInstance().DIRECTORY_DOWNLOAD);
        for(File f : file.listFiles()){
            pathList.add(f.getAbsolutePath());
        }
        //如果没有图片
        if (pathList == null || pathList.size() == 0) {
            //显示空白页
            lyEmpty.setVisibility(View.VISIBLE);
            //隐藏列表
            gvPaper.setVisibility(View.GONE);
        }
        //有图片
        else {
            //隐藏空白页
            lyEmpty.setVisibility(View.GONE);
            //显示列表
            gvPaper.setVisibility(View.VISIBLE);
            locaPaperAdapter = new LocalPaperAdapter(this, pathList);
            localPaperSelectAdapter = new LocalPaperSelectAdapter(this, pathList);
            gvPaper.setAdapter(locaPaperAdapter);
        }

    }

    @OnClick({R.id.ly_left, R.id.ly_del})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_left:
                finish();
                break;
            //右上角的多选删除按钮
            case R.id.ly_del:
                //切换adapter
                gvPaper.setAdapter(localPaperSelectAdapter);
                break;
        }
    }

    //适配器,普通状态的适配器
    public class LocalPaperAdapter extends BaseAdapter {

        protected List<String> filePathList;
        protected Context context;

        public LocalPaperAdapter(Context context, List<String> filePathList) {
            this.context = context;
            this.filePathList = filePathList;
        }

        @Override
        public int getCount() {
            return filePathList == null ? 0 : filePathList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.local_paper_item, null);
                holder = new ViewHolder(view);
                holder.init();
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //取出数据
            String path = filePathList.get(position);
            //显示图片
            //Glide.with(context).load(path).into(holder.ivItem);
            holder.ivItem.setImageDrawable(getResources().getDrawable(R.drawable.example));
            return view;
        }

        class ViewHolder {

            @Bind(R.id.iv_item)
            public ImageView ivItem;
            @Bind(R.id.iv_radio)
            public ImageView ivRadio;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            private void init() {
                ivRadio.setVisibility(View.GONE);
                ivRadio.setBackgroundResource(R.drawable.icon_radio);
            }
        }
    }

    //复选状态的适配器
    private class LocalPaperSelectAdapter extends LocalPaperAdapter {

        private Set<String> selectSet;//记录选中的集合

        public LocalPaperSelectAdapter(Context context, List<String> filePathList) {
            super(context, filePathList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final ViewHolder holder = new ViewHolder(view);
            final String path = filePathList.get(position);
            //单选按钮可见
            holder.ivRadio.setVisibility(View.VISIBLE);
            //显示单选按钮
            showRadio(holder, path);
            final int position2 = position;
            //给单选按钮添加监听器
            holder.ivRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击单选按钮后切换选中状态
                    toggle(holder, path);
                }
            });
            return view;
        }

        //设置按钮的状态
        private void showRadio(ViewHolder holder, String path) {
            //如果已经被选中
            if (selectSet.contains(path)) {
                holder.ivRadio.setBackgroundResource(R.drawable.icon_radio_select);
            }
            //没有被选中
            else {
                holder.ivRadio.setBackgroundResource(R.drawable.icon_radio);
            }
        }

        //切换单选按钮的选中状态
        private void toggle(ViewHolder holder, String path) {
            //如果被选中了
            if (selectSet.contains(path)) {
                //从集合中删除
                selectSet.remove(path);
            }
            //没有被选中
            else {
                //加入集合
                selectSet.add(path);
            }
            //显示
            showRadio(holder, path);
        }

        //获取选中的图片列表
        public String[] getSlectedList() {
            return selectSet.toArray(new String[0]);
        }

    }
}
