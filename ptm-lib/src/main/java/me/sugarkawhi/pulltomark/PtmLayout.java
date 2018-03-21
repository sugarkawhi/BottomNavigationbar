package me.sugarkawhi.pulltomark;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Pull To Mark Layout
 * 下拉添加书签
 * Created by ZhaoZongyao on 2018/3/20.
 */

public class PtmLayout extends ViewGroup {

    private View mHeaderView;
    private View mContentView;
    private int mHeaderId;
    private int mContentId;
    private int mHeaderHeight;

    //滑动产生距离
    private int mScaledTouchSlop;
    //起始点坐标
    private int mStartX, mStartY;
    //实时触摸点
    private int mTouchX, mTouchY;
    //
    private int mMoveX, mMoveY;
    private boolean isMoveState;

    public PtmLayout(Context context) {
        this(context, null);
    }

    public PtmLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtmLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PtmLayout);
        mHeaderId = array.getResourceId(R.styleable.PtmLayout_ptr_header, 0);
        mContentId = array.getResourceId(R.styleable.PtmLayout_ptr_content, 0);
        array.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        layoutChildren();
    }

    /**
     * 对子View进行布局
     */
    private void layoutChildren() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }

        if (mContentView != null) {
            measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);

            mContentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("PtmLayout only contain two child ");
        } else if (childCount == 2) {
            if (mHeaderId != 0 && mHeaderView == null) {
                mHeaderView = findViewById(mHeaderId);
            }
            if (mContentId != 0 && mContentView == null) {
                mContentView = findViewById(mContentId);
            }

            if (mHeaderView == null || mContentView == null) {
                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof PtmUIHandler) {

                }
            }
        } else if (childCount == 1) {
            mContentView = getChildAt(0);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("Child View is Empty!");
            mContentView = errorView;
            addView(mContentView);
        }
        if (mHeaderView != null) {
            mHeaderView.bringToFront();
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        mTouchX = x;
        mTouchY = y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isMoveState) isMoveState = Math.abs(mTouchY - mStartY) > mScaledTouchSlop;
                if (!isMoveState) return false;
                //第一次构成滑动 分辨方向
                if (mMoveX == 0 && mMoveY == 0) {

                }
                mMoveX = x;
                mMoveY = y;
                return true;
        }
        return super.onTouchEvent(event);
    }
}
