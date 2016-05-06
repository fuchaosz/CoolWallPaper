package com.coolwallpaper;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.coolwallpaper.utils.UmengUtil;

/**
 * 第三方登录的弹窗
 * Created by fuchao on 2016/5/3.
 */
public class LoginPopupWindow extends PopupWindow implements View.OnClickListener {

    private View contentView;
    private Activity context;

    public LoginPopupWindow(Activity context) {
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_window_login, null);
        //设置view
        this.setContentView(contentView);
        //设置宽高
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置窗口可以点击
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //设置动画效果
        this.setAnimationStyle(R.style.popup_window_anim_style);
        //添加简体器
        this.addListener();
    }

    //添加监听器
    private void addListener() {
        contentView.findViewById(R.id.ly_qq).setOnClickListener(this);
        contentView.findViewById(R.id.ly_wechat).setOnClickListener(this);
        contentView.findViewById(R.id.ly_sina).setOnClickListener(this);
        contentView.findViewById(R.id.iv_close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_qq:
            case R.id.ly_wechat:
            case R.id.ly_sina:
                qqLogin();
                break;
            //关闭弹窗
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }

    //弹出对话框
    public void showBottomDilog(View view) {
        //在底部显示
        this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    //QQ登录
    public void qqLogin() {
        UmengUtil.getInstence().qqLogin(context, new UmengUtil.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "qq login success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(context, "qq login failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
