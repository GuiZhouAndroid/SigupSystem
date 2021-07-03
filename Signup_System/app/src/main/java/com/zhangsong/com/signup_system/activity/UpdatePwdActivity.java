package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.RegularExpression.Validator;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.PowerfulEditText;
import com.zhangsong.com.signup_system.view.ProgressButton;
import com.zhangsong.com.signup_system.view.Wave.Wave;
import com.zhangsong.com.signup_system.view.Wave.WaveView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePwdActivity extends MySwipeBackActivity implements View.OnClickListener {
    private Toolbar toolbar_constant;
    private TextView tv_toolbar_title;//标题
    private WaveView wave_view;//背景动画
    PowerfulEditText update_old_pwd,update_new_pwd;//动画EditText
    ProgressButton pb_button_update;
    private ProgressButton ptn_update;
    private RelativeLayout rl_update_pwd_show;
    private String strEditOldPwd;//输入的旧密码
    private String strEditNewPwd;//输入的新密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        initView();//初始化View
        initToolbar();//初始化标题栏Toolbar
        initWave();//初始化背景动画
    }

    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        rl_update_pwd_show = (RelativeLayout) findViewById(R.id.rl_update_pwd_show);
        toolbar_constant= (Toolbar) findViewById(R.id.toolbar_constant);//Toolbar
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.update_password);
        wave_view = (WaveView) findViewById(R.id.wave_view);//背景动画
        //点击登录
        pb_button_update = (ProgressButton) findViewById(R.id.ptn_update);
        pb_button_update.setBgColor(Color.RED);
        pb_button_update.setTextColor(Color.WHITE);
        pb_button_update.setProColor(Color.WHITE);
        pb_button_update.setButtonText("查  询");
        ptn_update = (ProgressButton) findViewById(R.id.ptn_update);
        ptn_update.setOnClickListener(this);
        update_old_pwd= (PowerfulEditText) findViewById(R.id.update_old_pwd);//动画旧密码EditText
        update_new_pwd= (PowerfulEditText) findViewById(R.id.update_new_pwd);//动画新密码EditText
    }
    /**
     * 初始化标题栏Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar_constant);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    /**
     * 初始化背景动画
     */
    private void initWave() {
        wave_view.setOrientation(WaveView.Orientation.DOWN);
        int color = Color.parseColor("#55303F9F");
        Wave wave1 = new Wave(1080, 90, 10, 130, color);
        Wave wave2 = new Wave(1620, 78, -10, 140, color);
        Wave wave3 = new Wave(2080, 8, 8, 1500, color);
        wave_view.addWave(wave1);
        wave_view.addWave(wave2);
        wave_view.addWave(wave3);
    }
    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ptn_update:
               UpdatePassWord();//更新密码
                closeKeyboard();//软键盘的关闭
                break;
        }
    }

    /**
     * 更新密码
     */
    private void UpdatePassWord() {

        strEditOldPwd=update_old_pwd.getText().toString();//旧密码
        strEditNewPwd=update_new_pwd.getText().toString();//新密码
        if (TextUtils.isEmpty(strEditOldPwd)&&TextUtils.isEmpty(strEditNewPwd)){
            update_old_pwd.startShakeAnimation();//旧密码窗口抖动
            update_new_pwd.startShakeAnimation();//新密码窗口抖动
            Snackbar snackbar = Snackbar.make(rl_update_pwd_show,R.string.please_input_password, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //旧密码空判断
        if (TextUtils.isEmpty(strEditOldPwd)){
            update_old_pwd.startShakeAnimation();//旧密码窗口抖动
            Snackbar snackbar = Snackbar.make(rl_update_pwd_show,R.string.please_input_old_password, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //新密码空判断
        if (TextUtils.isEmpty(strEditNewPwd)){
            update_new_pwd.startShakeAnimation();//新密码窗口抖动
            Snackbar snackbar = Snackbar.make(rl_update_pwd_show,R.string.please_input_new_password, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if(!Validator.isPassword(strEditOldPwd)){
            update_old_pwd.startShakeAnimation();//旧密码窗口抖动
            Snackbar snackbar = Snackbar.make(rl_update_pwd_show,R.string.password_old_important, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if(!Validator.isPassword(strEditNewPwd)){
            update_new_pwd.startShakeAnimation();//新密码窗口抖动
            Snackbar snackbar = Snackbar.make(rl_update_pwd_show,R.string.password_new_important, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        pb_button_update.startAnim();
        Message m=UpdateHandler.obtainMessage();
        UpdateHandler.sendMessageDelayed(m,1500);
    }
    /**
     * 更新转场
     */
    private Handler UpdateHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_button_update.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    UiTools.showSimpleLD(UpdatePwdActivity.this, R.string.loading_update_pwd);//开始提示框
                    BmobUser.updateCurrentUserPassword(strEditOldPwd, strEditNewPwd, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            UiTools.closeSimpleLD();//关闭提示框
                            if(e==null){//修改成功提示框
                                DialogPrompt dialogPrompt = new DialogPrompt(UpdatePwdActivity.this, R.string.update_ResetOK, 3);
                                dialogPrompt.showAndFinish(UpdatePwdActivity.this);
                                UpdatePwdActivity.this.setResult(RESULT_OK);
                            }else {//修改失败提示框
                                DialogPrompt dialogPrompt = new DialogPrompt(UpdatePwdActivity.this, R.string.update_ResetNo);
                                dialogPrompt.show();
                            }
                        }
                    });
                }
            });

        }
    };
    /**
     *  从密码返回主页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    /**
     * Toolbar 的Navigation Button 添加点击事件,用于返回首页
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
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
}
