package me.sugarkawhi.hyloadingview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * 加载动画
 *
 * @author zhzy
 * @date 2018/1/8
 */
public class SimpleHyLoadingView extends HyLoadingView {
    private static final String TAG = "SimpleHyLoadingView";
    private ValueAnimator mLoadingAnimator;
    private ValueAnimator mRiseAnimator;
    private ValueAnimator mDropAnimator;
    private float mAnimatedValue;
    private float mSuccessAnimatedValue;
    private float mDropAnimatedValue;
    private PathMeasure mPathMeasure;
    private Path mSuccessPath;
    private Path mSuccessDstPath;
    private Path mDropPath;
    private Path mDropDstPath;

    public SimpleHyLoadingView(Context context) {
        this(context, null);
    }

    public SimpleHyLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleHyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLoadingAnimator();
        initSuccessAnimator();
        initDropAnimator();
        mSuccessPath = new Path();
        mSuccessDstPath = new Path();
        mDropPath = new Path();
        mDropDstPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    private void initLoadingAnimator() {
        mLoadingAnimator = ValueAnimator.ofFloat(0, 1);
        mLoadingAnimator.setDuration(mLoadingDuration);
        mLoadingAnimator.setRepeatMode(ValueAnimator.RESTART);
        mLoadingAnimator.setInterpolator(new LinearInterpolator());
        mLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLoadingAnimator.start();
    }

    private void initSuccessAnimator() {
        mRiseAnimator = ValueAnimator.ofFloat(0, 1);
        mRiseAnimator.setDuration(500);
        mRiseAnimator.setInterpolator(new LinearInterpolator());
        mRiseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSuccessAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mRiseAnimator.addListener(new IAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDropAnimator.start();
            }
        });
    }

    private void initDropAnimator() {
        mDropAnimator = ValueAnimator.ofFloat(0, 1);
        mDropAnimator.setDuration(500);
        mDropAnimator.setInterpolator(new LinearInterpolator());
        mDropAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDropAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    void drawLoading(Canvas canvas) {
        mPaint.setColor(mLoadingColor);
        mLoadingAnimator.setRepeatCount(-1);
        mPath.reset();
        mPath.addArc(mRectF, 360 * mAnimatedValue, 300);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    void drawSuccess(Canvas canvas) {
        mPaint.setColor(mSuccessColor);
        mPath.reset();
        mPath.addArc(mRectF, 0, 360);
        canvas.drawPath(mPath, mPaint);
        mSuccessPath.reset();
        mSuccessDstPath.reset();
        mSuccessPath.moveTo(mWidth - mStokeWidth / 2, mWidth);
        mSuccessPath.quadTo(mWidth, mWidth / 2, mWidth / 2, 0);
        mPathMeasure.setPath(mSuccessPath, false);
        float length = mPathMeasure.getLength();
        float startD = length * mSuccessAnimatedValue;
        mPathMeasure.getSegment(startD, startD + 20, mSuccessDstPath, true);
        canvas.drawPath(mSuccessDstPath, mPaint);

        if (mSuccessAnimatedValue == 1 && mDropAnimatedValue < 1) {
            if (!mDropAnimator.isRunning()) {
                mDropAnimator.start();
            }
            mDropPath.reset();
            mDropPath.moveTo(mWidth / 2, 0);
            mDropPath.lineTo(mWidth / 2, mWidth);
            mPathMeasure.setPath(mDropPath, false);
            float dropLength = mPathMeasure.getLength();
            float dropStartD = dropLength * mDropAnimatedValue;
            mPathMeasure.getSegment(dropStartD, dropStartD + 20, mDropDstPath, true);
            canvas.drawPath(mDropDstPath, mPaint);
        }
    }

    @Override
    void drawError(Canvas canvas) {

    }

    @Override
    public void success() {
        super.success();
        mRiseAnimator.start();
    }

    @Override
    public void loading() {
        super.loading();
        mLoadingAnimator.start();
    }
}
