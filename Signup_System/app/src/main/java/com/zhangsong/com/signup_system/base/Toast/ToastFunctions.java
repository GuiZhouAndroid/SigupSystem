package com.zhangsong.com.signup_system.base.Toast;

import android.content.Context;
import android.view.View;

/**
 * Toast的抽象类，主要用来规范有哪些方法，以便于维护
 *
 */
public interface ToastFunctions {

    /**
     * 直接显示Toast
     *
     * @param context
     *            上下文
     * @param text
     *            文本信息
     * @param duration
     *            显示时长
     */
    public void immediateToast(Context context, String text, int duration);

    /**
     * 自定义位置的Toast
     *
     * @param context
     *            上下文
     * @param text
     *            文本信息
     * @param duration
     *            显示时长
     * @param gravity
     *            位置
     */
    public void ToastLocation(Context context, String text, int duration,
                              int gravity);

    /**
     * 带图片的Toast
     *
     * @param context
     *            上下文
     * @param text
     *            文本信息
     * @param duration
     *            显示时长
     * @param resId
     *            图片资源id
     */
    public void ToastWithPic(Context context, String text, int duration,
                             int resId);

    /**
     * 自定义的Toast
     *
     * @param context
     *            上下文
     * @param text
     *            文本信息
     * @param duration
     *            显示时长
     * @param view
     *            布局文件xml文件的view对象 <br><br>
     *
     *            View的创建如下： <br>
     *            //自定义Toast主要是利用LayoutInflater来实现<br>
     *            LayoutInflater layoutInflater = getLayoutInflater(); <br>
     *            //加载一个布局文件，自定义的布局文件 View<br>
     *            view = layoutInflater.inflate(layoutId,null);<br>
     */
    public void ToastBySelf(Context context, String text, int duration,
                            View view);
}
