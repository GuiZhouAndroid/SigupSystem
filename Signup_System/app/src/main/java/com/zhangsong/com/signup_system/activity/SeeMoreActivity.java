package com.zhangsong.com.signup_system.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.adapter.RecyclerView.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreActivity extends MySwipeBackActivity {
    private Toolbar toolbar_constant;
    private TextView tv_toolbar_title;//标题
    private RecyclerViewAdapter mAdapter;
    private RecyclerView see_more_recyclerView;
    private LinearLayoutManager mLayoutManager;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        initView();//初始化View
        initToolbar();//初始化标题栏Toolbar
        initRecycleView();
    }

    private void initRecycleView() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item" + i);
        }
        //开始设置RecyclerView
        see_more_recyclerView=(RecyclerView)findViewById(R.id.see_more_recyclerView);
        //设置固定大小
        see_more_recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
        see_more_recyclerView.setLayoutManager(mLayoutManager);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.recyclerview_shape));
        see_more_recyclerView.addItemDecoration(divider);
        //创建适配器，并且设置
        mAdapter = new RecyclerViewAdapter(this,list);
        mAdapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        Toast.makeText(SeeMoreActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(SeeMoreActivity.this, "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(SeeMoreActivity.this, "3", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(SeeMoreActivity.this, "4", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(SeeMoreActivity.this, "5", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(SeeMoreActivity.this, "6", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //设置添加或删除item时的动画，这里使用默认动画
        see_more_recyclerView.setItemAnimator(new DefaultItemAnimator());
        see_more_recyclerView.setAdapter(mAdapter);

    }

    /**
     * 初始化View
     */
    private void initView() {
        toolbar_constant= (Toolbar) findViewById(R.id.toolbar_constant);//Toolbar
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.sign_Encyclopedias);
    }
    /**
     * 初始化标题栏Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar_constant);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    /**
     *  从密码返回主页
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
     * Toolbar 的Navigation Button 添加点击事件,用于返回首页
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
    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
