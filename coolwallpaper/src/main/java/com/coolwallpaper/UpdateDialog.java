package com.coolwallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.coolwallpaper.bmob.MyBmobUpdate;

/**
 * 升级提示对话框
 * Created by fuchao on 2016/7/4.
 */
public class UpdateDialog implements View.OnClickListener {

    private Activity context;
    private MyBmobUpdate myBmobUpdate;//bmob的升级表
    private AlertDialog dialog;
    private View view;
    private TextView tvVersionName;//版本名
    private TextView tvVersionCode;//版本号
    private TextView tvSize;//安装包大小

    public UpdateDialog(Activity context, MyBmobUpdate myBmobUpdate) {
        this.context = context;
        this.myBmobUpdate = myBmobUpdate;
        this.init();
        this.addListener();
    }

    private void init() {
        //根据是否强制升级来选择不同的view
        if (myBmobUpdate.getForceUpdate()) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_update_force, null);
            view.findViewById(R.id.ly_ok).setOnClickListener(this);
        }
        //选择升级
        else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_update, null);
            view.findViewById(R.id.ly_cancel).setOnClickListener(this);
            view.findViewById(R.id.ly_ok).setOnClickListener(this);
        }
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消按钮
            case R.id.ly_cancel:
                dialog.dismiss();
                break;
            //安装按钮
            case R.id.ly_ok:
                dialog.dismiss();
                //调用下载对话框
                new DownloadDialog(context, myBmobUpdate).show();
                break;
        }
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
        }
    }
}
