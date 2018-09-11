package com.fedming.bottomnavigationdemo.model;

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
    private String posterScreenName;
    private String publishDateStr;
    private List<String> imageUrlList;

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

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
        for (int i=0;i<imageUrlList.size();i++){
            System.out.println(imageUrlList.get(i));
        }
        System.out.println(posterScreenName);
        System.out.println(publishDateStr);
   }

    public static List<News> toNews(JSONObject jsonObject) {
        List<News> list = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                List<String> urlList=new ArrayList<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                News news = new News();
                news.setTitle(jsonObject1.getString("title"));
                news.setContent(jsonObject1.getString("content"));
                news.setUrl(jsonObject1.getString("url"));
                try{
                    JSONArray ja=jsonObject1.getJSONArray("imageUrls");
                    for (int t=0;t<ja.length();t++){
                        urlList.add(ja.getString(t));
                    }
                }catch (Exception e){
                    System.out.println("该条新闻没有配图");
                }
                news.setImageUrlList(urlList);
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
