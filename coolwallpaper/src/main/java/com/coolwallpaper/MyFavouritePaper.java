package com.coolwallpaper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.utils.ToastUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.PageIndicator;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 我的收藏
 * Created by fuchao on 2016/5/31.
 */
public class MyFavouritePaper extends BaseActivity implements View.OnClickListener {

    private MyBmobUser user;//登录成功的用户
    private List<MyBmobFavourite> favouritePaperList;//用户收藏的图片

    @Bind(R.id.title_indictor)
    PageIndicator pageIndicator;

    @Bind(R.id.vp)
    ViewPager viewPager;

    /**
     * 启动方法
     *
     * @param context
     * @param user    登录成功后的用户
     */
    public static void startActivity(Context context, MyBmobUser user) {
        Intent intent = new Intent(context, MyFavouritePaper.class);
        intent.putExtra("USER", user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = (MyBmobUser) getIntent().getSerializableExtra("USER");
        setContentView(R.layout.activity_my_favourite);
    }

    private void init() {

    }

    //查询所有收藏的壁纸
    private void queryMyFavourite() {
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("user", user);
        query.findObjects(this, new FindListener<MyBmobFavourite>() {
            @Override
            public void onSuccess(List<MyBmobFavourite> list) {
                favouritePaperList = list;
                ToastUtil.showDebug("获取用户收藏的图片成功");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show("查询收藏图片失败:" + s);
            }
        });
    }

    @OnClick({R.id.ly_local_paper, R.id.ly_change, R.id.ly_upload})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //local paper
            case R.id.ly_local_paper:
                break;
            //auto refresh paper
            case R.id.ly_change:
                break;
            //my upload paper
            case R.id.ly_upload:
                break;
        }
    }

    //ViewPager的适配器
    private class MyViewPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titles;

        public MyViewPagerAdapter(List<Fragment> fragmentList, String[] titles) {
            super(getFragmentManager());
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public android.app.Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

}
