package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.Bmob_date.User;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.ProgressButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static android.view.KeyEvent.KEYCODE_BACK;
import static com.zhangsong.com.signup_system.fragment.ManiMyInfoFragment.btn_finish_login;

public class LoginActivity extends MySwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    public static LoginActivity instance = null;
    private RelativeLayout rl_login_show;
    private TextView tv_go_register,tv_forget_pwd; //点击登录+去注册+忘记密码+手机号注册
    private EditText edit_login_usernameOrPhone,edit_login_password;//登录用户名和密码
    private ImageView iv_login_wx,iv_login_qq; //微信登录+QQ登录
    ProgressButton pb_button_login;
    private ProgressButton ptn_login;
    private String userNameOrPhone;
    private String password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        initView();//初始化View
    }

    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        //主父局
        rl_login_show= (RelativeLayout) findViewById(R.id.rl_login_show);
        rl_login_show= (RelativeLayout) findViewById(R.id.rl_login_show);
        //点击登录
        pb_button_login= (ProgressButton) findViewById(R.id.ptn_login);
        pb_button_login.setBgColor(Color.RED);
        pb_button_login.setTextColor(Color.WHITE);
        pb_button_login.setProColor(Color.WHITE);
        pb_button_login.setButtonText("登  录");
        ptn_login= (ProgressButton) findViewById(R.id.ptn_login);
        ptn_login.setOnClickListener(this);
        //去注册
        tv_go_register= (TextView) findViewById(R.id.tv_go_register);
        tv_go_register.setOnClickListener(this);
        //微信登录
        iv_login_wx= (ImageView) findViewById(R.id.iv_login_wx);
        iv_login_wx.setOnClickListener(this);
        //QQ登录
        iv_login_qq= (ImageView) findViewById(R.id.iv_login_qq);
        iv_login_qq.setOnClickListener(this);
        //忘记密码
        tv_forget_pwd= (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(this);
        //登录用户名和密码
        edit_login_usernameOrPhone = (EditText) findViewById(R.id.edit_login_usernameOrPhone);
        edit_login_password= (EditText) findViewById(R.id.edit_login_password);
    }
    /**
     * 软键盘的自动弹出
     * @param editText
     */
    private void showKeyboard(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    /**
     * 软键盘的关闭
     */
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击登录
            case R.id.ptn_login:
                closeKeyboard();
                userNameOrPhone = edit_login_usernameOrPhone.getText().toString();
                password = edit_login_password.getText().toString();
                doLogin(userNameOrPhone,password);
                break;
            //去注册
            case R.id.tv_go_register:
                startActivity(new Intent(LoginActivity.this,PhoneRegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(LoginActivity.this,ResetPwdActivity.class));
                break;
            //微信登录
            case R.id.iv_login_wx:
                DoWechat();
                break;
            //QQ登录
            case R.id.iv_login_qq:
                DoQQ();
                break;
        }
    }

    /**
     * Bmob用户名密码登录
     */
    private void doLogin(String userNameOrPhone,String password) {
        if(userNameOrPhone.isEmpty()&&password.isEmpty()){
            Snackbar snackbar = Snackbar.make(rl_login_show,  R.string.please_input_user_name_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (userNameOrPhone.isEmpty()) {
            Snackbar snackbar = Snackbar.make(rl_login_show,  R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (password.isEmpty()) {
            Snackbar snackbar = Snackbar.make(rl_login_show,   R.string.please_input_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }

        pb_button_login.startAnim();
        Message m=mHandler.obtainMessage();
        mHandler.sendMessageDelayed(m,1500);
    }
    /**
     * QQ登录
     */
    public void DoQQ(){
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        plat.removeAccount(false); //移除授权状态和本地缓存，下次授权会重新授权
        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(new PlatformActionListener() {//授权回调监听，监听oncomplete，onerror，oncancel三种状态
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry next = (Map.Entry) iterator.next();
                    Object key = next.getKey();
                    Object value = next.getValue();
                    Log.d("哈哈", "onComplete: "+key+"  "+value);

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("哈哈", "onError: "+platform.getName()+" "+platform.getDb()+"  "+"  "+platform.getId()+"  "+platform.getPlatformActionListener()+" "+platform.getVersion());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("哈哈", "onCancel: "+platform.getName());
            }
        });


        if (plat.isClientValid()) {
            //todo 判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if (plat.isAuthValid()) {
            //todo 判断是否已经存在授权状态，可以根据自己的登录逻辑设置
            Toast.makeText(this, "已经授权过了", Toast.LENGTH_SHORT).show();
            return;
        }
        //要功能，不要数据
        // plat.authorize();
        //要数据不要功能，主要体现在不会重复出现授权界面
        plat.showUser(null);
    }
    /**
     * 微信登录
     */
    private void DoWechat() {
        Platform wechat= ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(i==Platform.ACTION_USER_INFOR){

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        wechat.authorize();
    }

    /**
     * 登录转场
     */
    private Handler mHandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_button_login.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    UiTools.showSimpleLD(LoginActivity.this,getString(R.string.loading_login));
                    userNameOrPhone = edit_login_usernameOrPhone.getText().toString();
                    password = edit_login_password.getText().toString();
                    //  用户名/手机号和密码登录
                    StartUsernameOrPhonePassword(userNameOrPhone ,password);
                }
            });

        }
    };

    /**
     * 手机号和密码登录
     */
    private void StartUsernameOrPhonePassword(String userNameOrPhone, String password) {
        User Myuser = new User();
        Myuser.setUsername(userNameOrPhone);
        Myuser.setPassword(password);
        Myuser.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e){
                UiTools.closeSimpleLD();
                if(e==null){//登录成功
                    btn_finish_login.setText("退出登录");
                    Snackbar snackbar = Snackbar.make(rl_login_show,  R.string.login_successful, Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    LoginActivity.this.setResult(RESULT_OK);
                    LoginActivity.this.finish();
                } else{  //登录失败
                    if (e.getErrorCode() == 101) {
                        Snackbar snackbar = Snackbar.make(rl_login_show,R.string.login_error_name_or_password_incorrect, Snackbar.LENGTH_LONG);
                        //设置Snackbar上提示的字体颜色
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                    if (e.getErrorCode()==9016){
                        Snackbar snackbar = Snackbar.make(rl_login_show, R.string.networkConnError, Snackbar.LENGTH_LONG);
                        //设置Snackbar上提示的字体颜色
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                }
            }
        });
    }

    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
    /**
     *返回键执行返回上一页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
        }
        return super.onKeyUp(keyCode, event);
    }
}
