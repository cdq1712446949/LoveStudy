package com.fedming.bottomnavigationdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import com.fedming.bottomnavigationdemo.R;
import com.fedming.bottomnavigationdemo.model.User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.fedming.bottomnavigationdemo.utils.Prompt;

public class RegistActivity extends Activity {

    private EditText text_phone;
    private EditText text_name;
    private EditText text_password;
    private EditText text_yanzheng;
    private Button regist_sure;
    private Button regist_cancel;
    private Button regist_yanzheng;

    private boolean isYanzhengt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        setTitle("注册");
        findControl();

        BmobSMS.initialize(RegistActivity.this, Prompt.APPID);
        Bmob.initialize(RegistActivity.this,Prompt.APPID);

        //确定按钮监听
        regist_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });

        //取消按钮监听
        regist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取验证码
        regist_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobSMS.requestSMSCode(RegistActivity.this, text_phone.getText().toString(), "随缘1998", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                        if (e == null) {//短信验证码已验证成功
                            regist_yanzheng.setText("发送成功");
                        }
                    }
                });
            }
        });

    }



    //实例化相对应的控件
    private void findControl() {
        text_name = (EditText) findViewById(R.id.regist_text_name);
        text_phone = (EditText) findViewById(R.id.regist_text_phone);
        text_password = (EditText) findViewById(R.id.regist_text_password);
        text_yanzheng = (EditText) findViewById(R.id.regist_text_yanzheng);
        regist_sure = (Button) findViewById(R.id.regist_button);
        regist_cancel = (Button) findViewById(R.id.regist_button_cancel);
        regist_yanzheng = (Button) findViewById(R.id.regist_button_yanzheng);
    }

    //注册确定
    private boolean regist() {
        boolean isRegist = false;
        boolean isName = false;
        boolean isPhone = false;
        boolean isPassword = false;
        boolean isFuhe = false;
        boolean isYanzheng = false;


        if (!text_name.getText().toString().equals("")) {
            isName = true;
        }
        if (!isName) {
            Toast.makeText(RegistActivity.this, "昵称不能为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!text_phone.getText().toString().equals("")) {
            isPhone = true;
        }
        if (!isPhone) {
            Toast.makeText(RegistActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (Prompt.isMobileNO(text_phone.getText().toString())) {
            isFuhe = true;
            BmobQuery<User> query=new BmobQuery<User>();
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getPhone().equals(text_phone.getText().toString())){
                            Toast.makeText(RegistActivity.this,"该账号已存在",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
            });
        }
        if (!isFuhe) {
            Toast.makeText(RegistActivity.this, "手机号不符合规范", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!text_password.getText().toString().equals("")) {
            isPassword = true;
        }
        if (!isPassword) {
            Toast.makeText(RegistActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!text_yanzheng.getText().toString().equals("")) {
            isYanzheng = true;
        }
        if (!isYanzheng) {
            Toast.makeText(RegistActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
        }

        User user = new User();
        user.setName(text_name.getText().toString());
        user.setPhone(text_phone.getText().toString());
        user.setPd(text_password.getText().toString());
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    BmobSMS.verifySmsCode(RegistActivity.this,text_phone.getText().toString(), text_yanzheng.getText().toString(), new VerifySMSCodeListener() {

                        @Override
                        public void done(cn.bmob.sms.exception.BmobException e) {
                            if(e==null){//短信验证码已验证成功
                                Log.i("bmob", "验证通过");
                                isYanzhengt =true;
                                if (isYanzhengt){
                                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }else{
                                Log.i("bmob", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                            }
                        }

                    });

                } else {
                    Toast.makeText(RegistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return isRegist;
    }



}
