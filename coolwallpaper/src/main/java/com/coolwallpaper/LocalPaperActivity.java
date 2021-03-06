package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.coolwallpaper.model.LocalPictureDao;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.LocalPaperUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 本地壁纸
 * Created by fuchao on 2016/2/25.
 */
public class LocalPaperActivity extends BaseActivity implements View.OnClickListener {

    private static int REQUEST_IMAGE = 1;//请求多选图片
    private LocalPaperAdapter localPaperAdapter;//普通状态下的适配器
    private List<String> pathList = new ArrayList<>();
    private boolean isDeleteFile = false;//是否同时删除文件

    @Bind(R.id.ly_left)
    View lyLeft;

    @Bind(R.id.ly_del)
    View lyDel;

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
        this.addListener();
    }

    private void init() {
        //获取下载的壁纸
        pathList = LocalPaperUtil.getLocalPaperPathList();
        //如果没有图片
        if (pathList == null || pathList.size() == 0) {
            //显示空白页
            lyEmpty.setVisibility(View.VISIBLE);
            //隐藏列表
            gvPaper.setVisibility(View.GONE);
            //隐藏左边的删除图片
            ivDel.setVisibility(View.GONE);
        }
        //有图片
        else {
            //隐藏空白页
            lyEmpty.setVisibility(View.GONE);
            //显示列表
            gvPaper.setVisibility(View.VISIBLE);
            localPaperAdapter = new LocalPaperAdapter(this, pathList);
            gvPaper.setAdapter(localPaperAdapter);
            //显示左边的删除图片
            ivDel.setVisibility(View.VISIBLE);
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        lyDelConfirm.measure(width, height);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lyDelConfirm.getLayoutParams();
        params.bottomMargin = -lyDelConfirm.getMeasuredHeight();
        lyDelConfirm.setLayoutParams(params);
    }

    private void addListener() {
        //添加GridView点击监听
        this.gvPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取出点击的图片
                String path = pathList.get(position);
                //查看整个图片
                showPopupWoindow(path);
            }
        });
    }

    @OnClick({R.id.ly_left, R.id.iv_del, R.id.ly_del, R.id.ly_del_confirm, R.id.ly_empty})
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
                lyDel.setVisibility(View.VISIBLE);
                break;
            //点击了全选按钮
            case R.id.ly_del:
                onRightMenuClick();
                break;
            //确认删除
            case R.id.ly_del_confirm:
                confirmDelPic();
                break;
            //添加本地壁纸
            case R.id.ly_empty:
                addLocalPaper();
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
                        //判断数据是否为空
                        if (selctedList == null || selctedList.size() == 0) {
                            return;
                        }
                        //数据库中删除
                        deletLocalPictureFromDB(selctedList);
                        //批量删除
                        if (isDeleteFile) {
                            FileUtil.getInstance().deleteFileList(selctedList);
                        }
                        //刷新界面
                        init();
                    }
                }
            });
            //加一个扩展框
            View extView = LayoutInflater.from(this).inflate(R.layout.alert_view_ext_del_confirm, null);
            final ImageView ivCheck = (ImageView) extView.findViewById(R.id.iv_check);
            extView.setOnClickListener(new View.OnClickListener() {

                boolean isChecked = false;

                @Override
                public void onClick(View v) {
                    //切换图片
                    if (isChecked) {
                        //点击之前是选中状态
                        ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.icon_check));
                        isChecked = false;
                        //现在是没有选中状态
                        isDeleteFile = false;
                    }
                    //之前没有被选中
                    else {
                        //之前没有被选中则点击之后要被选中
                        ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.icon_check_blue));
                        isChecked = true;
                        //点击后是选中状态
                        isDeleteFile = true;
                    }
                }
            });
            alertView.addExtView(extView);
            alertView.show();
        }
    }

    //添加本地壁纸,跳转到图片多选界面
    private void addLocalPaper() {
        //跳转到图片多选界面
        Intent intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择图片,回填选项(支持String ArrayList)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, new ArrayList<String>());
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取用户选择的图片
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                //添加到数据库中
                LocalPaperUtil.insertLocalPaperList(pathList);
                //刷新界面
                init();
            }
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

    //从数据库中删除本地图片
    private void deletLocalPictureFromDB(List<String> pathList) {
        LocalPictureDao localPictureDao = DBUtil.getInstance().getLocalPictureDao();
        QueryBuilder qb = localPictureDao.queryBuilder();
        //遍历
        for (String path : pathList) {
            qb.where(LocalPictureDao.Properties.Path.eq(path));
            //如果数据库存在则删除
            localPictureDao.deleteInTx(qb.list());
        }
    }

    //用PopupWindow来查看整个图片
    private void showPopupWoindow(String path) {
        View view = getLayoutInflater().inflate(R.layout.activity_local_paper_show, null);
        Glide.with(this).load(path).into((ImageView) view.findViewById(R.id.iv));
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.coolwallpaper_home_bg));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });

    }
}
