package com.coolwallpaper;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * 第三方登录的弹窗
 * Created by fuchao on 2016/5/3.
 */
public class LoginPopupWindow extends PopupWindow implements View.OnClickListener {

    public final String TAG = "[fuchao][LoginPopupWindow]";
    private View contentView;
    private Activity context;
    private OnLoginListener onLoginListener;

    //登录类型常量
    public static final int LOGIN_TYPE_QQ = 1;
    public static final int LOGIN_TYPE_SINA = 2;
    public static final int LOGIN_TYPE_WECHAT = 3;

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
            //qq login
            case R.id.ly_qq:
                if (onLoginListener != null) {
                    onLoginListener.onLoginClick(LOGIN_TYPE_QQ);
                }
                break;
            // wechat login
            case R.id.ly_wechat:
                if (onLoginListener != null) {
                    onLoginListener.onLoginClick(LOGIN_TYPE_WECHAT);
                }
                break;
            //sina login
            case R.id.ly_sina:
                if (onLoginListener != null) {
                    onLoginListener.onLoginClick(LOGIN_TYPE_SINA);
                }
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

    //设置点击监听
    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    /**
     * 登录监听
     */
    public static interface OnLoginListener {

        /**
         * 登录按钮点击
         *
         * @param loginType 登录类型,详见本类常量字段LOGIN_TYPE
         */
        public void onLoginClick(int loginType);
    }

}
