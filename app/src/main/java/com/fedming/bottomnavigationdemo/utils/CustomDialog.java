package com.fedming.bottomnavigationdemo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.R;

public class CustomDialog {

    public void showLayoutDialog(final Context context,String s) {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout,null);
        TextView dialogText = (TextView) dialogView.findViewById(R.id.dialog_text);
        Button dialogBtnConfirm = (Button) dialogView.findViewById(R.id.dialog_btn_confirm);
        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(context);
        layoutDialog.setTitle(R.string.dialog_custom_layout_text);
        layoutDialog.setView(dialogView);
        //设置组件
        dialogText.setText(s);
        dialogBtnConfirm .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutDialog.create().show();
    }
}
