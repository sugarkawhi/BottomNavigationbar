package com.monster.monstersport.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.monster.monstersport.R;

/**
 * Created by ZhaoZongyao on 2018/1/8.
 */

public class ZWTimeLineView extends View {
    private static final String TAG = "ZWHistoryView";
    private Paint mPaint;
    private int mNodeColor;
    private int mLineColor;
    private int mNodeRadius;
    private int mLineWidth;
    private int mNodeRangeHeight;
    private int mWidth;

    //是否是第一个节点
    private boolean isFirstNode;

    public ZWTimeLineView(Context context) {
        this(context, null);
    }

    public ZWTimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZWTimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZWTimeLineView);
        mNodeRadius = array.getDimensionPixelSize(R.styleable.ZWTimeLineView_xhh_zwTL_nodeRadius, 10);
        mNodeRangeHeight = array.getDimensionPixelSize(R.styleable.ZWTimeLineView_xhh_zwTL_nodeRangeHeight, 50);
        mLineWidth = array.getDimensionPixelSize(R.styleable.ZWTimeLineView_xhh_zwTL_lineWidth, 5);
        mNodeColor = array.getColor(R.styleable.ZWTimeLineView_xhh_zwTL_nodeColor, Color.BLACK);
        mLineColor = array.getColor(R.styleable.ZWTimeLineView_xhh_zwTL_lineColor, Color.BLACK);
        isFirstNode = array.getBoolean(R.styleable.ZWTimeLineView_xhh_zwTL_isFirstNode, false);
        array.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                widthSize = 40;
                break;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                heightSize = 500;
                break;
        }
        mWidth = widthSize;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mNodeColor);
        int cX = mWidth / 2;
        int cY = mNodeRangeHeight / 2;
        canvas.drawCircle(cX, cY, mNodeRadius, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mLineColor);
        if (!isFirstNode) {
            canvas.drawLine(cX, 0, cX, mNodeRangeHeight / 2 - mNodeRadius, mPaint);
        }
        canvas.drawLine(cX, mNodeRangeHeight / 2 + mNodeRadius, cX, getHeight(), mPaint);

    }

    public void setFirstNode(boolean firstNode) {
        if (firstNode == isFirstNode) return;
        this.isFirstNode = firstNode;
        invalidate();
    }
}
