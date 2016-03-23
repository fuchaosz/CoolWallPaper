package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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

    private LocalPaperAdapter localPaperAdapter;//普通状态下的适配器
    private List<String> pathList = new ArrayList<>();
    private boolean isDeleteFile = false;//是否同时删除文件

    @Bind(R.id.ly_left)
    View lyLeft;

    @Bind(R.id.iv_del)
    View ivDel;

    @Bind(R.id.tv_del)
    TextView tvDel;

    @Bind(R.id.gv_paper)
    GridView gvPaper;

    //空白页
    @Bind(R.id.ly_empty)
    View lyEmpty;

    //底部控制删除的view
    @Bind(R.id.ly_del_confirm)
    View lyDelConfirm;

    //底部删除view上面的删除数量的提示
    @Bind(R.id.tv_del_num)
    TextView tvDelNum;

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
        for (File f : file.listFiles()) {
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
            localPaperAdapter = new LocalPaperAdapter(this, pathList);
            gvPaper.setAdapter(localPaperAdapter);
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        lyDelConfirm.measure(width, height);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lyDelConfirm.getLayoutParams();
        params.bottomMargin = -lyDelConfirm.getMeasuredHeight();
        lyDelConfirm.setLayoutParams(params);
    }

    @OnClick({R.id.ly_left, R.id.iv_del, R.id.tv_del, R.id.ly_del_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_left:
                finish();
                break;
            //右上角的多选删除按钮
            case R.id.iv_del:
                //设置显示按钮
                localPaperAdapter.setShowRadio(true);
                localPaperAdapter.notifyDataSetChanged();
                //底部升起删除按钮
                showDelView();
                //隐藏图标
                ivDel.setVisibility(View.GONE);
                //显示全不选
                tvDel.setVisibility(View.VISIBLE);
                break;
            //点击了全选按钮
            case R.id.tv_del:
                onRightMenuClick();
                break;
            //确认删除
            case R.id.ly_del_confirm:
                confirmDelPic();
                break;

        }
    }

    //右边的全选按钮点击
    private void onRightMenuClick() {
        //如果是全不选
        if (tvDel.getText().equals("全不选")) {
            //设为全不选
            localPaperAdapter.setAllNotSelected();
            tvDel.setText("全选");
        }
        //如果是全选
        else {
            //设为全选
            localPaperAdapter.setAllSelected();
            tvDel.setText("全不选");
        }
        //刷新底部
        setDelNum(localPaperAdapter.getSelectedNum());
    }

    //确认删除
    private void confirmDelPic() {
        //弹出确认对话框
        if (localPaperAdapter.getSelectedNum() == 0) {
            //弹出一个请重新选择的对话框
            new AlertView("提示", "请选择要删除的本地壁纸!", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {

                }
            }).show();
        }
        //弹出是否删除的对话框
        else {
            final AlertView alertView = new AlertView("注意", String.format("确定要删除%d个图片吗?", localPaperAdapter.getSelectedNum()), "取消", null, new String[]{"确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    //点击了确定按钮
                    if (position != AlertView.CANCELPOSITION) {
                        //获取所有选中图片
                        List<String> selctedList = localPaperAdapter.getSlectedList();
                        //批量删除

                    }
                }
            });
            //加一个扩展框
            ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alert_view_ext_del_confirm, null);
            extView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            alertView.addExtView(extView);
            alertView.show();
        }
    }

    //适配器,普通状态的适配器
    public class LocalPaperAdapter extends BaseAdapter {

        protected List<String> filePathList;
        protected Context context;
        protected Set<String> selectSet = new HashSet<>();//记录选中的集合
        private boolean showRadio = false;//是否显示按钮

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
            final String path = filePathList.get(position);
            //显示图片
            Glide.with(context).load(path).into(holder.ivItem);
            //如果单选按钮可见
            if (showRadio) {
                //单选按钮可见
                holder.ivRadio.setVisibility(View.VISIBLE);
                //显示单选按钮
                showRadio(holder, path);
                //给单选按钮添加监听器
                final ViewHolder holder2 = holder;
                holder.ivRadio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击单选按钮后切换选中状态
                        toggle(holder2, path);
                        //刷新数据
                        setDelNum(getSelectedNum());
                    }
                });
            }
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
        public List<String> getSlectedList() {
            return new ArrayList<>(selectSet);
        }

        //设置所有都选中
        public void setAllSelected() {
            if (filePathList == null) {
                return;
            }
            for (String str : filePathList) {
                selectSet.add(str);
            }
            //刷新数据
            notifyDataSetChanged();
        }

        //设置所有多不选中
        public void setAllNotSelected() {
            selectSet.clear();
            notifyDataSetChanged();
        }

        //获取选中的数量
        public int getSelectedNum() {
            return selectSet.size();
        }

        public boolean isShowRadio() {
            return showRadio;
        }

        public void setShowRadio(boolean showRadio) {
            this.showRadio = showRadio;
        }

    }

    //底部升起删除按钮
    private void showDelView() {
        //创建动画
        final Animation anim = new TranslateAnimation(0, 0, 0, -lyDelConfirm.getMeasuredHeight());
        anim.setFillAfter(true);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后恢复高度
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lyDelConfirm.getLayoutParams();
                params.bottomMargin = 0;
                lyDelConfirm.setLayoutParams(params);
                lyDelConfirm.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lyDelConfirm.startAnimation(anim);
        //设置选中的数量
        setDelNum(localPaperAdapter.getSelectedNum());
    }

    //设置删除按钮的文本
    private void setDelNum(int num) {
        tvDelNum.setText("删除(" + num + ")");
    }
}
