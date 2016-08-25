package com.coolwallpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bean.IUserOperator;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobLogin;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.utils.LogUtil;
import com.coolwallpaper.utils.TimeUtil;
import com.coolwallpaper.utils.ToastUtil;
import com.coolwallpaper.utils.UmengUtil;
import com.coolwallpaper.utils.UserUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录界面
 * Created by fuchao on 2016/6/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final int RESULT_CODE_FAILURE = 0x1002;//登录失败的返回码
    private String TAG = "[LoginActivity]";
    private IUserInfo mUser;//登录成功后的用户

    /**
     * 启动方法。在OnActivityResult中返回的是IUserInfo对象
     *
     * @param context
     * @param requestCode 请求码
     */
    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup_window_login);
        //默认是登录失败
        setResult(RESULT_CODE_FAILURE);
    }

    @OnClick({R.id.ly_qq, R.id.ly_sina, R.id.iv_close})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //qq登录
            case R.id.ly_qq:
                qqLogin();
                break;
            //新浪微博登录
            case R.id.ly_sina:
                sinaLogin();
                break;
            //关闭按钮
            case R.id.iv_close:
                finish();
                break;
        }
    }

    //QQ登录
    public void qqLogin() {
        UmengUtil.getInstence().qqLogin(this, new UmengUtil.Callback() {

            @Override
            public void onSuccess() {
                //获取qq用户信息
                getQQUserInfo();
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取qq用户信息
    public void getQQUserInfo() {
        UmengUtil.getInstence().getQQUserInfo(this, new UmengUtil.InfoCallBack() {
            @Override
            public void getUserInfo(IUserInfo userInfo) {
                //获取用户信息失败
                if (userInfo == null) {
                    Toast.makeText(getApplicationContext(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //登录成功后要做一些业务处理
                dealLoginSuccess(userInfo);
            }
        });
    }

    //新浪登录
    private void sinaLogin() {
        UmengUtil.getInstence().sinaLogin(this, new UmengUtil.Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(String reason) {
            }
        });
    }

    //获取新浪微博用户信息
    private void getSinaUserInfo(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //must rewrite this method and add next code for the third login
        UmengUtil.getInstence().setActivityResult(requestCode, resultCode, data);
    }

    //登录成功后处理一下后续逻辑
    private void dealLoginSuccess(IUserInfo userInfo) {
        //查询是否存在这个用户
        BmobQuery<MyBmobUser> userQuery = BmobUtil.getMyUserQuery();
        userQuery.addWhereEqualTo("account", userInfo.getAccount());
        userQuery.findObjects(this, new FindListener<MyBmobUser>() {
            @Override
            public void onSuccess(List<MyBmobUser> list) {
                MyBmobUser user = null;
                //不存在这个用户
                if (list == null || list.size() == 0) {
                    //注册这个用户
                    user = new MyBmobUser();
                    user.setImgUrl(userInfo.getImg());
                    user.setSex(userInfo.getSex());
                    user.setUsername(userInfo.getName());
                    user.setPassword("123456");//这个是必填的，默认123456
                    user.setAccount(userInfo.getAccount());
                    final MyBmobUser finalUser = user;
                    user.signUp(MyApplication.getInstance(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.showDebug("保存用户到bmob成功");
                            LogUtil.d("保存用户到bmob成功");
                            //写入登录记录
                            saveLoginInfo(finalUser, userInfo);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtil.showDebug("保存用户到bmob失败:" + s);
                            LogUtil.d("保存用户到bmob失败:" + s);
                        }
                    });
                }
                //存在这个用户
                else {
                    user = (MyBmobUser) list.get(0);
                    //写入登录记录
                    saveLoginInfo(user, userInfo);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //保存登录信息
    private void saveLoginInfo(MyBmobUser bmobUser, IUserInfo userInfo) {
        //写入登录记录
        MyBmobLogin myBmobLogin = new MyBmobLogin();
        myBmobLogin.setTime(TimeUtil.toString(new Date()));
        myBmobLogin.setType(userInfo.getUserType());
        myBmobLogin.setUser(bmobUser);
        myBmobLogin.save(MyApplication.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.d("保存登录信息成功");
                //写入本地文件
                IUserOperator operator = UserUtil.getInstance();
                boolean result = operator.addUser(userInfo);
                if (result) {
                    ToastUtil.showDebug("保存用户到本地成功,name=" + userInfo.getName());
                } else {
                    ToastUtil.showDebug("保存用户到本地失败,name=" + userInfo.getName());
                }
                //获取当前登录成功的用户
                mUser = userInfo;
                //退出
                exit();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.d("保存登录信息失败,s=" + s);
            }
        });
    }


    //退出时，将User对象传递出去
    private void exit() {
        Intent intent = new Intent();
        intent.putExtra("USER", mUser);
        setResult(RESULT_OK, intent);
        finish();
    }
}
