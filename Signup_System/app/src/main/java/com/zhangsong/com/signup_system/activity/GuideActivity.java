package com.zhangsong.com.signup_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.base.CircleProgress.CircleProgress;

public class GuideActivity extends Activity {
    private CircleProgress circleprogress;
    //防触碰使用的变量
    private long firstTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        //初始化View
        circleprogress =findViewById(R.id.circleprogress);
        //开始倒计时
        circleprogress.startCountDown();
        //倒计时结束
        circleprogress.setAddCountDownListener(new CircleProgress.OnCountDownFinishListener() {
            @Override
            public void countDownFinished() {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.anim_out,R.anim.anim_in);
                finish();
            }
        });
    }
    /**
     *  防触碰处理
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if (secondTime- firstTime >3000) {
                    Toast.makeText(this,"再按一次退出程序！",Toast.LENGTH_SHORT).show();
                    firstTime =secondTime;
                    return true;
                }else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
