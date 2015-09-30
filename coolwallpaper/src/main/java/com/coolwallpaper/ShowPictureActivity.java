package com.coolwallpaper;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 显示图片的Activity
 * Created by fuchao on 2015/9/29.
 */
public class ShowPictureActivity extends BaseActivity {

    private HttpUtils httpUtils;
    private WallPaperRequetParam requetParam;

    @ViewInject(R.id.rv_picture)
    RecyclerView rvPictue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        this.requetParam = new WallPaperRequetParam();
        //查询图片
        this.queryPicture();
    }

    //访问网络,查询图片
    private void queryPicture() {
        httpUtils.send(HttpRequest.HttpMethod.GET, requetParam.getUrl(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //responseInfo.result;
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //显示图片
    private void showPicture() {
        //Gson gson;
    }
}
