package com.zhangsong.com.signup_system.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.Bmob_date.PersonSign;
import com.zhangsong.com.signup_system.Bmob_date.SignVerificationCode;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.LoginActivity;
import com.zhangsong.com.signup_system.activity.SignQueryActivity;
import com.zhangsong.com.signup_system.base.Selector.CustomDatePicker;
import com.zhangsong.com.signup_system.base.Widget.StepView;
import com.zhangsong.com.signup_system.singlepicker.PickerBean.PickerItem;
import com.zhangsong.com.signup_system.singlepicker.SinglePicker;
import com.zhangsong.com.signup_system.singlepicker.widget.WheelView;
import com.zhangsong.com.signup_system.utils.Bmob.Constant;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;
import com.zhangsong.com.signup_system.view.PowerfulEditText;
import com.zhangsong.com.signup_system.view.ProcessSuccessView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class MainSignUpFragment extends Fragment implements View.OnClickListener {
    private StepView stepView;
    private Button btn_sign_back,btn_sign_next;
    private FrameLayout fl_sign_info_show;
    private View itemNoticeView;//报名须知
    private View itemVerificationView;//报考验证
    private View itemSubmitinfoView;//填写资料
    private View itemSuccessView;//报名成功
    private String[] titles = new String[]{"报考须知","报考验证", "填写资料", "报名成功"};
    private CustomDatePicker datePicker;//填写资料的时间日期选择器
    private String time;
    private String date;
    private ProcessSuccessView processSuccessView;//查看报名动画自定义View
    private View view;
    private String strSignVoucherCode;//报名成功关键id
    private EditText editCode;//输入报名验证码
    PowerfulEditText edit_verification;//动画EditText

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup,container,false);
        initView();//初始化View
        initTopProcess();//初始化报名流程

        return view;
    }
    /**
     * 初始化View
     */
    private void initView() {
        stepView= view.findViewById(R.id.stepView);//报名流程
        //上一步
        btn_sign_back= view.findViewById(R.id.btn_sign_back);
        btn_sign_back.setOnClickListener(this);
        //下一步
        btn_sign_next= view.findViewById(R.id.btn_sign_next);
        btn_sign_next.setOnClickListener(this);
        fl_sign_info_show= view.findViewById(R.id.fl_sign_info_show);//填充的FrameLayout
        initFrameLayout();//初始化FrameLayout

    }
    /**
     * 初始化FrameLayout
     */
    private void initFrameLayout() {
        //初始化默认填充报名须知XML
        itemNoticeView = View.inflate(getActivity(), R.layout.item_sign_notice, null);//报名须知
        fl_sign_info_show.addView(itemNoticeView);
    }
    /**
     * 初始化报名流程
     */
    private void initTopProcess() {
        //设置进度标题
        stepView.setTitles(titles);
    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_back://上一步or后退按钮
                SignBack();
                break;
            case R.id.btn_sign_next://下一步
                SignNext();
                break;
        }
    }

    /**
     * 上一步or后退按钮
     */
    private void SignBack() {
        closeKeyboard();//隐藏软键盘
        int step = stepView.getCurrentStep();
        switch (step){
            case 1:
                //设置进度
                stepView.setCurrentStep(Math.max((step - 1) % stepView.getStepNum(), 0));
                fl_sign_info_show.addView(itemNoticeView);//添加报名须知
                fl_sign_info_show.removeView(itemVerificationView);//移除报考验证
                break;
            case 2:
                //设置进度
                stepView.setCurrentStep(Math.max((step - 1) % stepView.getStepNum(), 0));
                fl_sign_info_show.addView(itemVerificationView);//添加报名验证
                fl_sign_info_show.removeView(itemSubmitinfoView);//移除填写资料
                break;
            case 3:
                Snackbar snackbar = Snackbar.make(fl_sign_info_show,  R.string.sign_over, Snackbar.LENGTH_LONG);
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                break;
        }
    }
    /**
     * 下一步or前进按钮
     */
    private void SignNext() {
        closeKeyboard();//隐藏软键盘
        final int step = stepView.getCurrentStep();//获取流程View当前位置为 step
        switch (step){
            case 0:
                DialogSignNotes();//弹出友好提示框
                break;
            case 1:
                Verification();//验证报名码
                break;
            case 2:
                SubmissionSignData();//提交报名资料
                break;
        }
    }

    /**
     * 验证报名码
     */
    @SuppressLint("CutPasteId")
    private void Verification() {
        editCode = itemVerificationView.findViewById(R.id.edit_verification);
        edit_verification=itemVerificationView.findViewById(R.id.edit_verification);//动画EditText
        final String strCode= editCode.getText().toString();
        if(strCode.isEmpty()){
            edit_verification.startShakeAnimation();
            Snackbar snackbar = Snackbar.make(fl_sign_info_show, R.string.please_input_sign_code, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if(Constant.user!=null){
            UiTools.showSimpleLD(getActivity(),getString(R.string.loading_VerificationCode));
            BmobQuery<SignVerificationCode> query =new BmobQuery<>();
            query.getObject("32408f4e7c", new QueryListener<SignVerificationCode>() {
                @Override
                public void done(SignVerificationCode signVerificationCode, BmobException e) {
                    UiTools.closeSimpleLD();
                    if(e==null){
                        String strBmobCode= signVerificationCode.getVerificationCode();//字符串接受查询得到的字符数据
                        if(strCode.equals(strBmobCode)){
                            //设置进度
                            DialogVerification();
                        }else {
                            edit_verification.startShakeAnimation();
                            Snackbar snackbar = Snackbar.make(fl_sign_info_show, R.string.SignVerification, Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                        }
                    }else {
                        if(e.getErrorCode()==9016){
                            Snackbar snackbar = Snackbar.make(fl_sign_info_show, R.string.networkConnError, Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        if(e.getErrorCode()==9015){
                            Snackbar snackbar = Snackbar.make(fl_sign_info_show, R.string.requestOvertime, Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }

                    }
                }
            });
        }else {
            Snackbar snackbar= Snackbar.make(itemVerificationView,"账户离线状态，是不是断网或被抢登了?",Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                    .setAction("嗯，我知道了", new View.OnClickListener() {  //设置点击按钮
                        @Override
                        public void onClick(View v) {

                        }
                    });
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
    }
    /**
     * 提交报名资料
     */
    private void SubmissionSignData() {
        /**
         * 获取提交资料XML布局View的id对象
         */
        EditText sign_edit_name =itemSubmitinfoView.findViewById(R.id.sign_edit_name);//姓名
        TextView sign_tv_sex=itemSubmitinfoView.findViewById(R.id.sign_tv_sex);//性别
        TextView sign_tv_nation=itemSubmitinfoView.findViewById(R.id.sign_tv_nation);//民族
        EditText sign_edit_id_card =itemSubmitinfoView.findViewById(R.id.sign_edit_id_card);//身份证号码
        TextView sign_tv_culture_degree=itemSubmitinfoView.findViewById(R.id.sign_tv_culture_degree);//文化程度
        TextView sign_tv_politics_face=itemSubmitinfoView.findViewById(R.id.sign_tv_politics_face);//政治面貌
        EditText sign_edit_contact_telephone =itemSubmitinfoView.findViewById(R.id.sign_edit_contact_telephone);//联系电话
        TextView sign_tv_declare_occupation=itemSubmitinfoView.findViewById(R.id.sign_tv_declare_occupation);//申报职业
        TextView sign_tv_declare_hrade=itemSubmitinfoView.findViewById(R.id.sign_tv_declare_hrade);//申报级别
        TextView sign_tv_Two_exams=itemSubmitinfoView.findViewById(R.id.sign_tv_Two_exams);//是否二考
        TextView sign_tv_examination_time=itemSubmitinfoView.findViewById(R.id.sign_tv_examination_time);//考试时间
        TextView sign_tv_work_company=itemSubmitinfoView.findViewById(R.id.sign_tv_work_company);//工作单位
        /**
         * 获取填写资料的文本内容，并进行友好合法判断
         */
        String strName=sign_edit_name.getText().toString();//姓名
        String strSex=sign_tv_sex.getText().toString();//性别
        String strNation=sign_tv_nation.getText().toString();//民族
        String strIdCard=sign_edit_id_card.getText().toString();//身份证号码
        String strDegreeEducation=sign_tv_culture_degree.getText().toString();//文化程度
        String strPoliticsFace=sign_tv_politics_face.getText().toString();//政治面貌
        String strContactTelephone=sign_edit_contact_telephone.getText().toString();//联系电话
        String strDeclareOccupation=sign_tv_declare_occupation.getText().toString();//申报职业
        String strDeclareLevel =sign_tv_declare_hrade.getText().toString();//申报级别
        String strTwoExams=sign_tv_Two_exams.getText().toString();//是否二考
        String strExaminationTime=sign_tv_examination_time.getText().toString();//考试时间
        String strWorkCompany=sign_tv_work_company.getText().toString();//工作单位
        if (strName.isEmpty()){//姓名
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signName);
            dialogPrompt.show();
            return;
        }
        if (strSex.isEmpty()){//性别
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signSex);
            dialogPrompt.show();
            return;
        }
        if (strNation.isEmpty()){//民族
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signNation);
            dialogPrompt.show();
            return;
        }
        if (strIdCard.isEmpty()){//身份证号
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signIdCard);
            dialogPrompt.show();
            return;
        }
        if (strDegreeEducation.isEmpty()){//文化程度
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signCultureDegree);
            dialogPrompt.show();
            return;
        }
        if (strPoliticsFace.isEmpty()){//政治面貌
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signPoliticsFace);
            dialogPrompt.show();
            return;
        }
        if (strContactTelephone.isEmpty()){//联系电话
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signContactTelephone);
            dialogPrompt.show();
            return;
        }
        if (strDeclareOccupation.isEmpty()){//申报职业
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signDeclareOccupation);
            dialogPrompt.show();
            return;
        }
        if (strDeclareLevel.isEmpty()){//申报级别
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signDeclareHrade);
            dialogPrompt.show();
            return;
        }
        if (strTwoExams.isEmpty()){//是否二考
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signTwoExams);
            dialogPrompt.show();
            return;
        }
        if (strExaminationTime.isEmpty()){//考试时间
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signExaminationTime);
            dialogPrompt.show();
            return;
        }
        if (strWorkCompany.isEmpty()){//工作单位
            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.signWorkCompany);
            dialogPrompt.show();
            return;
        }
        if (Constant.user!=null){
            /**
             * 开始提交资料到Bmob云端数据库
             */
            UiTools.showSimpleLD(getActivity(),getString(R.string.loading_Submission));
            PersonSign personSign=new PersonSign();//获取对象
            personSign.setName(strName);//提交姓名
            personSign.setSex(strSex);//提交性别
            personSign.setNation(strNation);//提交民族
            personSign.setIdCard(strIdCard);//提交身份证号码
            personSign.setDegreeEducation(strDegreeEducation);//提交文化程度
            personSign.setPoliticalOutlook(strPoliticsFace);//提交政治面貌
            personSign.setContactNumber(strContactTelephone);//提交联系电话
            personSign.setOccupation(strDeclareOccupation);//提交申报职业
            personSign.setDeclarationLevel(strDeclareLevel);//提交申报级别
            personSign.setWhetherTwoExam(strTwoExams);//是否二考
            personSign.setExaminationTime(strExaminationTime);//提交考试时间
            personSign.setWorkUnit(strWorkCompany);//提交工作单位
            personSign.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    UiTools.closeSimpleLD();
                    if (e==null){
                        //报名成功关键id
                        strSignVoucherCode = s;
                        DialogSubmissionInfo();//弹出加入QQ群窗口
                    }else {
                        if(e.getErrorCode()==401){
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.sign_idCardAndContactNumber);
                            dialogPrompt.show();
                            return;
                        }
                        if(e.getErrorCode()==9015){
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.networkConnError);
                            dialogPrompt.show();
                            return;
                        }
                        DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), R.string.unknown_error);
                        dialogPrompt.show();
                    }
                }
            });
        }else {
            Snackbar snackbar= Snackbar.make(itemSubmitinfoView,"账户离线状态，是不是断网或被抢登了?",Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                    .setAction("嗯，我知道了", new View.OnClickListener() {  //设置点击按钮
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "您可以选择重启应用或点击头像重新登录！", Toast.LENGTH_LONG).show();
                        }
                    });
            //设置Snackbar上提示的字体颜色
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }

    }


    /**
     * 添加和移除FrameLayout
     * @param addView
     * @param removeView
     */
    private void DialogAddViewRemoveView(View addView,View removeView){
        fl_sign_info_show.addView(addView);
        fl_sign_info_show.removeView(removeView);
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
     *
     * 报名须知
     */
    private void DialogSignNotes(){
        itemVerificationView=View.inflate(getActivity(), R.layout.fragment_sign_verification, null);//报考验证
        final int step = stepView.getCurrentStep();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon);
        builder.setTitle("温馨提示");
        builder.setMessage("您认真阅读报名须知了吗？");
        builder.setPositiveButton("去验证", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //点击去验证 才执行下一步View跳动
                stepView.setCurrentStep((step + 1) % stepView.getStepNum());
                //添加报名验证+移除报名须知
                DialogAddViewRemoveView(itemVerificationView,itemNoticeView);
            }
        });
        builder.setNeutralButton("再瞅瞅", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar snackbar= Snackbar.make(itemNoticeView,"这次一定要认真阅读哦",Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                        .setAction("嗯，我知道了", new View.OnClickListener() {  //设置点击按钮
                            @Override
                            public void onClick(View v) {

                            }
                        });
                //设置Snackbar上提示的字体颜色
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    /**
     *
     * 报名验证
     */
    private void DialogVerification(){
        final int step = stepView.getCurrentStep();
        itemSubmitinfoView =View.inflate(getActivity(), R.layout.fragment_sign_submitinfo, null);//填写资料
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon);
        builder.setTitle("温馨提示");
        builder.setMessage("验证通过");
        builder.setPositiveButton("开始报名", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                editCode.setText("");//开始报名后清除之前输入的报名码
                //点击去验证 才执行下一步View跳动
                stepView.setCurrentStep((step + 1) % stepView.getStepNum());
                initPicker();//初始化时间选择器
                initSinglePicker();//初始化单项文本滑动选择
                //添加提交资料+移除报名验证
                DialogAddViewRemoveView(itemSubmitinfoView,itemVerificationView);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    /**
     * 初始化单项文本滑动选择
     */
    private void initSinglePicker(){
        //性别
        RelativeLayout sign_rl_sex=itemSubmitinfoView.findViewById(R.id.sign_rl_sex);//性别根布局
        final TextView sign_tv_sex=itemSubmitinfoView.findViewById(R.id.sign_tv_sex);//性别
        //选择性别
        sign_rl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> SexData = new ArrayList<>();
                SexData.add(new PickerItem(1,"男"));
                SexData.add(new PickerItem(2,"女"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),SexData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){
                            case 0:
                                sign_tv_sex.setText(item.getName());//设置性别
                                break;
                            case 1:
                                sign_tv_sex.setText(item.getName());//设置性别
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //民族
        RelativeLayout sign_rl_nation=itemSubmitinfoView.findViewById(R.id.sign_rl_nation);//民族根布局
        final TextView sign_tv_nation=itemSubmitinfoView.findViewById(R.id.sign_tv_nation);//民族
        //选择民族
        sign_rl_nation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> NationData = new ArrayList<>();
                NationData.add(new PickerItem(1,"壮族"));
                NationData.add(new PickerItem(2,"藏族"));
                NationData.add(new PickerItem(3,"裕固族"));
                NationData.add(new PickerItem(4,"彝族"));
                NationData.add(new PickerItem(5,"瑶族"));
                NationData.add(new PickerItem(6,"锡伯族"));
                NationData.add(new PickerItem(7,"乌孜别克族"));
                NationData.add(new PickerItem(8,"维吾尔族"));
                NationData.add(new PickerItem(9,"佤族"));
                NationData.add(new PickerItem(10,"土家族"));
                NationData.add(new PickerItem(11,"土族"));
                NationData.add(new PickerItem(12,"塔塔尔族"));
                NationData.add(new PickerItem(13,"塔吉克族"));
                NationData.add(new PickerItem(14,"水族"));
                NationData.add(new PickerItem(15,"畲族"));
                NationData.add(new PickerItem(16,"撒拉族"));
                NationData.add(new PickerItem(17,"羌族"));
                NationData.add(new PickerItem(18,"普米族"));
                NationData.add(new PickerItem(19,"怒族"));
                NationData.add(new PickerItem(20,"纳西族"));
                NationData.add(new PickerItem(21,"仫佬族"));
                NationData.add(new PickerItem(22,"苗族"));
                NationData.add(new PickerItem(23,"蒙古族"));
                NationData.add(new PickerItem(24,"门巴族"));
                NationData.add(new PickerItem(25,"毛南族"));
                NationData.add(new PickerItem(26,"满族"));
                NationData.add(new PickerItem(27,"珞巴族"));
                NationData.add(new PickerItem(28,"僳僳族"));
                NationData.add(new PickerItem(29,"黎族"));
                NationData.add(new PickerItem(30,"拉祜族"));
                NationData.add(new PickerItem(31,"柯尔克孜族"));
                NationData.add(new PickerItem(32,"景颇族"));
                NationData.add(new PickerItem(33,"京族"));
                NationData.add(new PickerItem(34,"基诺族"));
                NationData.add(new PickerItem(35,"回族"));
                NationData.add(new PickerItem(36,"赫哲族"));
                NationData.add(new PickerItem(37,"哈萨克族"));
                NationData.add(new PickerItem(38,"哈尼族"));
                NationData.add(new PickerItem(39,"仡佬族"));
                NationData.add(new PickerItem(40,"高山族"));
                NationData.add(new PickerItem(41,"鄂温克族"));
                NationData.add(new PickerItem(42,"俄罗斯族"));
                NationData.add(new PickerItem(43,"鄂伦春族"));
                NationData.add(new PickerItem(44,"独龙族"));
                NationData.add(new PickerItem(45,"东乡族"));
                NationData.add(new PickerItem(46,"侗族"));
                NationData.add(new PickerItem(47,"德昂族"));
                NationData.add(new PickerItem(48,"傣族"));
                NationData.add(new PickerItem(49,"达斡尔族"));
                NationData.add(new PickerItem(50,"朝鲜族"));
                NationData.add(new PickerItem(51,"布依族"));
                NationData.add(new PickerItem(52,"布朗族"));
                NationData.add(new PickerItem(53,"保安族"));
                NationData.add(new PickerItem(54,"白族"));
                NationData.add(new PickerItem(55,"阿昌族"));
                NationData.add(new PickerItem(56,"汉族"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),NationData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(55);//默认选择汉族
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置民族
                            case 0:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 2:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 3:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 4:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 5:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 6:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 7:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 8:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 9:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 10:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 11:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 12:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 13:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 14:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 15:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 16:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 17:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 18:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 19:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 20:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 21:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 22:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 23:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 24:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 25:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 26:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 27:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 28:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 29:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 30:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 31:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 32:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 33:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 34:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 35:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 36:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 37:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 38:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 39:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 40:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 41:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 42:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 43:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 44:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 45:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 46:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 47:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 48:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 49:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 50:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 51:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 52:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 53:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 54:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 55:
                                sign_tv_nation.setText(item.getName());
                                break;
                            case 56:
                                sign_tv_nation.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //文化程度
        RelativeLayout sign_rl_culture_degree=itemSubmitinfoView.findViewById(R.id.sign_rl_culture_degree);//文化程度根布局
        final TextView sign_tv_culture_degree=itemSubmitinfoView.findViewById(R.id.sign_tv_culture_degree);//文化程度
        //选择文化程度
        sign_rl_culture_degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> CultureDegreeData = new ArrayList<>();
                CultureDegreeData.add(new PickerItem(1,"研究生"));
                CultureDegreeData.add(new PickerItem(2,"博士"));
                CultureDegreeData.add(new PickerItem(3,"大学本科"));
                CultureDegreeData.add(new PickerItem(4,"大学专科"));
                CultureDegreeData.add(new PickerItem(5,"专科学校"));
                CultureDegreeData.add(new PickerItem(6,"中等专业学校(中专)"));
                CultureDegreeData.add(new PickerItem(7,"中等技术学校(中技)"));
                CultureDegreeData.add(new PickerItem(8,"技工学校"));
                CultureDegreeData.add(new PickerItem(9,"高中"));
                CultureDegreeData.add(new PickerItem(10,"初中"));
                CultureDegreeData.add(new PickerItem(11,"小学"));
                CultureDegreeData.add(new PickerItem(12,"文盲或半文盲"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),CultureDegreeData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(3);//默认选择大学专科
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置文化程度
                            case 0:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 2:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 3:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 4:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 5:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 6:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 7:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 8:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 9:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 10:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 11:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                            case 12:
                                sign_tv_culture_degree.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //政治面貌
        RelativeLayout sign_rl_politics_face=itemSubmitinfoView.findViewById(R.id.sign_rl_politics_face);//政治面貌根布局
        final TextView sign_tv_politics_face=itemSubmitinfoView.findViewById(R.id.sign_tv_politics_face);//政治面貌
        //选择政治面貌
        sign_rl_politics_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> FaceData = new ArrayList<>();
                FaceData.add(new PickerItem(1, "中共党员"));
                FaceData.add(new PickerItem(2, "中共预备党员"));
                FaceData.add(new PickerItem(3, "共青团员"));
                FaceData.add(new PickerItem(4, "群众"));
                FaceData.add(new PickerItem(5, "民革党员"));
                FaceData.add(new PickerItem(7, "民盟盟员"));
                FaceData.add(new PickerItem(8, "民建会员"));
                FaceData.add(new PickerItem(9, "民进会员"));
                FaceData.add(new PickerItem(10, "农工党党员"));
                FaceData.add(new PickerItem(11, "致公党党员"));
                FaceData.add(new PickerItem(12, "九三学社社员"));
                FaceData.add(new PickerItem(13, "台盟盟员"));
                FaceData.add(new PickerItem(14, "无党派人士"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),FaceData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(3);//默认选择群众
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置政治面貌
                            case 0:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 2:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 3:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 4:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 5:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 6:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 7:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 8:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 9:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 10:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 11:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 12:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 13:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                            case 14:
                                sign_tv_politics_face.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //申报职业
        RelativeLayout sign_rl_declare_occupation=itemSubmitinfoView.findViewById(R.id.sign_rl_declare_occupation);//申报职业根布局
        final TextView sign_tv_declare_occupation=itemSubmitinfoView.findViewById(R.id.sign_tv_declare_occupation);//申报职业
        //选择申报职业
        sign_rl_declare_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> DeclareOccupationData = new ArrayList<>();
                DeclareOccupationData.add(new PickerItem(1,"办公软件应用(office2010)"));
                DeclareOccupationData.add(new PickerItem(2,"图形图像处理(PhotoShopC5S)"));
                DeclareOccupationData.add(new PickerItem(3,"因特网应用高级(ASP.NET)"));
                DeclareOccupationData.add(new PickerItem(4,"AutoCAD2010高级(建筑)"));
                DeclareOccupationData.add(new PickerItem(5,"AutoCAD2015高级(建筑)"));
                DeclareOccupationData.add(new PickerItem(6,"AutoCAD2010高级(机械)"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),DeclareOccupationData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置申报职业
                            case 0:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 2:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 3:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 4:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 5:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                            case 6:
                                sign_tv_declare_occupation.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //申报级别
        RelativeLayout sign_rl_declare_hrade=itemSubmitinfoView.findViewById(R.id.sign_rl_declare_hrade);//申报级别根布局
        final TextView sign_tv_declare_hrade=itemSubmitinfoView.findViewById(R.id.sign_tv_declare_hrade);//申报级别
        //选择申报级别
        sign_rl_declare_hrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> DeclareHradeData = new ArrayList<>();
                DeclareHradeData.add(new PickerItem(1,"3"));
                DeclareHradeData.add(new PickerItem(2,"4"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),DeclareHradeData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置申报级别
                            case 0:
                                sign_tv_declare_hrade.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_declare_hrade.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //是否二考
        RelativeLayout sign_rl_Two_exams=itemSubmitinfoView.findViewById(R.id.sign_rl_Two_exams);//是否二考根布局
        final TextView sign_tv_Two_exams=itemSubmitinfoView.findViewById(R.id.sign_tv_Two_exams);//是否二考
        //选择是否二考
        sign_rl_Two_exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> DeclareHradeData = new ArrayList<>();
                DeclareHradeData.add(new PickerItem(1,"是"));
                DeclareHradeData.add(new PickerItem(2,"否"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),DeclareHradeData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置是否二考
                            case 0:
                                sign_tv_Two_exams.setText(item.getName());
                                break;
                            case 1:
                                sign_tv_Two_exams.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
        //工作单位
        RelativeLayout sign_rl_work_company=itemSubmitinfoView.findViewById(R.id.sign_rl_work_company);//工作单位根目录
        final TextView sign_tv_work_company=itemSubmitinfoView.findViewById(R.id.sign_tv_work_company);//工作单位
        //选择工作单位
        sign_rl_work_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickerItem> DeclareHradeData = new ArrayList<>();
                DeclareHradeData.add(new PickerItem(1,"先添加这几个班。不知道有哪些班级"));
                DeclareHradeData.add(new PickerItem(2,"16计管（移动应用）"));
                DeclareHradeData.add(new PickerItem(3,"16计管（信息管理）"));
                DeclareHradeData.add(new PickerItem(4,"16计管（网络技术）"));
                DeclareHradeData.add(new PickerItem(5,"16物流（1）"));
                DeclareHradeData.add(new PickerItem(6,"16物流（2）"));
                DeclareHradeData.add(new PickerItem(7,"16电商（1）"));
                DeclareHradeData.add(new PickerItem(8,"16电商（2）"));
                DeclareHradeData.add(new PickerItem(9,"16电商（3）"));
                DeclareHradeData.add(new PickerItem(10,"16营销（2）"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(getActivity(),DeclareHradeData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index){//设置工作单位
                            case 0:
                                Toast.makeText(getActivity(), "此项不支持！", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 2:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 3:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 4:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 5:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 6:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 7:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 8:
                                sign_tv_work_company.setText(item.getName());
                                break;
                            case 9:
                                sign_tv_work_company.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });
    }

    /**
     * 初始化时间选择器
     */
    private void initPicker() {
        //时间选择器的考试时间
        RelativeLayout sign_rl_examination_time=itemSubmitinfoView.findViewById(R.id.sign_rl_examination_time);//时间选择器-考试时间
        final TextView sign_tv_examination_time=itemSubmitinfoView.findViewById(R.id.sign_tv_examination_time);//考试时间TextView
        sign_rl_examination_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(date);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置显示系统日期
        // sign_tv_examination_time.setText("2018-11-8");
        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(getActivity(), "请选择日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                sign_tv_examination_time.setText(time.split(" ")[0]);
            }
        }, "2007-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);
    }
    /**
     *
     * 报名成功
     */
    private void DialogSubmissionInfo(){
        final int step = stepView.getCurrentStep();
        itemSuccessView =View.inflate(getActivity(), R.layout.fragment_sign_success, null);//填写资料
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon);
        builder.setTitle("温馨提示");
        builder.setMessage(getString(R.string.signqq));
        builder.setPositiveButton("查看报名", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //点击去验证 才执行下一步View跳动
                stepView.setCurrentStep((step + 1) % stepView.getStepNum());
                //添加报名成功+移除提交资料
                DialogAddViewRemoveView(itemSuccessView,itemSubmitinfoView);
                initSignSuccess();//初始化报名成功
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    /**
     * 初始化报名成功
     */
    private void initSignSuccess() {
        //查看报名动画自定义View
        processSuccessView = itemSuccessView.findViewById(R.id.processSuccessView);
        TextView tv_sign_voucher_code=itemSuccessView.findViewById(R.id.tv_sign_voucher_code);
        tv_sign_voucher_code.setTextIsSelectable(true);
//        tv_sign_voucher_code.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                cmb.setText(getActivity().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//                cm.getText();//获取粘贴信息
//
//                return false;
//            }
//        });
        tv_sign_voucher_code.setText(strSignVoucherCode);
        //查询详细信息
        EditText tv_go_query=itemSuccessView.findViewById(R.id.tv_go_query);
        tv_go_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignQueryActivity.class));
            }
        });
        startAni();//开始报名成功动画
    }
    /**
     * 开始报名成功动画
     */
    private void startAni() {
        ValueAnimator a = ValueAnimator.ofFloat(0, 1);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                processSuccessView.setProgress(progress);
            }
        });
        a.setDuration(10000);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.start();
    }
    /**
     * 软键盘的关闭
     */
    private void closeKeyboard() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
