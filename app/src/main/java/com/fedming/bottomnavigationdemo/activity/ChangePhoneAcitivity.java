package com.fedming.bottomnavigationdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fedming.bottomnavigationdemo.R;
import com.flyco.dialog.listener.OnBtnClickL;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import com.fedming.bottomnavigationdemo.utils.Prompt;

/**
 * @author cdq created on 2018.9.11
 * 该类用来显示更改手机号页面
 */

public class ChangePhoneAcitivity extends AppCompatActivity {

    private EditText text_yanzheng;
    private Button button_yanzheng;
    private Button button_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        setTitle("解除绑定");
        findControl();

        BmobSMS.initialize(ChangePhoneAcitivity.this,Prompt.APPID);

        //发送验证码
        button_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Prompt.sendMassage(ChangePhoneAcitivity.this,LoginActivity.user.getPhone(),button_yanzheng);
            }
        });

        //确定按钮监听
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.verifySmsCode(ChangePhoneAcitivity.this, LoginActivity.user.getPhone(), text_yanzheng.getText().toString(), new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            startActivity(new Intent(ChangePhoneAcitivity.this,BangdingAcitivity.class));
                            finish();
                        }else {
                            Prompt.NormalDialogOneBtn(ChangePhoneAcitivity.this, "验证码错误", "确定", new OnBtnClickL() {
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
