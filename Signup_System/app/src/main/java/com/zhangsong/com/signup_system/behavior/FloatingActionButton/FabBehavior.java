package com.zhangsong.com.signup_system.behavior.FloatingActionButton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

public class FabBehavior extends FloatingActionButton.Behavior {
    int offsetY = 0;
    AnimatorUtils animatorUtils;

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        animatorUtils = new AnimatorUtils();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        //如果NSChild传过来的axes是竖直方向SCROLL_AXIS_VERTICAL则返回true
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            // 获取控件左上角到父控件底部的偏移量
            offsetY = coordinatorLayout.getMeasuredHeight() - child.getTop();
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull FloatingActionButton child, @NonNull View target,
                                  int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        // dy > 0 ,表示手指上滑，页面上拉, 查看更多内容，FAB要下滑隐藏
        if (dy > 0) {
            animatorUtils.startHindAnimator(child, offsetY);
            return;
        }
        // dy < 0 ,表示手指下滑, 页面下拉, 查看前面的内容, FAB上滑显示
        if (dy < 0) {
            animatorUtils.startShowAnimator(child, 0);
            return;
        }
    }


    class AnimatorUtils {

        boolean isAnimator = false;

        public void startHindAnimator(View view, int offsetY) {
            if (isAnimator) {
                return;
            }
            ViewCompat.animate(view).translationY(offsetY).setDuration(200)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            isAnimator = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimator = false;
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            isAnimator = false;
                        }
                    })
                    .start();
        }

        public void startShowAnimator(View view, int offsetY) {
            if (isAnimator) {
                return;
            }
            ViewPropertyAnimatorCompat showPropertyAnimatorCompat = ViewCompat
                    .animate(view)
                    .translationY(offsetY)
                    .setDuration(200)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            isAnimator = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimator = false;
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            isAnimator = false;
                        }
                    });
            showPropertyAnimatorCompat.start();
        }
    }
}

