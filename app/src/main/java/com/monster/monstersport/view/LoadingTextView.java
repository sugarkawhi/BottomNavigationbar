package com.monster.monstersport.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZhaoZongyao on 2017/10/23.
 */

public class LoadingTextView extends View {


    private int mStrokeWidth = 10;

    private Path mPathLeft;
    private Path mPathRight;
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private PathMeasure measureLeft;
    private PathMeasure measureRight;

    public LoadingTextView(Context context) {
        this(context, null);
    }

    public LoadingTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = 100;
        } else {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        mHeight = (int) (mWidth * 1.6f);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPathLeft = new Path();
        mPathRight = new Path();

        measureLeft = new PathMeasure();
        measureRight = new PathMeasure();
        mDstLeft = new Path();
        mDstRight = new Path();
        initAnimator();
    }


    private Path mDstLeft;
    private Path mDstRight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathLeft.reset();
        mPathLeft.moveTo(0, 0);
        mPathLeft.lineTo(0, mHeight);
        mPathLeft.lineTo(mWidth / 3, mHeight);
        mPathLeft.lineTo(mWidth / 3, mHeight / 5 * 3);
        mPathLeft.lineTo(mWidth / 3 * 2, mHeight / 5 * 3);
        mPathLeft.lineTo(mWidth / 3 * 2, mHeight);
        mPathLeft.lineTo(mWidth, mHeight);

        mPathRight.reset();
        mPathRight.moveTo(mWidth, mHeight);
        mPathRight.lineTo(mWidth, 0);
        mPathRight.lineTo(mWidth / 3 * 2, 0);
        mPathRight.lineTo(mWidth / 3 * 2, mHeight / 5 * 2);
        mPathRight.lineTo(mWidth / 3, mHeight / 5 * 2);
        mPathRight.lineTo(mWidth / 3, 0);
        mPathRight.lineTo(0, 0);

        float value = (float) animator.getAnimatedValue();
        measureLeft.setPath(mPathLeft, false);
        measureRight.setPath(mPathRight, false);

        mDstLeft.reset();
        mDstRight.reset();
        measureLeft.getSegment(0, measureLeft.getLength() * value, mDstLeft, true);
        measureRight.getSegment(0, measureRight.getLength() * value, mDstRight, true);

        canvas.drawPath(mDstLeft, mPaint);
        canvas.drawPath(mDstRight, mPaint);

    }

    private ValueAnimator animator;

    private void initAnimator() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2500);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void start() {
        animator.start();
    }
}
