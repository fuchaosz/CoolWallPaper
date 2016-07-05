package com.coolwallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolwallpaper.bmob.MyBmobUpdate;
import com.coolwallpaper.service.UpdateService;
import com.coolwallpaper.utils.ToastUtil;

import java.text.DecimalFormat;

/**
 * 下载Apk对话框
 * Created by fuchao on 2016/7/5.
 */
public class DownloadDialog {

    private Activity context;
    private MyBmobUpdate myBmobUpdate;//bmob的升级表
    private AlertDialog dialog;
    private View view;
    private TextView tvDownloadSize;//已经下载的文件大小
    private TextView tvTotalSize;//文件总大小
    private ProgressBar progressBar;//进度条
    private MyReceiver myReceiver;//广播接收器

    public DownloadDialog(Activity context, MyBmobUpdate myBmobUpdate) {
        this.context = context;
        this.myBmobUpdate = myBmobUpdate;
        this.init();
        this.addListener();
        this.registerBroadCast();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_downloading, null);
        //找出控件
        tvTotalSize = (TextView) view.findViewById(R.id.tv_total_size);
        tvDownloadSize = (TextView) view.findViewById(R.id.tv_download_size);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        //设置对话框属性
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.Sof);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void addListener() {
        //添加按键监听器
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //拦截返回键
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    //注册广播
    private void registerBroadCast() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加下载进度监听
        intentFilter.addAction(UpdateService.ACTION_DOWNLOAD_PROGRESS);
        intentFilter.addAction(UpdateService.ACTION_DOWNLOAD_FAILURE);
        intentFilter.addAction(UpdateService.ACTION_DOWNLOAD_SUCCESS);
        context.registerReceiver(myReceiver, intentFilter);
        //添加对话框消失的监听
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //取消注册广播
                context.unregisterReceiver(myReceiver);
            }
        });
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            dialog.setContentView(view);
            //调整对话框的大小
            WindowManager m = context.getWindowManager();
            Display d = m.getDefaultDisplay();
            //获取Activity的窗口的大小
            Point point = new Point();
            d.getSize(point);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            //按照比例设置对话框的宽度
            lp.width = (int) (point.x * 0.85);
            dialog.getWindow().setAttributes(lp);
            //第一次显示的时候，启动升级服务
            UpdateService.startService(context, myBmobUpdate);
        } else {
            dialog.show();
        }
    }

    //广播接收器
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //如果对话框消失的时候，就不要处理广播了
            if (!dialog.isShowing()) {
                return;
            }
            //刷新进度的广播
            if (action.equals(UpdateService.ACTION_DOWNLOAD_PROGRESS)) {
                //取出数据
                int progress = intent.getIntExtra("progress", 0);
                long downloadSize = intent.getLongExtra("downloadSize", 0);
                long totalSize = intent.getLongExtra("totalSize", 0);
                //显示进度
                progressBar.setProgress(progress);
                //显示下载数据
                double tmpSize = downloadSize * 1.0f / 1000 / 1000;
                //保留1位小数
                DecimalFormat df = new DecimalFormat("##0.0");
                tvDownloadSize.setText(df.format(tmpSize) + "M");
                //显示全部数据
                tmpSize = totalSize * 1.0f / 1000 / 1000;
                tvTotalSize.setText(df.format(tmpSize) + "M");
            }
            //下载失败的广播
            else if (action.equals(UpdateService.ACTION_DOWNLOAD_FAILURE)) {
                ToastUtil.show("安装包下载失败");
                dialog.dismiss();
            }
            //下载成功的广播
            else if (action.equals(UpdateService.ACTION_DOWNLOAD_SUCCESS)) {
                ToastUtil.show("安装包下载成功");
                dialog.dismiss();
            }
        }
    }


}
