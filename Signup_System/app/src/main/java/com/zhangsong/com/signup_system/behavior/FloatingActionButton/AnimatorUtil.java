package com.zhangsong.com.signup_system.behavior.FloatingActionButton;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

public class AnimatorUtil {
    private static AccelerateDecelerateInterpolator LINEAR_INTERRPLATOR =new AccelerateDecelerateInterpolator();
    public static void showFab(View view, MyBehavior.AnimateListener ...listener){
        if (listener.length!=0){
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(400)
                    .setInterpolator(LINEAR_INTERRPLATOR)
                    .setListener(listener[0])
                    .start();
        }else {
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(400)
                    .setInterpolator(LINEAR_INTERRPLATOR)
                    .start();
        }

    }
    public static void hideFab(View view, MyBehavior.AnimateListener listener){
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(400)
                .setInterpolator(LINEAR_INTERRPLATOR)
                .setListener(listener)
                .start();
    }
    private static LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

    private static AccelerateInterpolator LINER_INTERPOLATOR = new AccelerateInterpolator();


    /**
     * 显示view
     *
     * @param view View
     * @param viewPropertyAnimatorListener ViewPropertyAnimatorListener
     */
    public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(800)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .start();
    }

    /**
     * 隐藏view
     *
     * @param view View
     * @param viewPropertyAnimatorListener ViewPropertyAnimatorListener
     */
    public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(800)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

    /**
     * 显示view
     *
     * @param view View
     * @param viewPropertyAnimatorListener ViewPropertyAnimatorListener
     */
    public static void translateShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(0)
                .setDuration(400)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .start();
    }

    /**
     * 隐藏view
     *
     * @param view View
     * @param viewPropertyAnimatorListener ViewPropertyAnimatorListener
     */
    public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(350)
                .setDuration(400)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }
}