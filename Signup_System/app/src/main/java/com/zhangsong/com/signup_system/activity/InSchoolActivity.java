package com.zhangsong.com.signup_system.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhangsong.com.signup_system.R;

public class InSchoolActivity extends MySwipeBackActivity {
    private WebView wv_in_school;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_school);
        //WebView初始化获得实例
        wv_in_school= (WebView) findViewById(R.id.wv_in_school);
        //声明WebSettings子类
        WebSettings webSettings = wv_in_school.getSettings();
        //getSettings()方法是设置实例的属性，setJavaScriptEnabled()方法只是其中一种，
        // 在当前WebView中支持JavaScript脚本，结果为true
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件。若为false，则该WebView不可缩放
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //在当前WebView中在跳转下一个网页的时，仍然是在当前WebView显示，否则就打开系统浏览器
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        wv_in_school.setWebViewClient(new WebViewClient());
        //在当前WebView中打开的网址
        wv_in_school.loadUrl("http://www.qnzy.net");
        //设置不用系统浏览器打开,直接显示在当前Webview
        wv_in_school.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 点击返回上一页面，而不是退出浏览器
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果有历史记录，就检查关键事件是否有后退键
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_in_school.canGoBack()) {
            // 返回键退回
            wv_in_school.goBack();
            return true;
        }
        // 如果它不是后退键或者没有网页历史，就会退出
        // 系统默认行为（可能退出活动）
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 销毁Webview,避免内存泄漏
     */
    @Override
    protected void onDestroy() {
        if (wv_in_school != null) {
            wv_in_school.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wv_in_school.clearHistory();

            ((ViewGroup) wv_in_school.getParent()).removeView(wv_in_school);
            wv_in_school.destroy();
            wv_in_school = null;
        }
        super.onDestroy();
    }
}
