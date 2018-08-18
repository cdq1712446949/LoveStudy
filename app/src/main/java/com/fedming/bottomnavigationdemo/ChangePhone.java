package com.fedming.bottomnavigationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flyco.dialog.listener.OnBtnClickL;

import Login.User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import functionprompt.Prompt;

public class ChangePhone extends AppCompatActivity {

    private EditText text_yanzheng;
    private Button button_yanzheng;
    private Button button_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        setTitle("解除绑定");
        findControl();

        BmobSMS.initialize(ChangePhone.this,Prompt.APPID);

        //发送验证码
        button_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Log.i("----phone--->",LoginActivity.user.getPhone());
                Prompt.sendMassage(ChangePhone.this,LoginActivity.user.getPhone(),button_yanzheng);
            }
        });

        //确定按钮监听
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.verifySmsCode(ChangePhone.this, LoginActivity.user.getPhone(), text_yanzheng.getText().toString(), new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            startActivity(new Intent(ChangePhone.this,BangdingAcitivity.class));
                            finish();
                        }else {
                            Prompt.NormalDialogOneBtn(ChangePhone.this, "验证码错误", "确定", new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    Prompt.normalDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    private void findControl(){
        text_yanzheng=(EditText)findViewById(R.id.change_yanzheng);
        button_sure=(Button)findViewById(R.id.change_button_sure);
        button_yanzheng=(Button)findViewById(R.id.change_button_yanzheng);
    }

}
