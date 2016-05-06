package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by fuchao on 2016/4/29.
 */
public class MyCenterActivity extends BaseActivity {

    private LoginPopupWindow popupWindow;

    @Bind(R.id.iv_face)
    ImageView ivFace;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_tip)
    TextView tvTip;

    /**
     * 启动方法
     *
     * @param context
     */
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
        if (popupWindow == null) {
            popupWindow = new LoginPopupWindow(this);
        }
        popupWindow.showBottomDilog(getWindow().getDecorView());
    }
}
