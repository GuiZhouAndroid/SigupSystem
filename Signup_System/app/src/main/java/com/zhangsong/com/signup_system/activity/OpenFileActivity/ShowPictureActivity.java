package com.zhangsong.com.signup_system.activity.OpenFileActivity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.MySwipeBackActivity;
import com.zhangsong.com.signup_system.utils.StatusBar.StatusBarUtils;

/**
 * 图片查看
 */

public class ShowPictureActivity extends MySwipeBackActivity {

    private final String TAG = "ShowPictureActivity";

    private ImageView iv_showPicture;
    private RelativeLayout rl_picture_show_back;
    String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        setLightMode();
        init();
        filePath = getIntent().getStringExtra("path");
        rl_picture_show_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (filePath != null) {
            iv_showPicture.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        iv_showPicture = (ImageView) findViewById(R.id.iv_showPicture);
        rl_picture_show_back = (RelativeLayout) findViewById(R.id.rl_picture_show_back);

    }
    /**
     * Android 6.0 以上设置状态栏颜色
     */
    private void setLightMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.BLACK);

            // 设置状态栏字体黑色
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


            // 设置状态栏字体白色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
