package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.coolwallpaper.utils.HotTopicUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fuchao on 2016/4/19.
 */
public class SearchActivity extends BaseActivity {

    private List<String> hotTopicMan;
    private List<String> hotTopicGirl;
    private List<String> hotTopicMove;
    private OkHttpClient httpClient;
    private Handler handler;

    //标签布局
    @Bind(R.id.tag_group_1)
    TagGroup tagGroup1;

    @Bind(R.id.tag_group_2)
    TagGroup tagGroup2;

    @Bind(R.id.tag_group_3)
    TagGroup tagGroup3;

    //搜索框
    @Bind(R.id.ed_search)
    EditText edSearch;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.init();
        this.addListener();
    }

    private void init() {
        this.httpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
        //获取热词
        HotTopicUtil util = new HotTopicUtil();
        //用RxAndroid来切换线程
        //Observable.just("hello").subscribeOn(Schedulers.io()).flatMap(o -> Observable.just(util.getTopic())).observeOn(AndroidSchedulers.mainThread()).subscribe(topics -> tagGroup.setTags(topics));
        Observable.just("hello").subscribeOn(Schedulers.io()).map(o -> hotTopicMan = util.getTopicMan()).map(o -> hotTopicGirl = util.getTopicGirl()).map(o -> hotTopicMove = util.getTopicMove()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> showTags());
    }

    //添加监听器
    private void addListener() {
        //添加标签点击监听
        this.tagGroup1.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                //跳转到搜索结果页面
                showSearchResult(tag);
            }
        });
        this.tagGroup2.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                //跳转到搜索结果页面
                showSearchResult(tag);
            }
        });
        this.tagGroup3.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                //跳转到搜索结果页面
                showSearchResult(tag);
            }
        });
    }

    @OnClick({R.id.btn_search, R.id.ly_left})
    public void onClick(View v) {
        switch (v.getId()) {
            //搜索按钮
            case R.id.btn_search:
                //跳转到搜索结果页面
                showSearchResult(edSearch.getText().toString());
                break;
            //左边的箭头
            case R.id.ly_left:
                finish();
                break;
        }
    }

    //显示三个热词标签
    private void showTags() {
        tagGroup1.setTags(hotTopicMan);
        tagGroup2.setTags(hotTopicGirl);
        tagGroup3.setTags(hotTopicMove);
        findViewById(R.id.ly_tag_1).setVisibility(View.VISIBLE);
        findViewById(R.id.ly_tag_2).setVisibility(View.VISIBLE);
        findViewById(R.id.ly_tag_3).setVisibility(View.VISIBLE);
    }

    //显示搜索结果
    private void showSearchResult(String queryStr) {
        if (queryStr == null || "".equals(queryStr)) {
            Toast.makeText(this, "关键字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //跳转到搜索结果页面
        ShowSearchListActivity.startActivity(this, queryStr);
    }
}
