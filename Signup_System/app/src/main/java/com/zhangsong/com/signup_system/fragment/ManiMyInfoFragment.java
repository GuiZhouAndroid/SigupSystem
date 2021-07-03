package com.zhangsong.com.signup_system.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.LoginActivity;
import com.zhangsong.com.signup_system.activity.MineActivity;
import com.zhangsong.com.signup_system.activity.SettingActivity;
import com.zhangsong.com.signup_system.activity.UpdatePwdActivity;
import com.zhangsong.com.signup_system.navigation.EasyNavigitionBar;
import com.zhangsong.com.signup_system.utils.Bmob.Constant;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;

import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.zhangsong.com.signup_system.utils.Bmob.AppUtils.setIconFilePath;

public class ManiMyInfoFragment extends Fragment {
    private LinearLayout ll_myinfo_show,my_account_info,my_setting,my_night_mode,update_password;
    private View view;
    public static Button btn_finish_login;//退出登录

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        intView();//初始化View
        return view;
    }
    /**
     * 初始化View
     */
    private void intView() {
        //根布局，用户显示Snackbar
        ll_myinfo_show=view.findViewById(R.id.ll_myinfo_show);
        //账户信息
        my_account_info=view.findViewById(R.id.my_account_info);
        my_account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), MineActivity.class),104);
            }
        });
        //修改密码
        update_password=view.findViewById(R.id.update_password);
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
            }
        });
        //夜间模式
        my_night_mode=view.findViewById(R.id.my_night_mode);
        my_night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(ll_myinfo_show,R.string.night_mode, Snackbar.LENGTH_LONG);
                //设置Snackbar上提示的字体颜色
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
            }
        });
        //设置
        my_setting=view.findViewById(R.id.my_setting);
        my_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        btn_finish_login=view.findViewById(R.id.btn_finish_login);
        btn_finish_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();   //清除缓存用户对象
                setIconFilePath("");
                Constant.user = null;
                MainActivity.tv_signature.setText(R.string.signature);//个性签名
                MainActivity.tv_username.setText(R.string.click_user_image);//侧滑用户名
                setDefaultIcon();//侧滑默认头像
                MainActivity.mani_title.setText(R.string.app_name);
                btn_finish_login.setText("未登录");
                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
    private void setDefaultIcon() {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(getApplicationContext())
                .load(R.mipmap.wheat)
                .apply(options)
                .into(MainActivity.nav_header_icon);
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
}
