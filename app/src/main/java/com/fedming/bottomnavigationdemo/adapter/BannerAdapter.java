package com.fedming.bottomnavigationdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fedming.bottomnavigationdemo.R;

import java.util.List;


public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<Integer> list;
    private Context context;

    private boolean isClear = false;

    public BannerAdapter(Context context, List<Integer> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("---method--->", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("---method-->", "onBindViewHoleder");
        Log.i("--position-->", String.valueOf(position));
        if (position > (list.size() - 1)) {
            list.clear();
        } else {
            //加载图片资源到imageView
            Glide.with(context).load(list.get(position % list.size())).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        //加载轮播图每一页layout
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
        }

    }


}
