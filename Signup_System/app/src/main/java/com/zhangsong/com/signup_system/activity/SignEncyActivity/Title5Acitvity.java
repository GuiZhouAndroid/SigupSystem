package com.zhangsong.com.signup_system.activity.SignEncyActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.MySwipeBackActivity;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class Title5Acitvity extends MySwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ency_title5);
    }
    /**
     * 点击分享
     */
    private void shareMethod() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        getString(R.string.app_name);
        //title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我是标题");
        //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("https://www.baidu.com/");
        //text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        //comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        //site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        //siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://user.qzone.qq.com/2393579405/infocenter");
        // 启动分享GUI
        oks.show(this);
    }
}
