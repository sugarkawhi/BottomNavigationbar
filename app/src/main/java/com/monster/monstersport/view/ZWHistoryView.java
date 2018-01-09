package com.monster.monstersport.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.monster.monstersport.R;

/**
 * Created by ZhaoZongyao on 2018/1/8.
 */

public class ZWHistoryView extends ViewGroup {
    private static final String TAG = "ZWHistoryView";
    //min length
    private static final int MIN_LENGTH = 3;
    //every column contain 3
    private static final int COLUMN_NUM = 3;
    private int mGap;
    private float mAspectRatio;

    private int mSmallWidth;
    private int mSmallHeight;
    private int mBigWidth;
    private int mBigHeight;


    public ZWHistoryView(Context context) {
        this(context, null);
    }

    public ZWHistoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZWHistoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZWHistoryView);
        mGap = array.getDimensionPixelSize(R.styleable.ZWHistoryView_xhh_zw_gap, 10);
        mAspectRatio = array.getFloat(R.styleable.ZWHistoryView_xhh_zw_aspect_ratio, 1.43f);
        array.recycle();
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
                widthSize = 400;
                break;
        }
        mSmallWidth = (widthSize - getPaddingLeft() - getPaddingRight() - 2 * mGap) / 3;
        mSmallHeight = (int) (mSmallWidth * mAspectRatio);
        mBigWidth = mSmallWidth * 2 + mGap;
        mBigHeight = (int) (mBigWidth * mAspectRatio);
        int heightSize = mBigHeight + getPaddingTop() + getPaddingBottom();
        if (getChildCount() > MIN_LENGTH) {
            heightSize += (mGap + mSmallHeight) * getRows();
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        doMeasureChildren(widthSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    private void doMeasureChildren(int widthSize) {
        View child;
        LayoutParams params;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            params = child.getLayoutParams();
            switch (i) {
                case 0:
                    params.width = mBigWidth;
                    params.height = mBigHeight;
                    break;
                case 1:
                case 2:
                    params.width = widthSize - getPaddingLeft() - getPaddingRight() - mGap - mBigWidth;
                    params.height = (mBigHeight - mGap) / 2;
                    break;
                default:
                    params.width = mSmallWidth;
                    params.height = mSmallHeight;
                    break;
            }
            Log.e(TAG, "doMeasureChildren position=" + i + "  width=" + params.width + "  height=" + params.height);
            child.setLayoutParams(params);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        doLayoutChildren(paddingLeft, paddingRight, paddingTop, paddingBottom);
    }

    private void doLayoutChildren(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            switch (i) {
                case 0:
                    child.layout(paddingLeft, paddingTop, mBigWidth + paddingLeft, mBigHeight + paddingTop);
                    break;
                case 1:
                    child.layout(paddingLeft + mBigWidth + mGap, paddingTop, getWidth() - paddingRight, paddingTop + mBigHeight / 2);
                    break;
                case 2:
                    child.layout(paddingLeft + mBigWidth + mGap, paddingTop + mGap + (mBigHeight - mGap) / 2, getWidth() - paddingRight, mBigHeight + paddingTop);
                    break;
                default:
                    int l, t, r, b;
                    int row = getChildInRow(i);
                    l = paddingLeft + (mSmallWidth + mGap) * getChildInColumn(i);
                    t = paddingTop + mBigHeight + mGap * row + mSmallHeight * (row - 1);
                    r = l + mSmallWidth;
                    b = t + mSmallHeight;
                    child.layout(l, t, r, b);
                    Log.e(TAG, "doLayoutChildren index = " + i + " row = " + row + " l=" + l + " t=" + t + " r=" + r + " b=" + b);
                    break;
            }
        }
    }

    /**
     * get rows except first row
     */
    private int getRows() {
        return (getChildCount() - MIN_LENGTH) / COLUMN_NUM + ((getChildCount() - MIN_LENGTH) % COLUMN_NUM == 0 ? 0 : 1);
    }

    /**
     * get child in which row
     * Attention: row start with 1
     */
    private int getChildInRow(int index) {
        return index / COLUMN_NUM;
    }

    /**
     * get child in which column
     * Attention: column start with 0
     */
    private int getChildInColumn(int index) {
        return index % COLUMN_NUM;
    }

    public void setAdapter(Adapter adapter) {
        removeAllViews();
        Log.e(TAG, "setAdapter");
        int i = 0;
        View child;
        for (; i < adapter.getCount(); i++) {
            child = adapter.getView(i);
            addView(child, generateDefaultLayoutParams());
        }
        requestLayout();
        invalidate();
    }


    public static abstract class Adapter {

        protected abstract View getView(int position);

        protected abstract int getCount();
    }

}
