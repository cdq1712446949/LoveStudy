package functionprompt;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.ForgetActivity;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;

import static com.fedming.bottomnavigationdemo.fourfragment.MyFragment.mBasIn;

public class Prompt {

    public static String APPID="bff50aa63ec8cf401bc519ebfe275070";
    public static String MSSTYLE="随缘1998";
    public static BaseAnimatorSet mBasOut;
    public static NormalDialog normalDialog;

    /**
     * 验证手机格式
     移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     联通：130、131、132、152、155、156、185、186
     电信：133、153、180、189、（1349卫通）
     加上 170号段。
     总结起来就是第一位必定为1，第二位必定为3或5、7或8，其他位置的可以为0-9
     */

    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (TextUtils.isEmpty(mobiles)) return false;

        else return mobiles.matches(telRegex);

    }

    public static void toAst(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    public static void  sendMassage(final Context context, String phone, final Button button){
        if (Prompt.isMobileNO(phone)){
            BmobSMS.requestSMSCode(context, phone, Prompt.MSSTYLE, new RequestSMSCodeListener() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e==null){
                        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000,button);
                        myCountDownTimer.start();
                        button.setText("发送成功");
                    }else {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(context,"手机号不符合规范",Toast.LENGTH_LONG).show();
        }
    }

    //一个button提示对话框
    public static void NormalDialogOneBtn(Context mContext,String content,String btntext,OnBtnClickL onBtnClickL) {
        normalDialog = new NormalDialog(mContext);
        normalDialog.content(content)//
                .btnNum(1)
                .btnText(btntext)//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();
        normalDialog.setOnBtnClickL(onBtnClickL);
    }

    //倒计时函数
    private static class MyCountDownTimer extends CountDownTimer {

        private Button button;

        public MyCountDownTimer(long millisInFuture, long countDownInterval,Button button) {
            super(millisInFuture, countDownInterval);
            this.button=button;
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            button.setClickable(false);
            button.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            button.setText("重新获取");
            //设置可点击
            button.setClickable(true);
        }
    }

}
