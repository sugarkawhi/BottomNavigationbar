package com.monster.monstersport.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.monster.monstersport.R;

/**
 * Created by ZhaoZongyao on 2017/10/24.
 */

public class JiKeView extends View {
    String TAG = "JiKeView";

    private int mAngle;
    private int mGapAngle;
    private float mLineTimes = 1.3f;
    private int mLetterColor;
    private int mLetterBackgroundColor;
    private int mCornerRadius = 50;
    private int mStrokeWidth = 25;
    //加载状态转圈速度
    private int mAnimDuration;

    //正常状态  显示字母J
    private static final int STATE_NORMAL = 1;
    //转换状态
    private static final int STATE_TRANSFORM = 3;
    //加载状态 变为缺口圆圈
    private static final int STATE_LOADING = 4;
    //加载完成状态 缺口圆圈变小
    private static final int STATE_COMPLETE = 5;

    private int mCurrentState = STATE_NORMAL;

    private Paint mLetterPaint;
    private Paint mBackgroundPaint;
    //当前进度 范围 0f~1f
    private float mPercent = 0f;

    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private Path mLetterPath;
    private Path mBackgroundPath;

    private ValueAnimator mXAnimator;
    private ValueAnimator mLoadingAnimator;

    public JiKeView(Context context) {
        this(context, null);
    }

