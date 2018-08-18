package com.fedming.bottomnavigationdemo.fourfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.codbking.view.ItemView;

public class MyItemView extends ItemView {
    public MyItemView(Context context) {
        super(context);
    }

    public MyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

    }
}
