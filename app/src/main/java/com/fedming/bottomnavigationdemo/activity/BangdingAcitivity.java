package com.fedming.bottomnavigationdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.R;
import com.flyco.dialog.listener.OnBtnClickL;

import java.util.List;

import com.fedming.bottomnavigationdemo.model.User;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import com.fedming.bottomnavigationdemo.utils.Prompt;

/**
 * @author cdq created on 2018.9.13
 * 该类是显示绑定手机号活动页面
 */

public class BangdingAcitivity extends AppCompatActivity {

    private EditText text_phone;
    private EditText text_yanzheng;
    private Button button_yanzheng;
    private Button button_sure;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangding_acitivity);
        findControl();

        //获取验证码
        button_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> query=new BmobQuery<User>();
                query.addWhereEqualTo("phone",text_phone.getText().toString());
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (list.size()!=0){
                            Prompt.NormalDialogOneBtn(BangdingAcitivity.this, "该手机号已注册", "确定", new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    Prompt.normalDialog.dismiss();
                                }
                            });
                            return;
                        }
                    }
                });
                if (Prompt.isMobileNO(text_phone.getText().toString())){
                    Prompt.sendMassage(BangdingAcitivity.this,text_phone.getText().toString(),button_yanzheng);
                    phone=text_phone.getText().toString();
                }else{
                    Prompt.NormalDialogOneBtn(BangdingAcitivity.this, "手机号不符合规范", "确定", new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            Prompt.normalDialog.dismiss();
                        }
                    });
                    return;
                }
            }
        });

        //确定按钮添加监听
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn.bmob.sms.BmobSMS.verifySmsCode(BangdingAcitivity.this, text_phone.getText().toString(), text_yanzheng.getText().toString(), new VerifySMSCodeListener() {
                    @Override
                    public void done(cn.bmob.sms.exception.BmobException e) {
                        if (e==null){
                            User user=new User();
                            user.setPhone(phone);
                            user.update(LoginActivity.user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(BangdingAcitivity.this,"绑定成功",Toast.LENGTH_LONG).show();
                                        Prompt.NormalDialogOneBtn(BangdingAcitivity.this, "软件将会重新启动", "确定", new OnBtnClickL() {
                                            @Override
                                            public void onBtnClick() {
                                                Prompt.normalDialog.dismiss();
                                            }
                                        });
                                        restartApp();
                                    }else {
                                        Toast.makeText(BangdingAcitivity.this,"绑定失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(BangdingAcitivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void findControl(){
        text_phone=(EditText)findViewById(R.id.bang_text_phone);
        text_yanzheng=(EditText)findViewById(R.id.bang_text_yanzheng);
        button_sure=(Button)findViewById(R.id.bang_button_sure);
        button_yanzheng=(Button)findViewById(R.id.bang_button_yanzheng);
    }

    /**
     * 重新启动App -> 杀进程,会短暂黑屏,启动慢
     */
    public void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
