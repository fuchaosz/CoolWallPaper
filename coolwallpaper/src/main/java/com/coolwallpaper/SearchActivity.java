package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.coolwallpaper.utils.HotTopicUtil;

import java.util.List;

import butterknife.Bind;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by fuchao on 2016/4/19.
 */
public class SearchActivity extends BaseActivity {

    private List<String> hotTopicList;

    //标签布局
    @Bind(R.id.tag_group)
    TagGroup tagGroup;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        TextView tv = new TextView(this);
        tv.setOnClickListener(v -> Toast.makeText(this, "lambd", Toast.LENGTH_SHORT).show());
        this.init();
    }

    private void init() {
        //获取热词
        HotTopicUtil util = new HotTopicUtil();
        hotTopicList = util.getTopic();
        //显示在TabGroup中
        tagGroup.setTags(hotTopicList);
    }

    private class GetTopicTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }
    }
}
