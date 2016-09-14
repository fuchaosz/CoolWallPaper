package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.coolwallpaper.utils.ToastUtil;
import com.coolwallpaper.utils.UserUtil;

/**
 * 启动上传图片类上传图片
 * Created by fuchao on 2016/9/8.
 */
public class UploadActivityStarter extends BaseActivity {

    private int REQUEST_CODE = 0x01;

    /**
     * 启动方法
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UploadActivityStarter.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //首先判断有没有登录
        if (UserUtil.getInstance().getUser() == null) {
            //没有用户登录，则直接跳转到登录界面
            LoginActivity.startActivityForResult(this, REQUEST_CODE);
        }
        //用户已经登录了
        else {
            //则直接跳转到上传图片的地方
            UploadActivity.startActivity(this);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //登录后返回
            if (requestCode == REQUEST_CODE) {
                ToastUtil.show("登录成功");
                //继续跳转到上传界面
                UploadActivity.startActivity(this);
            }
        }
        //登录失败
        if (resultCode == LoginActivity.RESULT_CODE_FAILURE) {
            ToastUtil.show("登录失败，请重试");
        }
        finish();
    }
}
