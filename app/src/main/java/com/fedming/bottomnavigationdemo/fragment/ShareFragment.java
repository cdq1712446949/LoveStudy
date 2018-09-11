package com.fedming.bottomnavigationdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.fedming.bottomnavigationdemo.activity.LoginActivity;
import com.fedming.bottomnavigationdemo.R;
import com.flyco.dialog.listener.OnBtnClickL;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.fedming.bottomnavigationdemo.model.Document;
import com.fedming.bottomnavigationdemo.utils.Prompt;

@SuppressLint("ValidFragment")
public class ShareFragment extends Fragment {

    private boolean isLogin;
    private TextView text_path;
    private Spinner spinner;
    private Button button_path;
    private Button button_sure;
    private Button button_notlogin;
    private String url;
    private String filename;
    BmobFile file;

    @SuppressLint("ValidFragment")
    public ShareFragment(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bmob.initialize(getContext(), Prompt.APPID);
        View view;
        if (isLogin) {
            view = inflater.inflate(R.layout.fragment_share, null);
            findControl(view);
            String[] strings = {"艺术", "工学", "文学", "机械", "医学", "金融", "葡萄酒"};
            @SuppressLint("ResourceType") ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, strings);
            spinner.setAdapter(arrayAdapter);
            button_path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //intent.setType(“image/*”);//选择图片
                    //intent.setType(“audio/*”); //选择音频
                    //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                    //intent.setType(“video/*;image/*”);//同时选择视频和图片
                    intent.setType("*/*");//无类型限制
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                }
            });
            button_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (text_path.getText().toString().equals("")){
                        return;
                    }
                    final ProgressDialog dialog=new ProgressDialog(getContext());
                    dialog.setTitle("分享");
                    dialog.setMessage("上传中");
                    dialog.show();
                    final boolean[] b = {false};
                    final Document document = new Document();
                    file = new BmobFile(new File(text_path.getText().toString()));
                    file.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                url = file.getFileUrl();
                                Log.i("----url--->",url);
                                filename = file.getFilename();
                                Log.i("----filename-->",filename);
                                b[0] =true;
                                if (b[0]){
                                    document.setAuthor(LoginActivity.user.getName());
                                    document.setType(spinner.getSelectedItem().toString());
                                    document.setWendang(url);
                                    document.setName(filename);
                                    document.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Log.i("---document.save-->", "success");
                                                dialog.dismiss();
                                                Prompt.NormalDialogOneBtn(getContext(), "分享成功", "确定", new OnBtnClickL() {
                                                    @Override
                                                    public void onBtnClick() {
                                                        Prompt.normalDialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                Log.i("--msg1---", e.getMessage());
                                                Prompt.NormalDialogOneBtn(getContext(), "分享失败，请检查网络", "确定", new OnBtnClickL() {
                                                    @Override
                                                    public void onBtnClick() {
                                                        Prompt.normalDialog.dismiss();
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                            } else {
                                Log.i("---msg3--->", e.getMessage());
                            }
                        }
                    });

                }
            });
        } else {
            view = inflater.inflate(R.layout.fragment_share_notlogin, null);
            button_notlogin = (Button) view.findViewById(R.id.share_button_notlogin);
            button_notlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }
        return view;

    }

    String path;

    private void findControl(View view) {
        text_path = (TextView) view.findViewById(R.id.share_text_path);
        spinner = (Spinner) view.findViewById(R.id.share_spinner_zhuanye);
        button_path = (Button) view.findViewById(R.id.share_button_path);
        button_sure = (Button) view.findViewById(R.id.share_button_share);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                text_path.setText(path);
//                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(getContext(), uri);
                text_path.setText(path);
//                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                text_path.setText(path);
//                Toast.makeText(MainActivity, path + "222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}


