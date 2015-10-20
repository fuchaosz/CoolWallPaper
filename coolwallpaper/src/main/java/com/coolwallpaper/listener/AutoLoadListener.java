package com.coolwallpaper.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

/**
 * 自动加载监听
 * Created by fuchao on 2015/10/19.
 */
public class AutoLoadListener implements OnScrollListener {

    boolean isLastRow = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            isLastRow = false;
            Toast.makeText(view.getContext(), "加载更多", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }
    }
}
