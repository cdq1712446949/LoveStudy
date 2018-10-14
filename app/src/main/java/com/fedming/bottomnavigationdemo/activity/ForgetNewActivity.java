package com.fedming.bottomnavigationdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.R;
import com.fedming.bottomnavigationdemo.model.User;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetNewActivity extends AppCompatActivity {

    private EditText text_password;
    private EditText text_password2;
    private Button button_sure;
    private String password;
    private String passwordtwo;
    private String phone;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_new);
        findControl();

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        id=intent.getStringExtra("id");
        System.out.println(id);

        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=text_password.getText().toString();
                passwordtwo=text_password2.getText().toString();
                if (!passwordtwo.equals(password)){
                    Toast.makeText(ForgetNewActivity.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                    return;
                }
                final User user=new User();
                user.setPd(password);
                user.update(id,new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(ForgetNewActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(ForgetNewActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    //实例化所有控件
    private void findControl(){
        text_password=(EditText)findViewById(R.id.forgetnew_password);
        text_password2=(EditText)findViewById(R.id.forgetnew_passwordtwo);
        button_sure=(Button)findViewById(R.id.forgetnew_sure);
    }

}
