package com.zhangsong.com.signup_system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.utils.StatusBar.StatusBarUtils;

public class SettingActivity extends MySwipeBackActivity {
    private Toolbar toolbar_setting;
    private TextView tv_toolbar_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();//初始化View
        initToolbar();//初始化标题栏Toolbar
    }

    /**
     * 初始化View
     */
    private void initView() {
        toolbar_setting= (Toolbar) findViewById(R.id.toolbar_constant);
        tv_toolbar_title= (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.drawer_menu_setting_title);
    }

    /**
     * 初始化标题栏Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar_setting);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     *  从软件设置返回主页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    /**
     * Toolbar 的Navigation Button 添加点击事件,用于打开或关闭侧滑抽屉
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
              finish();
                break;
        }
        return true;
    }
}
