package com.fedming.bottomnavigationdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.fedming.bottomnavigationdemo.R;
import com.fedming.bottomnavigationdemo.customcontrols.RoundImageView;
import com.fedming.bottomnavigationdemo.model.User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.fedming.bottomnavigationdemo.utils.CustomDialog;
import com.fedming.bottomnavigationdemo.utils.Prompt;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    public static User user;

    private Button button_login;
    private TextView textView_phone;
    private EditText text_passdord;
    private RoundImageView imageView_head;
    private TextView textView_regist;
    private TextView textView_forget;
    private CustomDialog customDialog;
    private boolean login = false;
    private Uri uri;
    private long mExitTime;
    private ProgressDialog dialog;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView_phone = (TextView) findViewById(R.id.phone);
        text_passdord = (EditText) findViewById(R.id.password);
        imageView_head = (RoundImageView) findViewById(R.id.headphoto);

        //初始化bmob
        Bmob.initialize(this, Prompt.APPID);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//
//        File file = new File(PathGetter.getPath(this, uri));
//        BmobFile bmobFile = new BmobFile(file);
//
//        User user=new User();
//        user.setPhone("980814");
//        user.setPd("980814");
//        user.setName("test");
//        user.setImageHead(bmobFile);
//        user.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e==null){
//                    Toast.makeText(LoginActivity.this,"成功",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        //为登录按钮添加监听
        button_login = (Button) findViewById(R.id.sign_in_button);
        button_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textView_phone.getText().toString();
                String password = text_passdord.getText().toString();
                Login(username, password);
            }
        });

        //为注册添加监听
        textView_regist = (TextView) findViewById(R.id.regist);
        textView_regist.setClickable(true);
        textView_regist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });

        //为忘记密码添加监听
        textView_forget = (TextView) findViewById(R.id.forget);
        textView_forget.setClickable(true);
        textView_forget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                forget();
            }
        });

    }

    //登录操作
    private boolean Login(final String username, final String password) {

        if (textView_phone.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_LONG).show();
            return false;
        }

        if (text_passdord.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            return false;
        }
        dialog = new ProgressDialog(LoginActivity.this );
        dialog.setMessage("登录中");
        dialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
//        query.addWhereEqualTo(username,new User())
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPhone().equals(username)) {
                            if (list.get(i).getPd().equals(password)) {
                                login=true;
                                user=list.get(i);
                                Log.i("-----登陆提示5--->", "登陆成功");
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("islogin",login);
                                startActivity(intent);
                                finish();
                            } else {

                            }
                        }
                    }
                   if (!login){
                       Log.i("----登陆提示6---->", "登陆失败");
                       Toast.makeText(LoginActivity.this, "登陆失败，账号不存在或者密码错误!", Toast.LENGTH_LONG).show();
                       dialog.dismiss();
                       return;
                   }
                } else {
                    Log.i("----错误提示8---->", e.getMessage());
                }
            }
        });

        return login;
    }

    //跳转到注册界面
    private void regist() {
        startActivityForResult(new Intent(LoginActivity.this,RegistActivity.class),1);
    }


    //跳转到忘记密码界面
    private void forget() {
        startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
        }
    }
}

