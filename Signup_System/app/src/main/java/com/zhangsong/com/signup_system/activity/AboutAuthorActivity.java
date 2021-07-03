package com.zhangsong.com.signup_system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.transition.ChangeColor;
import com.zhangsong.com.signup_system.transition.ChangePosition;
import com.zhangsong.com.signup_system.transition.CommentEnterTransition;
import com.zhangsong.com.signup_system.transition.ShareElemEnterRevealTransition;
import com.zhangsong.com.signup_system.transition.ShareElemReturnChangePosition;
import com.zhangsong.com.signup_system.transition.ShareElemReturnRevealTransition;

public class AboutAuthorActivity extends MySwipeBackActivity {
    private Toolbar toolbar_myself;
    //转场
    View mBottomSendBar;
    View mTitleBarTxt;
    View mCommentBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        initToolbar();
        initTransfer();
    }
    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        toolbar_myself= (Toolbar) findViewById(R.id.toolbar_myself);
        setSupportActionBar(toolbar_myself);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    /**
     * 初始化转场
     */
    private void initTransfer() {
        mCommentBox = findViewById(R.id.comment_box);
        mTitleBarTxt = findViewById(R.id.appbar_top);
        mBottomSendBar = findViewById(R.id.bottom_send_bar);
        setTransition();
    }

    /**
     * 设置转场样式
     */
    private void setTransition() {
        // 顶部 title 和底部输入框的进入动画
        getWindow().setEnterTransition(new CommentEnterTransition(this, mTitleBarTxt, mBottomSendBar));
        getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
        getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());

    }
    /**
     * 分享 元素 进入动画
     * @return
     */
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();
        Transition changePos = new ChangePosition();
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_box);
        transitionSet.addTransition(changePos);
        Transition revealTransition = new ShareElemEnterRevealTransition(mCommentBox);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(R.id.comment_box);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(300);
        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.black_85_alpha), getResources().getColor(R.color.white));
        changeColor.addTarget(R.id.comment_box);
        changeColor.setDuration(350);
        transitionSet.addTransition(changeColor);
        transitionSet.setDuration(900);
        return transitionSet;
    }

    /**
     * 分享元素返回动画
     * @return
     */
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();
        Transition changePos = new ShareElemReturnChangePosition();
        changePos.addTarget(R.id.comment_box);
        transitionSet.addTransition(changePos);
        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.white), getResources().getColor(R.color.black_85_alpha));
        changeColor.addTarget(R.id.comment_box);
        transitionSet.addTransition(changeColor);
        Transition revealTransition = new ShareElemReturnRevealTransition(mCommentBox);
        revealTransition.addTarget(R.id.comment_box);
        transitionSet.addTransition(revealTransition);
        transitionSet.setDuration(900);
        return transitionSet;
    }
    /**
     *  从关于作者返回主页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //finish();
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
                //finish();
                break;
        }
        return true;
    }

}
