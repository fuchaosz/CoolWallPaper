package com.coolwallpaper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolwallpaper.service.BmobUploadService;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.LogUtil;
import com.coolwallpaper.utils.ToastUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 上传图片
 * Created by fuchao on 2016/6/27.
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener {

    private int REQUEST_IMAGE = 0x01;
    private Map<String, Integer> progressMap;//上传进度map
    private MyAdapter adapter;
    private List<String> pathList;//所有要上传的图片列表
    private MyConn myConn;//绑定服务
    private BmobUploadService uploadService;//上传服务
    private Set<String> delSet;//删除或取消上传的图片列表
    private String desc;//图片的描述,上传图片的时候要写图片描述

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.ly_empty)
    View lyEmpty;

    @Bind(R.id.lv_upload)
    ListView lvUpload;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UploadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        this.myConn = new MyConn();
        //绑定服务
        BmobUploadService.bindService(this, myConn);
        tvTitle.setText("上传图片");
    }

    //绑定服务的时候的连接对象
    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取服务的实例
            uploadService = ((BmobUploadService.MyBinder) service).getUploadService();
            LogUtil.d("绑定BmobUploadService服务成功");
            //初始化
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void init() {
        //获取正在上传的图片的数量
        pathList = uploadService.getUploadingPathList();
        progressMap = uploadService.getProgressMap();
        delSet = uploadService.getDelPathSet();
        //如果没有数据
        if (EmptyUtil.isEmpty(pathList)) {
            //显示空白页
            lyEmpty.setVisibility(View.VISIBLE);
            lvUpload.setVisibility(View.GONE);
        }
        //有数据
        else {
            //显示列表
            lyEmpty.setVisibility(View.GONE);
            lvUpload.setVisibility(View.VISIBLE);
            //创建adapter
            this.adapter = new MyAdapter(this, pathList, progressMap, delSet);
            this.lvUpload.setAdapter(adapter);
        }
        //注册广播,刷新进度
        IntentFilter filter = new IntentFilter(BmobUploadService.ACTION_UPDATE_PFROGRESS);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //取出参数
                String path = intent.getStringExtra("PATH");
                int progress = intent.getIntExtra("PROGRESS", 0);
                LogUtil.d("收到进度广播,progress = " + progress + " path = " + path);
                //刷新图片
                updateProgress(path, progress);
            }
        }, filter);

    }

    @OnClick({R.id.ly_menu_left, R.id.iv_upload, R.id.ly_menu_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //向左的箭头
            case R.id.ly_menu_left:
                finish();
                break;
            //菜单栏右边上传图片菜单
            case R.id.ly_menu_right:
            case R.id.iv_upload://空白界面上传图片
                //添加图片
                addPic();
                break;
        }
    }

    //添加图片
    private void addPic() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.layout_add_desc_dialog);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCanceledOnTouchOutside(false);
        View view = dialog.getWindow().getDecorView();
        EditText edDesc = (EditText) view.findViewById(R.id.ed_desc);
        //取消按钮
        view.findViewById(R.id.ly_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showDebug("点击了取消按钮");
                dialog.dismiss();
            }
        });
        //完成按钮
        view.findViewById(R.id.ly_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpDesc = edDesc.getText().toString();
                if (EmptyUtil.isEmpty(tmpDesc)) {
                    ToastUtil.show("图片描述不能为空");
                } else {
                    //保存图片描述
                    desc = tmpDesc;
                    // 跳转到图片选择界面
                    selectPic();
                    dialog.dismiss();
                }

            }
        });

    }

    //添加图片
    private void selectPic() {
        //跳转到图片多选界面
        Intent intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择图片,回填选项(支持String ArrayList)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, new ArrayList<>());
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //上传图片
    private void upload(List<String> uploadList) {
        if (EmptyUtil.isEmpty(uploadList)) {
            return;
        }
        //将已经选中的去掉,防止重复上传
        if (!EmptyUtil.isEmpty(pathList)) {
            Iterator<String> it = uploadList.iterator();
            //遍历用户选择的图片
            while (it.hasNext()) {
                String tmp = it.next();
                //如果之前已经上传过来，则删除掉
                if (pathList.contains(tmp)) {
                    it.remove();
                }
            }
        }
        //筛选完了之后在判断一下，是否还有剩余的
        if (EmptyUtil.isEmpty(uploadList)) {
            return;
        }
        //隐藏空白界面
        lyEmpty.setVisibility(View.GONE);
        lvUpload.setVisibility(View.VISIBLE);
        //没有数据
        if (adapter == null) {
            progressMap = uploadService.getProgressMap();
            pathList = new ArrayList<>();
            pathList.addAll(uploadList);
            delSet = uploadService.getDelPathSet();
            //创建adapter
            this.adapter = new MyAdapter(this, pathList, progressMap, delSet);
            this.lvUpload.setAdapter(adapter);
        }
        //有数据了
        else {
            pathList.addAll(uploadList);
            adapter.setPathList(pathList);
            adapter.notifyDataSetChanged();
        }
        //调用服务去上传
        uploadService.upload(uploadList, desc);
    }

    //刷新进度
    private void updateProgress(String path, int progress) {
        //局部刷新
        int position = 0;
        //判断path是否存在于列表中
        if (!pathList.contains(path)) {
            return;
        }
        //找到在Path中的位置
        for (int i = 0; i < pathList.size(); i++) {
            //找出path图片所在的位置
            if (pathList.get(i).equals(path)) {
                position = i;
                break;
            }
        }
        //找出对应的view
        int firstVisiable = lvUpload.getFirstVisiblePosition();
        View view = lvUpload.getChildAt(position - firstVisiable);
        adapter.updateView(view, path, progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取用选择的图片
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                //上传图片
                upload(pathList);
            }
        }
    }

    //上传列表适配器
    public class MyAdapter extends BaseAdapter {

        private Context context;
        private Map<String, Integer> progressMap;
        private List<String> pathList;//所有要上传的图片列表
        private Set<String> delSet;//删除集合

        public MyAdapter(Context context, List<String> pathList, Map<String, Integer> progressMap, Set<String> delSet) {
            this.context = context;
            this.progressMap = progressMap;
            this.pathList = pathList;
            this.delSet = delSet;
        }

        @Override
        public int getCount() {
            return pathList == null ? 0 : pathList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.upload_dialog, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //取出数据
            String path = pathList.get(position);
            Integer progress = progressMap.get(path);
            if (progress == null) {
                progress = 0;
            }
            //设置数据
            setData(holder, path, progress);
            return view;
        }

        class ViewHolder {

            @Bind(R.id.ly_item)
            View lyItem;
            @Bind(R.id.tv_pic_name)
            TextView tvPicName;
            @Bind(R.id.pb)
            ProgressBar progressBar;
            @Bind(R.id.btn_cancel)
            Button btnCancel;

            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }

        //设置数据
        public void setData(ViewHolder holder, String path, int progress) {
            holder.tvPicName.setText(FileUtil.getFileName(path));
            //判断是不是已经删除的
            if (delSet != null && delSet.contains(path)) {
                //设置取消的界面
                setCancelView(holder, "已删除");
            }
            //没有删除
            else {
                //设置进度
                holder.progressBar.setProgress(progress);
                String str = "";
                //小于100则是取消
                if (progress < 100) {
                    str = "取消";
                }
                //上传完毕则是删除
                else {
                    str = "删除";
                }
                holder.btnCancel.setText(str);
                //添加取消按钮监听事件
                final String finalStr = str;
                holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加入到取消列表中
                        delSet.add(path);
                        //通知服务端
                        uploadService.setDelPathSet(delSet);
                        //设置取消的item界面
                        setCancelView(holder, "已" + finalStr);
                    }
                });
            }
        }

        //设置取消的view
        private void setCancelView(ViewHolder holder, String btnCancelStr) {
            holder.progressBar.setProgress(0);
            //背景颜色变为灰色
            holder.lyItem.setBackgroundColor(getResources().getColor(R.color.halfTransparent));
            holder.btnCancel.setOnClickListener(null);
            holder.btnCancel.setText(btnCancelStr);
            //按钮变为灰色
            holder.btnCancel.setBackgroundResource(R.drawable.rectange_round_gray_shape_2);
        }

        //局部刷新
        public void updateView(View view, String path, int progress) {
            ViewHolder holder = (ViewHolder) view.getTag();
            setData(holder, path, progress);
        }

        public void setProgressMap(Map<String, Integer> progressMap) {
            this.progressMap = progressMap;
        }

        public void setPathList(List<String> pathList) {
            this.pathList = pathList;
        }
    }

    @Override
    protected void onDestroy() {
        //解除服务的绑定
        if (myConn != null) {
            unbindService(myConn);
        }
        super.onDestroy();
    }
}
