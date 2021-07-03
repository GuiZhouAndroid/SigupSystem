package com.zhangsong.com.signup_system.behavior.FloatingActionButton;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class MyBehavior extends FloatingActionButton.Behavior {
    private boolean isAnimateIng = false;   // 是否正在动画
    private boolean isShow = true;  // 是否已经显示

    public MyBehavior(Context context, AttributeSet attrs) {
        super();
    }
    //只有当返回值为true才会执行下面的方法,例如onNestedScroll
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
                ||axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //滑动时调用该方法
    //dxConsumed: 表示view消费了x方向的距离长度
    //dyConsumed: 表示view消费了y方向的距离长度
    //消费的距离是指实际滚动的距离
    /*
     *  coordinatorLayout - the CoordinatorLayout parent of the view this Behavior is associated with
        child - the child view of the CoordinatorLayout this Behavior is associated with
        target - the descendant view of the CoordinatorLayout performing the nested scroll
        dxConsumed - horizontal pixels consumed by the target's own scrolling operation
        dyConsumed - vertical pixels consumed by the target's own scrolling operation
        dxUnconsumed - horizontal pixels not consumed by the target's own scrolling operation, but requested by the user
        dyUnconsumed - vertical pixels not consumed by the target's own scrolling operation, but requested by the user
        type - the type of input which cause this scroll event
    */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //界面向下滑动,fab动画结束,且正在显示
        //隐藏Fab
        if ((dyConsumed>0||dyUnconsumed>0)&&!isAnimateIng&&isShow){
            AnimatorUtil.hideFab(child,new AnimateListener());
        }
        //界面向上滑动,fab动画结束,且隐藏
        //显示Fab
        else if ((dyConsumed<0||dyUnconsumed<0)&&!isAnimateIng&&!isShow){
            AnimatorUtil.showFab(child,new AnimateListener());

        }

    }
    public class AnimateListener implements  Animator.AnimatorListener {


        @Override
        public void onAnimationStart(Animator animation) {
            isAnimateIng=true;
            isShow=!isShow;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimateIng=false;

        }

        @Override
        public void onAnimationCancel(Animator animation) {


        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
