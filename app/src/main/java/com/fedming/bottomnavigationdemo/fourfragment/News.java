package com.fedming.bottomnavigationdemo.fourfragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cdq
 * created on 2018.9.9
 * 该类用来表示新闻类，新闻源来自聚合数据360api
 */

public class News {

    private String title;
    private String content;
    private String url;
    private String[] imageArray;
    private String posterScreenName;
    private String publishDateStr;

    public String getPosterScreenName() {
        return posterScreenName;
    }

    public void setPosterScreenName(String posterScreenName) {
        this.posterScreenName = posterScreenName;
    }

    public String getPublishDateStr() {
        return publishDateStr;
    }

    public void setPublishDateStr(String publishDateStr) {
        this.publishDateStr = publishDateStr;
    }

    public String[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(String[] imageArray) {
        this.imageArray = imageArray;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   public void printNews(){
        System.out.println(title);
        System.out.println(content);
        System.out.println(url);
        for (int i=0;i<imageArray.length;i++){
            System.out.println(imageArray[i]);
        }
        System.out.println(posterScreenName);
        System.out.println(publishDateStr);
   }

    public static List<News> toNews(JSONObject jsonObject) {
        List<News> list = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                News news = new News();
                news.setTitle(jsonObject1.getString("title"));
                news.setContent(jsonObject1.getString("content"));
                news.setUrl(jsonObject1.getString("url"));
                String[] is=jsonObject1.getString("imageUrls").split(",");
                news.setImageArray(is);
                news.setPosterScreenName(jsonObject1.getString("posterScreenName"));
                news.setPublishDateStr(jsonObject1.getString("publishDateStr"));
                list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return list;
    }

}
