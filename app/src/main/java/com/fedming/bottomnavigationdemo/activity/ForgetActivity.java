package com.fedming.bottomnavigationdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import com.fedming.bottomnavigationdemo.R;
import com.fedming.bottomnavigationdemo.model.User;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fedming.bottomnavigationdemo.utils.Prompt;

public class ForgetActivity extends Activity {

    private Button button_yanzehng;
    private Button button_sure;
    private Button button_cancel;
    private EditText text_phone;
    private EditText text_yanzheng;

    private String phone;
    private String yanzehng;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_fragment);
        setTitle("找回密码");
        findControl();

        //初始化
        BmobSMS.initialize(ForgetActivity.this, Prompt.APPID);

        //确定按钮添加监听
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("确定按钮监听");
                phone = text_phone.getText().toString();
                yanzehng = text_yanzheng.getText().toString();
                if (phone.equals("")) {
                    Toast.makeText(ForgetActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
                    return;
                }
//                if (Prompt.isMobileNO(phone)){
//                    Toast.makeText(ForgetActivity.this,"手机号不符合规范",Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (yanzehng.equals("")) {
                    System.out.println(yanzehng);
                    Toast.makeText(ForgetActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                BmobSMS.verifySmsCode(ForgetActivity.this, phone, yanzehng, new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            System.out.println();
                            BmobQuery<User> query = new BmobQuery<User>();
                            query.addWhereEqualTo("phone", phone);
                            query.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, cn.bmob.v3.exception.BmobException e) {
                                    if (e == null) {
                                        for (int i = 0; i < list.size(); i++) {
                                            id = list.get(i).getObjectId();
                                            Toast.makeText(ForgetActivity.this, "验证通过", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ForgetActivity.this, ForgetNewActivity.class);
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("id", id);
                                            System.out.println(id + "    ididiidid");
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(ForgetActivity.this, "手机号不存在", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(ForgetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //发送验证码
        button_yanzehng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                phone = text_phone.getText().toString();
                Prompt.sendMassage(ForgetActivity.this, phone, button_yanzehng);//调用静态方法发送验证码
            }
        });

        //取消操作
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //实例化所有控件
    private void findControl() {
        button_cancel = (Button) findViewById(R.id.forget_button_cancel);
        button_sure = (Button) findViewById(R.id.forget_button_sure);
        button_yanzehng = (Button) findViewById(R.id.forget_button_yanzheng);
        text_phone = (EditText) findViewById(R.id.forget_edittext_phone);
        text_yanzheng = (EditText) findViewById(R.id.forget_text_yanzheng);
    }

}
