package com.coolwallpaper.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 热词获取工具,热词都来自搜狗搜索榜单
 * Created by fuchao on 2016/4/19.
 */
public class HotTopicUtil {

    private String urlMan = "http://top.sogou.com/people/nanyanyuan_1.html";//男演员排行
    private String urlGirl = "http://top.sogou.com/people/nvyanyuan_1.html";//女演员排行
    private String urlMove = "http://top.sogou.com/movie/quanbu_1.html";//电影排行

    private List<String> getTopicByUrl(String url) {
        Document content = null;
        List<String> result = new ArrayList<>();
        try {
            content = Jsoup.connect(url).timeout(5000).get();
            // 选出所有的列表项
            Elements lis = content.select("ul.pub-list li");
            for (Element li : lis) {
                String str = li.select("span.s2 a").first().text();
                result.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getTopic() {
        List<String> result = new ArrayList<String>();
        result.addAll(getTopicByUrl(urlMan));
        result.addAll(getTopicByUrl(urlGirl));
        result.addAll(getTopicByUrl(urlMove));
        return result;
    }
}
