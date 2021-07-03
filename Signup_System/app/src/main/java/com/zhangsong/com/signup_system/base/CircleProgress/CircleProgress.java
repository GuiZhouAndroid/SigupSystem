package com.zhangsong.com.signup_system.base.CircleProgress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.zhangsong.com.signup_system.R;

public class CircleProgress extends View {
    private Context mContext;
    //默认大小
    private int mDefaultSize;
    //是否开启抗锯齿
    private boolean antiAlias;
    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;
    private float mHintOffset;
    //绘制单位
    private TextPaint mUnitPaint;
    private CharSequence mUnit;
    private int mUnitColor;
    private float mUnitSize;
    private float mUnitOffset;
    //绘制数值
    private TextPaint mValuePaint;
    //绘制圆弧
    private Paint mArcPaint;
    private float mArcWidth;
    private RectF mRectF;
    //渐变的颜色是360度，如果只显示270，那么则会缺失部分颜色
    private SweepGradient mSweepGradient;
    private int[] mGradientColors = {Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE, Color.MAGENTA};
    //属性动画
    public ValueAnimator mAnimator;
    //绘制背景圆弧
    private Paint mBgArcPaint;
    private int mBgArcColor;
    public float mBgArcWidth;
    //圆心坐标，半径
    private Point mCenterPoint;
    private float mRadius;
    private float mTextOffsetPercentInRadius;
    //倒计时
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;
    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mDefaultSize = MiscUtil.dipToPx(mContext,150);
        mAnimator = new ValueAnimator();
        mRectF = new RectF();
        mCenterPoint = new Point();
        initAttrs(attrs);
        initPaint();
    }
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        //开启抗锯齿
        antiAlias = typedArray.getBoolean(R.styleable.CircleProgressBar_antiAlias, true);
        mHint = typedArray.getString(R.styleable.CircleProgressBar_hint);
        mHintColor = typedArray.getColor(R.styleable.CircleProgressBar_hintColor, Color.WHITE);
        mHintSize = typedArray.getDimension(R.styleable.CircleProgressBar_hintSize, 0);
        //第三文本字体显示
        mUnit = typedArray.getString(R.styleable.CircleProgressBar_unit);
        //第二，第三文本字体颜色
        mUnitColor = typedArray.getColor(R.styleable.CircleProgressBar_unitColor, Color.BLACK);
        //第二，第三文本字体大小
        mUnitSize = typedArray.getDimension(R.styleable.CircleProgressBar_unitSize, 20);
        //圆环粗细
        mArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_arcWidth, 4);
        //圆圈背景
        mBgArcColor = typedArray.getColor(R.styleable.CircleProgressBar_bgArcColor, Color.WHITE);
        //圆圈中的内容边距
        mTextOffsetPercentInRadius = typedArray.getFloat(R.styleable.CircleProgressBar_textOffsetPercentInRadius, 0.45f);
        //开始运行倒计时
       mCountdownTime = typedArray.getInteger(R.styleable.CircleProgressBar_countdownTime, 3);
    }
    public void setAddCountDownListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }
    public interface OnCountDownFinishListener {
        void countDownFinished();
    }
    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;

    }
    /**
     * 开始倒计时
     */
    public void startCountDown() {
        setClickable(false);
        ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                mCurrentProgress = (int) (360 * (i / 100f));
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null) {
                    mListener.countDownFinished();
                }
                setClickable(true);
            }
        });
    }
    private void initPaint() {
        mHintPaint = new TextPaint();
        // 设置抗锯齿,会消耗较大资源，绘制图形速度会变慢。
        mHintPaint.setAntiAlias(antiAlias);
        // 设置绘制文字大小
        mHintPaint.setTextSize(mHintSize);
        // 设置画笔颜色
        mHintPaint.setColor(mHintColor);
        // 从中间向两边绘制，不需要再次计算文字
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint = new TextPaint();
        mValuePaint.setAntiAlias(antiAlias);
        // 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
        mValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mValuePaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new TextPaint();
        mUnitPaint.setAntiAlias(antiAlias);
        mUnitPaint.setTextSize(mUnitSize);
        mUnitPaint.setColor(mUnitColor);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(antiAlias);
        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
        mArcPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔粗细
        mArcPaint.setStrokeWidth(mArcWidth);
        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND,或方形样式 Cap.SQUARE
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgArcPaint = new Paint();
        mBgArcPaint.setAntiAlias(antiAlias);
        mBgArcPaint.setColor(mBgArcColor);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mBgArcWidth);
        mBgArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, mDefaultSize),
                MiscUtil.measure(heightMeasureSpec, mDefaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //求圆弧和背景圆弧的最大宽度
        float maxArcWidth = Math.max(mArcWidth, mBgArcWidth);
        //求最小值作为实际值
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight() - 2 * (int) maxArcWidth,
                h - getPaddingTop() - getPaddingBottom() - 2 * (int) maxArcWidth);
        //减去圆弧的宽度，否则会造成部分圆弧绘制在外围
        mRadius = minSize / 2;
        //获取圆的相关参数
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - maxArcWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - maxArcWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + maxArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + maxArcWidth / 2;
        //计算文字绘制时的 baseline
        //由于文字的baseline、descent、ascent等属性只与textSize和typeface有关，所以此时可以直接计算
        //若hint、unit由同一个画笔绘制或者需要动态设置文字的大小，则需要在每次更新后再次计算
        mHintOffset = mCenterPoint.y - mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mHintPaint);
        mUnitOffset = mCenterPoint.y + mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mUnitPaint);
        updateArcPaint();
    }
    private float getBaselineOffsetFromY(Paint paint) {
        return MiscUtil.measureTextHeight(paint) / 2;
    }

    /**
     * 开始绘画
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        /**
         *圆环
         */
        //颜色
        mArcPaint.setColor(mBgArcColor);
        //空心
        mArcPaint.setStyle(Paint.Style.STROKE);
        //宽度
        mArcPaint.setStrokeWidth(mArcWidth);
        canvas.drawArc(mRectF, -90, mCurrentProgress - 360, false, mArcPaint);
        //绘制文本
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
       // String text = (int) (mCountdownTime - (mCountdownTime / 60f * mCurrentProgress / 6f)) + "";
        String text = mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime) + "";
        textPaint.setTextSize(mUnitSize);
        textPaint.setColor(mUnitColor);
        //文字居中显示
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }
    /**
     * 绘制内容文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        // 计算文字宽度，由于Paint已设置为居中绘制，故此处不需要重新计算
        // float textWidth = mValuePaint.measureText(mValue.toString());
        // float x = mCenterPoint.x - textWidth / 2;
        if (mHint != null) {
            canvas.drawText(mHint.toString(), mCenterPoint.x, mHintOffset, mHintPaint);
        }
        if (mUnit != null) {
            canvas.drawText(mUnit.toString(), mCenterPoint.x, mUnitOffset, mUnitPaint);
        }
    }
    /**
     * 更新圆弧画笔
     */
    private void updateArcPaint() {
        // 设置渐变
        mSweepGradient = new SweepGradient(mCenterPoint.x, mCenterPoint.y, mGradientColors, null);
        mArcPaint.setShader(mSweepGradient);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //释放资源
    }

}
