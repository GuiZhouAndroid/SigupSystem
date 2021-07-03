package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.CountDown.CountDownButtonHelper;
import com.zhangsong.com.signup_system.utils.RegularExpression.Validator;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.view.ProgressButton;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ResetPwdActivity extends MySwipeBackActivity implements View.OnClickListener{
    private RelativeLayout rl_reset_show;//父布局。用于Snackbar
    private EditText edit_input_resetPassword_phone,edit_input_new_password_reset,edit_input_code_reset;//手机号+密码+验证码
    private Button btn_yzm_reset;//点击获取验证码
    ProgressButton pb_button_yzm_reset;
    private ProgressButton ptn_yzm_reset;
    private TextView tv_go_login_reset;//去登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        initView();//初始化View
    }

    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        rl_reset_show= (RelativeLayout) findViewById(R.id.rl_reset_show);//父布局
        edit_input_resetPassword_phone=(EditText) findViewById(R.id.edit_input_resetPassword_phone);//输入的手机号
        edit_input_new_password_reset=(EditText) findViewById(R.id.edit_input_new_password_reset);//输入的新密码
        edit_input_code_reset=(EditText) findViewById(R.id.edit_input_code_reset);//输入的验证码
        //获取验证码
        btn_yzm_reset= (Button) findViewById(R.id.btn_yzm_reset);
        btn_yzm_reset.setOnClickListener(this);
        //点击验证登录
        pb_button_yzm_reset= (ProgressButton) findViewById(R.id.ptn_yzm_reset);
        pb_button_yzm_reset.setBgColor(Color.RED);
        pb_button_yzm_reset.setTextColor(Color.WHITE);
        pb_button_yzm_reset.setProColor(Color.WHITE);
        pb_button_yzm_reset.setButtonText("重  置");
        ptn_yzm_reset= (ProgressButton) findViewById(R.id.ptn_yzm_reset);
        ptn_yzm_reset.setOnClickListener(this);
        //去登录
        tv_go_login_reset= (TextView) findViewById(R.id.tv_go_login_reset);
        tv_go_login_reset.setOnClickListener(this);
    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yzm_reset:
                closeKeyboard();
                String strGetJudgePhone = edit_input_resetPassword_phone.getText().toString();//EditText中输入的手机号
                String strResetGetPassword2=edit_input_new_password_reset.getText().toString();//EditText中输入的新密码
                JudgePhoneReset(strGetJudgePhone,strResetGetPassword2);//判断手机号
                break;
            case R.id.ptn_yzm_reset:
                closeKeyboard();
                String strResetGetPhoneCode=edit_input_code_reset.getText().toString();//EditText中输入的验证码
                String strResetGetPassword=edit_input_new_password_reset.getText().toString();//EditText中输入的新密码
                resetPassword(strResetGetPhoneCode,strResetGetPassword);//重置密码
                break;
            case R.id.tv_go_login_reset:  //去登录
                overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
                finish();
                break;

        }
    }



    /**
     *
     * @param strGetJudgePhone
     */
    private void JudgePhoneReset(String strGetJudgePhone,String strResetGetPassword2) {
        //正则表达式 ——手机号正确
        if (Validator.isMobile(strGetJudgePhone)&&Validator.isPassword(strResetGetPassword2)){
            CountDownButtonHelper helper = new CountDownButtonHelper(btn_yzm_reset,"再次获取",60,1);
            helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void finish() {
                    btn_yzm_reset.setText("再次获取");
                }
            });
            helper.start();//开始倒计时
            //手机号合法，开始获取验证码
            BmobSMS.requestSMSCode(strGetJudgePhone, "重置密码", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e==null){//获取成功
                        Snackbar snackbar = Snackbar.make(rl_reset_show,  R.string.SMS_Code_Get_Success, Snackbar.LENGTH_LONG);
                        //设置Snackbar上提示的字体颜色
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }else {
                        if (e.getErrorCode()==9015){
                            Toast.makeText(ResetPwdActivity.this, "获取失败,如果你看到这条提示，请务必联系开发者！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }else {
            DialogPrompt dialogPrompt = new DialogPrompt(ResetPwdActivity.this, R.string.please_input_Correct_Phone_resetPassword );
            dialogPrompt.show();
        }
    }
    /**
     * 重置密码
     * @param strGetResetPhoneCode
     * @param strGetResetNewPassword
     */
    private void resetPassword(String strGetResetPhoneCode,String strGetResetNewPassword) {
        BmobUser.resetPasswordBySMSCode(strGetResetPhoneCode, strGetResetNewPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    DialogPrompt dialogPrompt = new DialogPrompt(ResetPwdActivity.this, R.string.password_reset_Success, 3);
                    dialogPrompt.showAndFinish(ResetPwdActivity.this);
                }else{
                    if (e.getErrorCode()==207){
                        Snackbar snackbar = Snackbar.make(rl_reset_show, "重置失败：验证码无效！", Snackbar.LENGTH_LONG);
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
            overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }
}
