package com.fedming.bottomnavigationdemo.fourfragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fedming.bottomnavigationdemo.R;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.internal.util.ScalarSynchronousSingle;

public class DynamicFragment extends Fragment {

    private List<News> newsList=new ArrayList<>();

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dongtai, null);
        listView=view.findViewById(R.id.list_dongtai);

        Thread thread = new Thread(new MyThread());
        thread.start();

        listView.setAdapter(new MyAdapter());

        return view;
    }

    private class MyAdapter extends BaseAdapter{
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

            final MyHolder myHolder;

            if (view==null){
                myHolder=new MyHolder();
                view=View.inflate(getContext(),R.layout.item_news,null);
                myHolder.imageViews[0]=view.findViewById(R.id.image1);
                myHolder.imageViews[1]=view.findViewById(R.id.image2);
                myHolder.imageViews[2]=view.findViewById(R.id.image3);
                myHolder.imageViews[3]=view.findViewById(R.id.image4);
                myHolder.textView_author=view.findViewById(R.id.author);
                myHolder.textView_time=view.findViewById(R.id.time);
                myHolder.textView_title=view.findViewById(R.id.textView2);
                view.setTag(myHolder);
            }else{
                myHolder=(MyHolder)view.getTag();
            }
            News news=newsList.get(i);
//            String[] s=news.getImageArray();
            String url= "http://p6.qhimg.com/t01a72ae6c03de2c775.jpg?size=640x290";
            Glide.with(getContext()).load(url).into(myHolder.imageViews[0]);
//            System.out.println(s[0]);
//            if (s.length!=0){
//                for (int w=0;i<s.length;i++){
//                    Glide.with(getContext()).load(s[i]).into(myHolder.imageViews[i]);
//                }
//            }else {
//                System.out.println("该条新闻没有配图");
//            }
            myHolder.textView_title.setText(news.getTitle());
            myHolder.textView_time.setText(news.getPublishDateStr());
            myHolder.textView_author.setText(news.getPosterScreenName());
            return view;
        }

        private class MyHolder{
            TextView textView_title;
            ImageView[] imageViews=new ImageView[4];
//            ImageView imageView2;
//            ImageView imageView3;
//            ImageView imageView4;
            TextView textView_author;
            TextView textView_time;
        }

    }

    //在子线程中加载网络数据
    public class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("开始加载网络数据......");
                newsList = GetNews.getNews();
                System.out.println("加载完成");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
