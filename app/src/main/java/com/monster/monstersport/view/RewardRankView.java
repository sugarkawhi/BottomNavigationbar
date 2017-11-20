package com.monster.monstersport.view;

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

import com.monster.monstersport.R;


/**
 * 打赏榜background
 *
 * @author zhzy
 * @date 2017/11/17
 */

public class RewardRankView extends View {


    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mPath;
    private Path mStrokePath;
    private RectF mRectF;
    private RectF mRightRectF;

    private float mCircleRadius;
    private float mRightCircleRadius;
    private int mWidth;
    private int mColor;
    private float mShadowRadius;
    private int mShadowColor;

    public RewardRankView(Context context) {
        this(context, null);
    }

    public RewardRankView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RewardRankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RewardRankView);
        mCircleRadius = array.getDimensionPixelSize(R.styleable.RewardRankView_rrv_circle_radius, 30);
        mShadowRadius = array.getDimensionPixelSize(R.styleable.RewardRankView_rrv_shadow_radius, 5);
        mColor = array.getColor(R.styleable.RewardRankView_rrv_color, Color.WHITE);
        mShadowColor = array.getColor(R.styleable.RewardRankView_rrv_shadow_color, Color.LTGRAY);
        array.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPath = new Path();

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(mShadowColor);
        mStrokePaint.setStrokeWidth(1);
        mStrokePaint.setShadowLayer(mShadowRadius, 0, 3, mShadowColor);

        mStrokePath = new Path();
        mRectF = new RectF(0, 0, mCircleRadius * 2, mCircleRadius * 2);
        mRightCircleRadius = mCircleRadius * 1.5f / 2;
        mRightRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (widthMeasureMode) {
            case MeasureSpec.AT_MOST:
                mWidth = 500;
                break;
            case MeasureSpec.EXACTLY:
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED:
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                mWidth = 0;
                break;
        }

        mRightRectF.left = mWidth - mRightCircleRadius * 2;
        mRightRectF.top = 0;
        mRightRectF.right = mWidth;
        mRightRectF.bottom = mRightCircleRadius * 2;

        setMeasuredDimension(mWidth, (int) (mCircleRadius * 2));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //画左边的圆
        mPath.addArc(mRectF, -90, -240);
        mPath.lineTo(mWidth - mRightCircleRadius, mRightCircleRadius * 2);
        mPath.addArc(mRightRectF,
                90,
                -180);
        mPath.lineTo(mCircleRadius, 0);

        mStrokePath.addPath(mPath);

        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mStrokePath, mStrokePaint);
    }
}
