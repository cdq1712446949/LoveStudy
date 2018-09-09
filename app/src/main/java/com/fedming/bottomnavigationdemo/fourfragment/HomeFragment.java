package com.fedming.bottomnavigationdemo.fourfragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.OpenDocumentAcitivity;
import com.fedming.bottomnavigationdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import daocumentlist.Document;
import functionprompt.Prompt;

/**
 * @author cdq
 * 首页
 */
public class HomeFragment extends Fragment {

    private ListView listView;
    private ArrayList<Document> documentlist = new ArrayList<Document>();
    private int resize = 0;
    private int listsize;
    private List<Integer> list = new ArrayList<Integer>(4);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        Bmob.initialize(getActivity(), Prompt.APPID);
        findControl(view);

        list.add(R.drawable.b1);
        list.add(R.drawable.b2);
        list.add(R.drawable.b3);
        list.add(R.drawable.b4);


        BannerAdapter adapter = new BannerAdapter(getContext(), list);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        final SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size() * 10);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        final BannerIndicator bannerIndicator = (BannerIndicator) view.findViewById(R.id.indicator);
        bannerIndicator.setNumber(list.size());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = layoutManager.findFirstVisibleItemPosition() % list.size();
                    bannerIndicator.setPosition(i);
                }
            }
        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);

        getDocument();
        return view;
    }

    private void findControl(View view) {
        listView = (ListView) view.findViewById(R.id.docunment_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("---tag--->", "item");
                Log.i("-----position--->", String.valueOf(position));
                Intent intent = new Intent(getContext(), OpenDocumentAcitivity.class);
                Log.i("-----url----->", documentlist.get(position).getWendang());
                intent.putExtra("url", documentlist.get(position).getWendang());
                startActivity(intent);
            }
        });
    }

    private void getDocument() {
        BmobQuery<Document> query = null;
        try {
            query = new BmobQuery<Document>();
        } catch (Exception e) {
            Dialog dialog=new Dialog(getContext(),Dialog.BUTTON_NEGATIVE);
            dialog.setTitle("请连接网络");
            dialog.show();
        }
        query.findObjects(new FindListener<Document>() {
            @Override
            public void done(List<Document> list, BmobException e) {
                if (resize == list.size()) {
                    documentlist.clear();
                }
                if (e == null) {
                    listsize = list.size();
                    for (int i = 0; i < list.size(); i++) {
                        Document document = new Document();
                        document = list.get(i);
                        documentlist.add(document);
                    }
                } else {
                    Log.i("-----Massage--->", e.getMessage());
                }
                Log.i("-----documentsize--->", String.valueOf(documentlist.size()));
                resize = list.size();
                listView.setAdapter(new NewsAdapter());
            }
        });
    }

    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return documentlist.size();
        }

        @Override
        public Document getItem(int position) {
            return documentlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.i("-----setView----->", "setView");
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.list_item, null);
                holder.button_up = (ImageButton) convertView.findViewById(R.id.document_up);
                holder.text_author = (TextView) convertView.findViewById(R.id.document_author);
                holder.text_date = (TextView) convertView.findViewById(R.id.document_date);
                holder.text_name = (TextView) convertView.findViewById(R.id.docunment_name);
                holder.text_up = (TextView) convertView.findViewById(R.id.text_up);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Document item;
            item = (Document) getItem(position);
            holder.text_name.setText(item.getName());
            holder.text_date.setText(item.getCreatedAt());
            holder.text_author.setText(item.getAuthor());
            holder.text_up.setText(String.valueOf(item.getUp()));
            holder.button_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.button_up.setImageResource(R.drawable.zan_yes);
                }
            });
            if (position == documentlist.size()) {
                documentlist.clear();
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView text_name;
        TextView text_author;
        TextView text_date;
        TextView text_up;
        ImageButton button_up;
    }


}
