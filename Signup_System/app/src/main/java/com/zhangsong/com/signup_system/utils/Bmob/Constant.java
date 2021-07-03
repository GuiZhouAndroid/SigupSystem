package com.zhangsong.com.signup_system.utils.Bmob;

import android.os.Environment;

import com.zhangsong.com.signup_system.Bmob_date.PersonSign;
import com.zhangsong.com.signup_system.Bmob_date.SignVerificationCode;
import com.zhangsong.com.signup_system.Bmob_date.User;
import java.io.File;
import java.util.List;

public class Constant {
    public static int width;
    public static int height;
    public static List<File> list;
    public static List<File> thumbnailList;

    public static String picturePath = Environment.getExternalStorageDirectory()
            + File.separator + "SelectFileTemp" + File.separator + "picture";
    public static String voicePath = Environment.getExternalStorageDirectory()
            + File.separator + "SelectFileTemp" + File.separator + "voice";

    public static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "Test";
    public static String imagePath = basePath + File.separator + "images";
    public static User user;
    public static PersonSign personSign;
    public static SignVerificationCode signVerificationCode;
}
