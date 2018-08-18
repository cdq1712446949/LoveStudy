package com.fedming.bottomnavigationdemo.fourfragment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fedming.bottomnavigationdemo.BrowserActivity;
import com.fedming.bottomnavigationdemo.OpenDocumentAcitivity;
import com.fedming.bottomnavigationdemo.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import daocumentlist.Document;
import functionprompt.Prompt;

/**
 * 首页
 */
public class HomeFragment extends Fragment {

    private ListView listView;
    private ArrayList<Document> documentlist = new ArrayList<Document>();
    private int size=0;
    private int listsize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        Bmob.initialize(getActivity(), Prompt.APPID);
        findControl(view);
        getDocument();
        return view;
    }

    private void findControl(View view) {
        listView = (ListView) view.findViewById(R.id.docunment_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("---tag--->","item");
                Log.i("-----position--->",String.valueOf(position));
                Intent intent = new Intent(getContext(), OpenDocumentAcitivity.class);
                Log.i("-----url----->",documentlist.get(position).getWendang());
                intent.putExtra("url",documentlist.get(position).getWendang());
                startActivity(intent);
            }
        });
    }

    private void getDocument() {
        BmobQuery<Document> query = new BmobQuery<Document>();
        query.findObjects(new FindListener<Document>() {
            @Override
            public void done(List<Document> list, BmobException e) {
                if (e == null) {
                    listsize=list.size();
                    for (int i = 0; i < list.size(); i++) {
                        Document document = new Document();
                        document = list.get(i);
                        documentlist.add(document);
                    }
                } else {
                    Log.i("-----Massage--->", e.getMessage());
                }
                Log.i("-----documentsize--->", String.valueOf(documentlist.size()));
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

            Log.i("-----setView----->","setView");
            final ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(getContext(),R.layout.list_item,null);
//                holder.button_down=(ImageButton)convertView.findViewById(R.id.document_down);
//                holder.button_down.setBackground(getResources().getDrawable(R.drawable.cai_no));
                holder.button_up=(ImageButton)convertView.findViewById(R.id.document_up);
                holder.text_author=(TextView)convertView.findViewById(R.id.document_author);
                holder.text_date=(TextView)convertView.findViewById(R.id.document_date);
                holder.text_name=(TextView)convertView.findViewById(R.id.docunment_name);
                holder.text_up=(TextView)convertView.findViewById(R.id.text_up);
//                holder.text_down=(TextView)convertView.findViewById(R.id.text_down);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            Document item;
            item = (Document) getItem(position);
            holder.text_name.setText(item.getName());
            holder.text_date.setText(item.getCreatedAt());
            holder.text_author.setText(item.getAuthor());
            holder.text_up.setText(String.valueOf(item.getUp()));
//            holder.text_down.setText(String.valueOf(item.getDown()));
            holder.button_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("-----Click--->","Click");
                    if (holder.button_up.getBackground()==getResources().getDrawable(R.drawable.zan_no)){
                        holder.button_up.setBackground(getResources().getDrawable(R.drawable.zan_yes));
                    }
                }
            });
            return convertView;
        }
    }

    private static class ViewHolder{
        TextView text_name;
        TextView text_author;
        TextView text_date;
        TextView text_up;
//        TextView text_down;
        ImageButton button_up;
//        ImageButton button_down;
    }

}
