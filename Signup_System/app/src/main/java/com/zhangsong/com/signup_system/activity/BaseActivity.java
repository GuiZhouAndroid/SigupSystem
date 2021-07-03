package com.zhangsong.com.signup_system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import cn.bmob.v3.Bmob;



public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化SDK
        Bmob.initialize(this, "1f6406597d7e42d492c21eacb8f103d7");
        //初始化Fresco
        Fresco.initialize(this);
    }
}
