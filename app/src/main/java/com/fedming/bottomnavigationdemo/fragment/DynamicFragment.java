package com.fedming.bottomnavigationdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fedming.bottomnavigationdemo.R;
import com.fedming.bottomnavigationdemo.model.News;
import com.fedming.bottomnavigationdemo.utils.GetNews;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends Fragment {

    private List<News> newsList = new ArrayList<>();

    private ListView listView;

    private Dialog dialog;

    @SuppressLint("HandlerLeak")
    public Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==100){
                listView.setAdapter(new MyAdapter());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Uri uri = Uri.parse(newsList.get(i).getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                dialog.cancel();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog=new Dialog(getContext(),Dialog.BUTTON_NEGATIVE);
        dialog.setTitle("努力加载中");
        dialog.show();
        View view = inflater.inflate(R.layout.fragment_dongtai, null);
        listView = view.findViewById(R.id.list_dongtai);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("开始加载网络数据......");
                    newsList = GetNews.getNews();
                    System.out.println("加载完成");
                    myHandler.sendEmptyMessage(100);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return view;
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int i) {
            return newsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            System.out.println("这是MyAdapter的getview方法");

            final MyHolder myHolder;

            if (view == null) {
                myHolder = new MyHolder();
                view = View.inflate(getContext(), R.layout.item_news, null);
                myHolder.imageViews[0] = view.findViewById(R.id.image1);
                myHolder.imageViews[1] = view.findViewById(R.id.image2);
                myHolder.imageViews[2] = view.findViewById(R.id.image3);
                myHolder.imageViews[3] = view.findViewById(R.id.image4);
                myHolder.textView_author = view.findViewById(R.id.author);
                myHolder.textView_time = view.findViewById(R.id.time);
                myHolder.textView_title = view.findViewById(R.id.textView2);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }
            News news = newsList.get(i);
            List<String> list=news.getImageUrlList();
            //图片加载条件，如果图片数量超过4只加载前四章，如果没有则加载暂时没有图片
            if (list.size()!=0){
                if (list.size()>3){
                    int size=4;
                    for (int s=0;s<size;s++){
                        Glide.with(getContext()).load(list.get(s)).into(myHolder.imageViews[s]);
                    }
                }else {
                    //temp记录list的数量，以便于为后续没有图片的view加载提示照片
                    int temp=list.size();
                    for (int s=0;s<list.size();s++){
                        Glide.with(getContext()).load(list.get(s)).into(myHolder.imageViews[s]);
                    }
                    for (int j=temp;j<4;j++){
                        String url="http://bmob-cdn-20657.b0.upaiyun.com/2018/09/11/a7591b0c407106c5801e208987ba7a53.jpg";
                        Glide.with(getContext()).load(url).into(myHolder.imageViews[j]);
                    }
                }
            }else {
                for (int g=0;g<4;g++){
                    String url="http://bmob-cdn-20657.b0.upaiyun.com/2018/09/11/a7591b0c407106c5801e208987ba7a53.jpg";
                    Glide.with(getContext()).load(url).into(myHolder.imageViews[g]);
                }
            }
            myHolder.textView_title.setText(news.getTitle());
            myHolder.textView_time.setText(news.getPublishDateStr());
            myHolder.textView_author.setText(news.getPosterScreenName());
            return view;
        }

        private class MyHolder {
            TextView textView_title;
            ImageView[] imageViews = new ImageView[4];
            TextView textView_author;
            TextView textView_time;
        }

    }




}
