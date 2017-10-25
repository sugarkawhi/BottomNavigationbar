package com.monster.monstersport.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZhaoZongyao on 2017/10/24.
 */

public class JiKeLoadingView extends View {
    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int mRadius = 30;
    private RectF mRectF;
    private Path mPath;
    private Path mDstPath;
    private PathMeasure mPathMeasure;
    private int mStrokeWidth = 15;

    public JiKeLoadingView(Context context) {
        this(context, null);
    }

    public JiKeLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiKeLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.parseColor("#4682B4"));
        mRectF = new RectF();
        mPath = new Path();
        mDstPath = new Path();
        mPathMeasure = new PathMeasure();
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
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRectF.set(mWidth / 2 - mRadius * 2, mHeight / 2 - mRadius, mWidth / 2, mHeight / 2 + mRadius);
        mPath.addArc(mRectF, 0, 60);
        mPaint.setColor(Color.parseColor("#F4F4F4"));
        mPath.reset();
        mPath.moveTo(mWidth / 2, mHeight / 2);
        mPath.lineTo(mWidth / 2, mHeight / 2 - mRadius * 3 / 2);
        canvas.drawPath(mPath, mPaint);

        mDstPath.reset();
        mPaint.setColor(Color.parseColor("#4682B4"));
        mPathMeasure.setPath(mPath, false);
        mPathMeasure.getSegment(0, (float) (mPathMeasure.getLength() * 0.5), mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);
    }


}
