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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangsong.com.signup_system.Bmob_date.PersonSign;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.Bmob.Constant;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.PowerfulEditText;
import com.zhangsong.com.signup_system.view.ProgressButton;
import com.zhangsong.com.signup_system.view.Wave.Wave;
import com.zhangsong.com.signup_system.view.Wave.WaveView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class SignQueryActivity extends MySwipeBackActivity implements View.OnClickListener{
    private Toolbar toolbar_constant;
    private TextView tv_toolbar_title;//标题
    private WaveView wave_view;//背景动画
    PowerfulEditText sign_query;//动画EditText
    ProgressButton pb_button_query;
    private ProgressButton ptn_query;
    private RelativeLayout rl_query_show;
    private String strEditContent;//输入的凭证号
    private TextView tv_sign_people;//目前已成功报名人数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_query);
        initView();//初始化View
        initToolbar();//初始化标题栏Toolbar
        initSignPeopleNumber();
        initWave();//初始化背景动画
    }

    /**
     * 初始化View
     */
    @SuppressLint("CutPasteId")
    private void initView() {
        toolbar_constant= (Toolbar) findViewById(R.id.toolbar_constant);//Toolbar
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.sign_query);
        tv_sign_people= (TextView) findViewById(R.id.tv_sign_people);//目前已成功报名人数
        rl_query_show = (RelativeLayout) findViewById(R.id.rl_query_show);
        wave_view = (WaveView) findViewById(R.id.wave_view);//背景动画
        //点击登录
        pb_button_query = (ProgressButton) findViewById(R.id.ptn_query);
        pb_button_query.setBgColor(Color.RED);
        pb_button_query.setTextColor(Color.WHITE);
        pb_button_query.setProColor(Color.WHITE);
        pb_button_query.setButtonText("查  询");
        ptn_query = (ProgressButton) findViewById(R.id.ptn_query);
        ptn_query.setOnClickListener(this);
        sign_query= (PowerfulEditText) findViewById(R.id.sign_query);//动画EditText
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
     * 目前已成功报名人数
     */
    @SuppressLint("SetTextI18n")
    private void initSignPeopleNumber() {
        BmobQuery<PersonSign> personSignBmobQuery = new BmobQuery<>();
        personSignBmobQuery.findObjects(new FindListener<PersonSign>() {
            @Override
            public void done(List<PersonSign> list, BmobException e) {
                if (e==null){
                    tv_sign_people.setText(list.size()+"位同学");
                }else{
                    tv_sign_people.setText("加载失败，请检查网络");
                }
            }
        });
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
            case R.id.ptn_query:
                closeKeyboard();//关闭软键盘
                QuerySignInfo();
                break;
        }
    }

    /**
     * 查询信息
     */
    private void QuerySignInfo() {
        strEditContent = sign_query.getText().toString();
        if (strEditContent.isEmpty()){
            sign_query.startShakeAnimation();
            Snackbar snackbar = Snackbar.make(rl_query_show,  R.string.please_input_query_code, Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        pb_button_query.startAnim();
        Message m=QueryHandler.obtainMessage();
        QueryHandler.sendMessageDelayed(m,1500);
    }
    /**
     * 查询转场
     */
    private Handler QueryHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_button_query.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    if (Constant.user!=null){
                        UiTools.showSimpleLD(SignQueryActivity.this,getString(R.string.loading_Query));
                        final BmobQuery<PersonSign> query =new BmobQuery<>();
                        query.getObject(strEditContent, new QueryListener<PersonSign>() {
                            @Override
                            public void done(PersonSign personSign, BmobException e) {
                                UiTools.closeSimpleLD();//关闭提示框
                                if (e==null){
                                    //字符串接受查询得到的字符数据
                                    String strGetObjectId=personSign.getObjectId();
                                    if (strEditContent.equals(strGetObjectId)){//输入的凭证号匹配Bmob id
                                        sign_query.setText("");//清除凭证号
                                        Intent intent=new Intent(SignQueryActivity.this,SignTableActivity.class);
                                        intent.putExtra("BmobObjectId",personSign.getObjectId());//凭证号
                                        intent.putExtra("BmobName",personSign.getName());//姓名
                                        intent.putExtra("BmobSex",personSign.getSex());//性别
                                        intent.putExtra("BmobNation",personSign.getNation());//民族
                                        intent.putExtra("Bmobidcard",personSign.getIdCard());//身份证号
                                        intent.putExtra("BmobDegreeEducation",personSign.getDegreeEducation());//文化程度
                                        intent.putExtra("BmobPoliticalOutlook",personSign.getPoliticalOutlook());//政治面貌
                                        intent.putExtra("BmobContactNumber",personSign.getContactNumber());//联系电话
                                        intent.putExtra("BmobOccupation",personSign.getOccupation());//申报职业
                                        intent.putExtra("BmobDeclarationLevel",personSign.getDeclarationLevel());//申报级别
                                        intent.putExtra("BmobWhetherTwoExam",personSign.getWhetherTwoExam());//是否二考
                                        intent.putExtra("BmobExaminationTime",personSign.getExaminationTime());//考试时间
                                        intent.putExtra("BmobWorkUnit",personSign.getWorkUnit());//工作单位
                                        intent.putExtra("BmobCreatTime",personSign.getCreatedAt());//报名时间
                                        startActivity(intent);
                                        return;
                                    }
                                }else {
                                    if (e.getErrorCode() == 101) {
                                        sign_query.startShakeAnimation();
                                        Snackbar snackbar = Snackbar.make(rl_query_show,R.string.SignQueryCode, Snackbar.LENGTH_LONG);
                                        //设置Snackbar上提示的字体颜色
                                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                        snackbar.show();
                                        return;
                                    }
                                    if(e.getErrorCode() == 9015){
                                        Snackbar snackbar = Snackbar.make(rl_query_show,  R.string.Query_networkConnError, Snackbar.LENGTH_LONG);
                                        //设置Snackbar上提示的字体颜色
                                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                        snackbar.show();
                                        return;
                                    }
                                    if(e.getErrorCode() == 9016){
                                        Snackbar snackbar = Snackbar.make(rl_query_show,  R.string.Query_networkConnError, Snackbar.LENGTH_LONG);
                                        //设置Snackbar上提示的字体颜色
                                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                        snackbar.show();
                                        return;
                                    }
                                }
                            }
                        });
                    }else {
                        Snackbar snackbar = Snackbar.make(rl_query_show, "账户离线状态，是不是断网或被抢登了?", Snackbar.LENGTH_LONG);
                        //设置Snackbar上提示的字体颜色
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                }
            });

        }
    };
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
     *  从报名查询返回主页
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

}
