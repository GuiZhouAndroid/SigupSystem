package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.Bmob_date.PersonSign;
import com.zhangsong.com.signup_system.R;


import com.zhangsong.com.signup_system.Bmob_date.User;
import com.zhangsong.com.signup_system.utils.Permission.LogUtils;
import com.zhangsong.com.signup_system.utils.StatusBar.StatusBarUtils;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.ProgressButton;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.view.KeyEvent.KEYCODE_BACK;

public class RegisterActivity extends MySwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private TextView tv_go_login;//去登录
    private ProgressButton pb_button_register;
    private ProgressButton ptn_register;
    //注册用户名+密码+确认密码
    private EditText edit_register_username,edit_register_password,edit_register_true_password;
    private String name;
    private String pwd;
    public static RegisterActivity instance = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance = this;
        initView();//初始化View

    }
    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        //点击注册
        pb_button_register= (ProgressButton) findViewById(R.id.ptn_register);
        pb_button_register.setBgColor(Color.RED);
        pb_button_register.setTextColor(Color.WHITE);
        pb_button_register.setProColor(Color.WHITE);
        pb_button_register.setButtonText("注  册");
        ptn_register= (ProgressButton) findViewById(R.id.ptn_register);
        ptn_register.setOnClickListener(this);
        //去登陆
        tv_go_login= (TextView) findViewById(R.id.tv_go_login);
        tv_go_login.setOnClickListener(this);
        edit_register_username= (EditText) findViewById(R.id.edit_register_username);//注册用户名
        edit_register_password= (EditText) findViewById(R.id.edit_register_password);//注册密码
        edit_register_true_password= (EditText) findViewById(R.id.edit_register_true_password);//注册确认密码
    }
    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ptn_register:  //点击注册
                doRegister();
                break;
            case R.id.tv_go_login:  //去登录
                finish();
                break;
        }
    }
    /**
     * 点击注册
     */
    private void doRegister() {
        name = edit_register_username.getText().toString();
        pwd = edit_register_password.getText().toString();
        String pwdConfirm = edit_register_true_password.getText().toString();
        if (name.isEmpty()) {//判断用户为空
            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.user_name_is_not_empty);
            dialogPrompt.show();
            return;
        }
        if (pwd.isEmpty()) {//判断密码为空
            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.please_input_password);
            dialogPrompt.show();
            return;
        }
        if (!pwd.equals(pwdConfirm)) {//判断确认密码为空
            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.password_new_different);
            dialogPrompt.show();
            return;
        }
        pb_button_register.startAnim();
        Message m=mHandler.obtainMessage();
        mHandler.sendMessageDelayed(m,1500);
    }
    /**
     * 注册转场
     */
    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            pb_button_register.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    UiTools.showSimpleLD(RegisterActivity.this,getString(R.string.loading_register));
                    //填写注册信息
                    User user = new User();
                    user.setUsername(name);//注册用户名
                    user.setPassword(pwd);//注册密码
                    user.setSex("保密");//性别
                    user.setAge("保密");//年龄
                    user.setSignature(getString(R.string.signature));//个性签名
                    //提交注册
                    user.signUp(new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            UiTools.closeSimpleLD();
                            if(e==null){//注册成功
                                DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.register_ok, 3);
                                dialogPrompt.showAndFinish(RegisterActivity.this);

                            }else {//注册失败
                                // LogUtils.i(TAG, new Throwable(), e.getErrorCode() + ":" + e.getMessage());
                                if (e.getErrorCode() == 202) {
                                    DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.register_error_user_already_taken);
                                    dialogPrompt.show();
                                } else {
                                    DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this, getString(R.string.register_error) + "，错误码：" + e.getErrorCode() + "," + e.getMessage());
                                    dialogPrompt.show();
                                }
                            }
                        }
                    });
                }
            });

        }
    };
    /**
     *返回键执行返回上一页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KEYCODE_BACK){
            overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }
}
