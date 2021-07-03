package com.zhangsong.com.signup_system.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import com.mob.MobApplication;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
public class MyApplication extends MobApplication{
    private NotificationManager manager;
    private static MyApplication applicationAcg;
    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //初始化SDK
        Bmob.initialize(this, "1f6406597d7e42d492c21eacb8f103d7");
        applicationAcg = this;
//        // 使用推送服务时的初始化操作
//        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
//            @Override
//            public void done(BmobInstallation bmobInstallation, BmobException e) {
//                if (e == null) {//初始化成功
//                    Toast.makeText(MyApplication.this, bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId(), Toast.LENGTH_SHORT).show();
//                } else {//初始化失败
//                    Toast.makeText(MyApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        // 启动推送服务
//        BmobPush.startWork(this);
        /**
         * 默认设置一直使用夜间模式
         *
         * 这里AppCompatDelegate.setDefaultNightMode()方法可以接受的参数值有4个：
         * MODE_NIGHT_NO. Always use the day (light) theme(一直应用日间(light)主题).
         * MODE_NIGHT_YES. Always use the night (dark) theme(一直使用夜间(dark)主题).
         * MODE_NIGHT_AUTO. Changes between day/night based on the time of day(根据当前时间在day/night主题间切换).
         * MODE_NIGHT_FOLLOW_SYSTEM(默认选项). This setting follows the system’s setting, which is essentially MODE_NIGHT_NO(跟随系统，通常为MODE_NIGHT_NO).
         */
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }
    public static MyApplication getInstance() {
        return applicationAcg;
    }
}
