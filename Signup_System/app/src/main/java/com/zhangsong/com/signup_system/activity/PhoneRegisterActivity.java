package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.Bmob_date.User;
import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.CountDown.CountDownButtonHelper;
import com.zhangsong.com.signup_system.utils.RegularExpression.Validator;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.ProgressButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static android.view.KeyEvent.KEYCODE_BACK;

public class PhoneRegisterActivity extends MySwipeBackActivity implements View.OnClickListener {
    private RelativeLayout rl_phone_show;//父布局。用于Snackbar
    private EditText edit_input_phone, edit_input_phone_password,edit_input_phone_code;//手机号+密码+验证码
    private Button btn_yzm;//点击获取验证码
    ProgressButton pb_button_yzm;
    private ProgressButton ptn_yzm;
    private TextView tv_go_login;//去登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        initView();
    }

    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        rl_phone_show= (RelativeLayout) findViewById(R.id.rl_phone_show);//父布局
        edit_input_phone=(EditText) findViewById(R.id.edit_input_phone);//输入的手机号
        edit_input_phone_password=(EditText) findViewById(R.id.edit_input_phone_password);//输入的密码
        edit_input_phone_code=(EditText) findViewById(R.id.edit_input_phone_code);//输入的验证码
        //获取验证码
        btn_yzm= (Button) findViewById(R.id.btn_yzm);
        btn_yzm.setOnClickListener(this);
        //点击验证登录
        pb_button_yzm= (ProgressButton) findViewById(R.id.ptn_yzm);
        pb_button_yzm.setBgColor(Color.RED);
        pb_button_yzm.setTextColor(Color.WHITE);
        pb_button_yzm.setProColor(Color.WHITE);
        pb_button_yzm.setButtonText("注  册");
        ptn_yzm= (ProgressButton) findViewById(R.id.ptn_yzm);
        ptn_yzm.setOnClickListener(this);
        //去登陆
        tv_go_login= (TextView) findViewById(R.id.tv_go_login);
        tv_go_login.setOnClickListener(this);
    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yzm:
                closeKeyboard();
                String strGetPassword=edit_input_phone_password.getText().toString();//EditText中输入的密码
                String strGetJudgePhone = edit_input_phone.getText().toString();//EditText中输入的手机号
                JudgePhone(strGetJudgePhone,strGetPassword);//判断手机号
                break;
            case R.id.ptn_yzm:
                closeKeyboard();
                String strGetPhone = edit_input_phone.getText().toString();//EditText中输入的手机号
                String strGetPassword2=edit_input_phone_password.getText().toString();//EditText中输入的密码
                String strGetPhoneCode=edit_input_phone_code.getText().toString();//EditText中输入的验证码
                VerificationLogin(strGetPhone,strGetPassword2,strGetPhoneCode);//验证登录
                break;
            case R.id.tv_go_login:  //去登录
                overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
                finish();
                break;

        }
    }
    /**
     * 判断手机号
     */
    private void JudgePhone(String strGetJudgePhone,String strGetPassword){
        //正则表达式 ——手机号正确
        if (Validator.isMobile(strGetJudgePhone)&&Validator.isPassword(strGetPassword)){
            CountDownButtonHelper helper = new CountDownButtonHelper(btn_yzm,"再次获取",60,1);
            helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener(){
                @SuppressLint("ResourceAsColor")
                @Override
                public void finish() {
                    btn_yzm.setText("再次获取");
                }
            });
            helper.start();//开始倒计时
            //手机号合法，开始获取验证码
            BmobSMS.requestSMSCode(strGetJudgePhone, "高新报名助手", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e==null){//获取成功
                        Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.SMS_Code_Get_Success, Snackbar.LENGTH_LONG);
                        //设置Snackbar上提示的字体颜色
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }else {
                        if (e.getErrorCode()==9015){
                            Toast.makeText(PhoneRegisterActivity.this, "获取失败,如果你看到这条提示，请务必联系开发者！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }); }
            else {
            DialogPrompt dialogPrompt = new DialogPrompt(PhoneRegisterActivity.this, R.string.please_input_Correct_Phone );
            dialogPrompt.show();
        }
    }
    /**
     * 验证登录
     * @param strGetPhone
     * @param strGetPassword
     * @param strGetPhoneCode
     */
    private void VerificationLogin(String strGetPhone,String strGetPassword,String strGetPhoneCode) {
        //判断手机号+密码+验证码同时为空
        if (TextUtils.isEmpty(strGetPhone)&&TextUtils.isEmpty(strGetPassword)&&TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_Pwd_And_SMS_Code_No_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断手机号非空+和密码和验证码同时为空
        if (!TextUtils.isEmpty(strGetPhone)&&TextUtils.isEmpty(strGetPassword)&&TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Pwd_And_SMS_Code_No_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断密码非空+手机号和验证码同时为空
        if (TextUtils.isEmpty(strGetPhone)&&!TextUtils.isEmpty(strGetPassword)&&TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_SMS_Code_No_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断验证码非空+手机号和密码同时为空
        if (TextUtils.isEmpty(strGetPhone)&&TextUtils.isEmpty(strGetPassword)&&!TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_Pwd_AndNo_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }

        //判断手机号+密码同时为空
        if (TextUtils.isEmpty(strGetPhone)&&TextUtils.isEmpty(strGetPassword)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_Pwd_AndNo_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断手机号+验证码同时为空
        if (TextUtils.isEmpty(strGetPhone)&&TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_SMS_Code_No_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断手机号为空
        if (TextUtils.isEmpty(strGetPhone)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.Phone_And_No_Null, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //判断密码为空
        if (TextUtils.isEmpty(strGetPassword)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.please_input_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //验证码为空
        if (TextUtils.isEmpty(strGetPhoneCode)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.please_input_Verification_code, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //手机号不正确
        if (!Validator.isMobile(strGetPhone)){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.please_input_Correct_Phone, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //验证码长度不满足6位数
        if (strGetPhoneCode.length()!=6){
            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.please_input_Correct_SMS_Code, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }

        pb_button_yzm.startAnim();
        Message m=mHandler.obtainMessage();
        mHandler.sendMessageDelayed(m,1500);
    }
    /**
     * 登录转场
     */
    private Handler mHandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_button_yzm.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    String strGetPhone = edit_input_phone.getText().toString();//EditText中输入的手机号
                    String strGetPassword=edit_input_phone_password.getText().toString();//EditText中输入的密码
                    String strGetPhoneCode=edit_input_phone_code.getText().toString();//EditText中输入的验证码
                    StartRegister(strGetPhone,strGetPassword,strGetPhoneCode);

                    /**
                     * 验证后一键登录
                     */
//                    String strGetPhone = edit_input_phone.getText().toString();//EditText中输入的手机号
//                    String strGetPhoneCode=edit_input_phone_code.getText().toString();//EditText中输入的验证码
//                    BmobUser.signOrLoginByMobilePhone(strGetPhone, strGetPhoneCode, new LogInListener<User>() {
//                        @Override
//                        public void done(User user, BmobException e) {
//                            if(user!=null){
//                                LoginActivity.instance.finish();//销毁登录界面
//                                startActivity(new Intent(PhoneRegisterActivity.this, MainActivity.class));//跳转到主页面
//                                finish();
//                                Toast.makeText(PhoneRegisterActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(PhoneRegisterActivity.this, "验证失败！", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            });
        }
    };

    /**
     * 开始注册
     */
    private void StartRegister(String strGetPhone,String strGetPassword,String strGetPhoneCode) {
        UiTools.showSimpleLD(PhoneRegisterActivity.this,getString(R.string.loading_register));
        //填写注册信息
            User user = new User();
            user.setMobilePhoneNumber(strGetPhone);//手机号
            user.setUsername(strGetPhone);//用户名位手机号码
            user.setPassword(strGetPassword);//密码
            user.setSex("保密");//性别
            user.setAge("保密");//年龄
            user.setSignature(getString(R.string.signature));//个性签名
            user.signOrLogin(strGetPhoneCode, new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    UiTools.closeSimpleLD();
                    if (e==null){//注册成功
                        DialogPrompt dialogPrompt = new DialogPrompt(PhoneRegisterActivity.this, R.string.register_ok, 3);
                        dialogPrompt.showAndFinish(PhoneRegisterActivity.this);
                    }else{//注册失败
                        DialogPrompt dialogPrompt = new DialogPrompt(PhoneRegisterActivity.this, R.string.register_error_user_already_taken);
                        dialogPrompt.show();
                        if(e.getErrorCode() == 9015){
                            Snackbar snackbar = Snackbar.make(rl_phone_show,  R.string.networkConnError, Snackbar.LENGTH_LONG);
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
     *返回键执行返回上一页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KEYCODE_BACK){
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }
}
