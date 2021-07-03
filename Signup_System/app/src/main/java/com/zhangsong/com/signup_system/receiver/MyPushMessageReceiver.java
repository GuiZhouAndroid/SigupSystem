package com.zhangsong.com.signup_system.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.bmob.push.PushConstants;

/**
 * 创建自定义的推送消息接收器，并在文件清单中注册
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private static final String TAG = "客户端收到的推荐消息";
    @Override
    public void onReceive(Context context, Intent intent) {

        String msg=intent.getStringExtra("msg");
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Toast.makeText(context, TAG+msg, Toast.LENGTH_SHORT).show();
            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentText("这是推送内容："+msg)
                    .setContentTitle("推送标题");
        }
    }

}