    public JiKeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiKeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.JiKeView);
        mAngle = array.getInt(R.styleable.JiKeView_jk_angle, 60);
        mGapAngle = array.getInt(R.styleable.JiKeView_jk_gap_angle, 10);
        mAnimDuration = array.getInt(R.styleable.JiKeView_jk_duration, 500);
        mLetterColor = array.getColor(R.styleable.JiKeView_jk_letter_color, Color.parseColor("#4682B4"));
        mLetterBackgroundColor = array.getColor(R.styleable.JiKeView_jk_letter_background_color, Color.parseColor("#F4F4F4"));
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.JiKeView_jk_stroke_width, 25);
        mCornerRadius = array.getDimensionPixelSize(R.styleable.JiKeView_jk_corner_radius, 50);
        array.recycle();

        if (mGapAngle < 0)
            mGapAngle = Math.abs(mGapAngle);
        if (mGapAngle >= 90)
            mGapAngle = 10;
        if (mAngle < 0)
            mAngle = Math.abs(mAngle);
        if (mAngle >= 90)
            mAngle = 90;
        init();
    }


    private void init() {
        mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLetterPaint.setStyle(Paint.Style.STROKE);
        mLetterPaint.setStrokeWidth(mStrokeWidth);
        mLetterPaint.setColor(mLetterColor);
        mRectF = new RectF();
        mLetterPath = new Path();
        mBackgroundPath = new Path();
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mLetterBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(mStrokeWidth);
        initXAnimator();
        initLoadingAnimator();
    }


    private void initXAnimator() {
        float offsetValue = mAngle / (360f - mGapAngle);
        int xDuration = (int) (mAnimDuration * (1 - offsetValue));
        mXAnimator = ValueAnimator.ofFloat(0, 1f);
        mXAnimator.setDuration(xDuration);
        mXAnimator.setInterpolator(new LinearInterpolator());
        mXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mXAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_LOADING;
                if (!mLoadingAnimator.isStarted()) {
                    mLoadingAnimator.start();
                }
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initLoadingAnimator() {
        mLoadingAnimator = ValueAnimator.ofFloat(0, 1f);
        mLoadingAnimator.setDuration(mAnimDuration);
        mLoadingAnimator.setInterpolator(new LinearInterpolator());
        mLoadingAnimator.setRepeatCount(-1);
        mLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = 200;
        } else {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        mHeight = mWidth;

        if (mCornerRadius > mWidth / 2) {
            mCornerRadius = mWidth / 2 - mStrokeWidth / 2;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentState) {
            case STATE_NORMAL:
                drawNormal(canvas);
                break;
            case STATE_TRANSFORM:
                drawTransform(canvas);
                break;
            case STATE_LOADING:
                drawLoading(canvas);
                break;
            case STATE_COMPLETE:
                drawComplete(canvas);
                break;
            default:
                drawNormal(canvas);
        }
    }

    /**
     * 正常状态
     */
    private void drawNormal(Canvas canvas) {
        //先画拐角
        mLetterPath.reset();
        mRectF.set(mWidth / 2 - mCornerRadius, mHeight / 2 - mCornerRadius, mWidth / 2 + mCornerRadius, mHeight / 2 + mCornerRadius);
        mLetterPath.addArc(mRectF, mAngle, -mAngle);
        //再画进度
        mLetterPath.moveTo(mWidth / 2 + mCornerRadius, mHeight / 2);
        mLetterPath.lineTo(mWidth / 2 + mCornerRadius, mHeight / 2 - (mCornerRadius * mLineTimes) * mPercent);
        canvas.drawPath(mLetterPath, mLetterPaint);
        //最后画背景
        mBackgroundPath.reset();
        mBackgroundPath.moveTo(mWidth / 2 + mCornerRadius, mHeight / 2 - (mCornerRadius * mLineTimes) * mPercent);
        mBackgroundPath.lineTo(mWidth / 2 + mCornerRadius, mHeight / 2 - mCornerRadius * mLineTimes);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
    }


    /**
     * STATE_TRANSFORM
     */
    private void drawTransform(Canvas canvas) {
        mLetterPath.reset();
        float value = (float) mXAnimator.getAnimatedValue();
        mLetterPath.addArc(mRectF, 0, mAngle);
        mLetterPath.addArc(mRectF, mAngle, (360 - mGapAngle - mAngle) * value);
        mLetterPath.moveTo(mWidth / 2 + mCornerRadius, mHeight / 2);
        mLetterPath.lineTo(mWidth / 2 + mCornerRadius, mHeight / 2 - (mCornerRadius * mLineTimes) * (1 - value));
        canvas.drawPath(mLetterPath, mLetterPaint);
    }

    /**
     * STATE_LOADING
     */
    private void drawLoading(Canvas canvas) {
        mLetterPath.reset();
        mRectF.set(mWidth / 2 - mCornerRadius, mHeight / 2 - mCornerRadius, mWidth / 2 + mCornerRadius, mHeight / 2 + mCornerRadius);
        float value = (float) mLoadingAnimator.getAnimatedValue();
        mLetterPath.addArc(mRectF, 360 * value, 360 - mGapAngle);
        canvas.drawPath(mLetterPath, mLetterPaint);
    }

    /**
     * STATE_COMPLETE
     */
    private void drawComplete(Canvas canvas) {
        mLetterPaint.setStrokeWidth(mStrokeWidth / 3);
        mLetterPath.reset();
        mRectF.set(mWidth / 2 - mCornerRadius, mHeight / 2 - mCornerRadius, mWidth / 2 + mCornerRadius, mHeight / 2 + mCornerRadius);
        float value = (float) mLoadingAnimator.getAnimatedValue();
        mLetterPath.addArc(mRectF, 360 * value, 360 - mGapAngle);
        canvas.drawPath(mLetterPath, mLetterPaint);
    }

    /**
     * 设置当前进度
     * percent 值越大，那么dstPath长度越短
     */
    public void setPercent(float percent) {
        if (percent < 0)
            return;
        if (mCurrentState != STATE_NORMAL)
            return;
        if (percent < 1) {
            this.mPercent = percent;
        }
        if (percent == 1) {
            mCurrentState = STATE_TRANSFORM;
            mXAnimator.start();
        }
        invalidate();
    }

    /**
     * 设置加载完成
     */
    public void complete() {
        if (mCurrentState == STATE_LOADING) {
            this.mCurrentState = STATE_COMPLETE;
            invalidate();
        } else {
            reset();
        }
    }


    /**
     * 重置状态、进度
     */
    public void reset() {
        this.mPercent = 0;
        mCurrentState = STATE_NORMAL;
        mXAnimator.cancel();
        mLoadingAnimator.cancel();
        mLetterPaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

}
