package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.PictureBean;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    private PictureBean pictureBean;
    private ImageLoader imageLoader;

    @ViewInject(R.id.iv_image)
    ImageView ivImage;

    //浮动层菜单
    @ViewInject(R.id.ly_picture_detail_menu)
    View lyPictureDetailMenu;

    /**
     * 启动方法，要显示的图片
     *
     * @param context
     * @param bean
     */
    public static void startActivity(Context context, PictureBean bean) {
        Intent intent = new Intent(context, ShowPictureDetailActivity.class);
        intent.putExtra("PICTURE_BEAN", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        this.pictureBean = (PictureBean) getIntent().getSerializableExtra("PICTURE_BEAN");
        //初始化
        this.init();
    }

    //初始化
    private void init() {
        //创建ImageLoader
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //显示图片
        this.imageLoader.displayImage(pictureBean.getImageUrl(), ivImage);
    }

    @OnClick(R.id.iv_image)
    public void onClick(View v) {
        switch (v.getId()) {
            //点击图片
            case R.id.iv_image:
                //弹出浮动层菜单
                lyPictureDetailMenu.setVisibility(View.VISIBLE);
                break;
        }
    }
}
