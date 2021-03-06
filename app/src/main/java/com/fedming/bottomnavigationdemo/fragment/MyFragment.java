package com.fedming.bottomnavigationdemo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.codbking.view.ItemView;
import com.fedming.bottomnavigationdemo.activity.ChangePhoneAcitivity;
import com.fedming.bottomnavigationdemo.activity.HomeActivity;
import com.fedming.bottomnavigationdemo.activity.LoginActivity;
import com.fedming.bottomnavigationdemo.R;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import com.fedming.bottomnavigationdemo.model.User;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

@SuppressLint("ValidFragment")
public class MyFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    public static BaseAnimatorSet mBasIn;
    public static BaseAnimatorSet mBasOut;
    ExpandableListView mElv;
    private boolean isLogin = false;
    private ImageView imagehead;
    private ImageView imageback;
    private TextView textPhone;
    private TextView textName;
    private User user;
    private ItemView itemViewName;
    private ItemView itemViewSex;
    private ItemView itemViewQianming;
    private ItemView itemViewEmail;
    private ItemView itemViewVersion;
    private ItemView itemViewKefu;
    private ItemView itemViewPhone;
    private Button notlogin;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bmob.initialize(getActivity(),Prompt.APPID);
        if (isLogin) {
            user = LoginActivity.user;
            View view = inflater.inflate(R.layout.fragment_my, null);
            findControl(view);
            setClickListener();
            setText(user.getName(), user.getPhone());
            String imageurl = user.getImageHead();
            if (imageurl.equals("")) {
               Toast.makeText(getActivity(),"头像地址为空",Toast.LENGTH_LONG).show();
            }else{
                //设置背景磨砂效果
                Glide.with(this).load(imageurl)
                        .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                        .into(imageback);
                //设置圆形图像
                Glide.with(this).load(imageurl)
                        .bitmapTransform(new CropCircleTransformation(getActivity()))
                        .into(imagehead);
            }
            imagehead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                }
            });
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_my_notlogin, null);
            Button button = (Button) view.findViewById(R.id.button_login);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
            return view;
        }

    }

    @SuppressLint("ValidFragment")
    public MyFragment(boolean islogin) {
        this.isLogin = islogin;
    }


    private void findControl(View view) {
        imagehead = (ImageView) view.findViewById(R.id.h_head);
        imageback = (ImageView) view.findViewById(R.id.h_back);
        textName = (TextView) view.findViewById(R.id.user_name);
        textPhone = (TextView) view.findViewById(R.id.user_val);
        itemViewEmail = (ItemView) view.findViewById(R.id.item_email);
        itemViewEmail.setText(user.getEmail());
        itemViewName = (ItemView) view.findViewById(R.id.item_name);
        itemViewName.setText(user.getName());
        itemViewSex = (ItemView) view.findViewById(R.id.item_sex);
        itemViewSex.setText(user.getSex());
        itemViewVersion = (ItemView) view.findViewById(R.id.item_banben);
        itemViewPhone = (ItemView) view.findViewById(R.id.item_phone);
        itemViewPhone.setText(user.getPhone());
        itemViewKefu = (ItemView) view.findViewById(R.id.item_kefu);
        itemViewQianming = (ItemView) view.findViewById(R.id.item_qianming);
        itemViewQianming.setText(user.getQianming());
        notlogin = (Button) view.findViewById(R.id.notlogin);
    }

    private void setClickListener() {
        itemViewName.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                alert_edit(v);
            }
        });
        itemViewSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialogNoTitle();
            }
        });
        itemViewQianming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入签名")
                        .setView(et)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                String qianming = et.getText().toString();
                                itemViewQianming.setText(qianming);
                                final User username = new User();
                                username.setQianming(qianming);
                                username.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        itemViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleOne(getActivity(), "是否更换手机号");
            }
        });
        notlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("退出中");
                dialog.show();
                LoginActivity.user = null;
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("islogin", false);
                startActivity(intent);
            }
        });
    }

    //是否执行操作对话框
    public void NormalDialogStyleOne(Context context, String s) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.content(s)//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        goChange();
                        dialog.dismiss();
                    }
                });
    }

    //输入对话框
    public void alert_edit(View view) {
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入昵称")
                .setView(et)
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String name = et.getText().toString();
                        itemViewName.setText(name);
                        final User username = new User();
                        username.setName(name);
                        username.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton("取消", null).show();
    }

    //底部单选对话框
    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"男", "女"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, mElv);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    itemViewSex.setText("男");
                    final User usersex = new User();
                    usersex.setSex("男");
                    usersex.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
                if (position == 1) {
                    itemViewSex.setText("女");
                    final User usersex = new User();
                    usersex.setSex("女");
                    usersex.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void setText(String name, String phone) {
        textName.setText(name);
        textPhone.setText(phone);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    private void goChange() {
        startActivity(new Intent(getActivity(), ChangePhoneAcitivity.class));
//        getActivity().finish();
    }
}
