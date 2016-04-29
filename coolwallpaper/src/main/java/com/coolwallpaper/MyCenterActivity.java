package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by fuchao on 2016/4/29.
 */
public class MyCenterActivity extends BaseActivity {

    @Bind(R.id.iv_face)
    ImageView ivFace;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_tip)
    TextView tvTip;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        this.init();
    }

    public void init() {

    }

    @OnClick({R.id.ly_left_arrow, R.id.iv_face, R.id.ly_my_wallpaper, R.id.ly_my_favour, R.id.ly_my_download, R.id.ly_my_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            //标题栏上返回键
            case R.id.ly_left_arrow:
                break;
            //我的头像
            case R.id.iv_face:
                showLoginWindow();
                break;
            //我的壁纸
            case R.id.ly_my_wallpaper:
                break;
            //我的收藏
            case R.id.ly_my_favour:
                break;
            //我的下载
            case R.id.ly_my_download:
                break;
            //我的上传
            case R.id.ly_my_upload:
                break;
        }
    }

    //弹出登录窗口
    private void showLoginWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_window_login, null);
        PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        //指定动画
        window.setAnimationStyle(R.style.popup_window_anim_style);
        //在底部显示
        window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
