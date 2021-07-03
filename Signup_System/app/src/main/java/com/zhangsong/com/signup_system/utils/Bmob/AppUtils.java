package com.zhangsong.com.signup_system.utils.Bmob;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.zhangsong.com.signup_system.activity.MyApplication;

public class AppUtils {

    private static String IconFilePath = "";

    public static String getIconFilePath() {
        if (TextUtils.isEmpty(IconFilePath)) {
            IconFilePath = getSharedPerferences().getString("IconFilePath", "");
        }
        return IconFilePath;
    }
    public static void setIconFilePath(String avatarPath) {
        IconFilePath = avatarPath;
        SharedPreferences.Editor editor = getSharedPerferences().edit();
        editor.putString("IconFilePath", avatarPath);
        editor.commit();
    }

    public static SharedPreferences getSharedPerferences() {
        return MyApplication.getInstance().getSharedPreferences("300fans", Context.MODE_PRIVATE);
    }
}