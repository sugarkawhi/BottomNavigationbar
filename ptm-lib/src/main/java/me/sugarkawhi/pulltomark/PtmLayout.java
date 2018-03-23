package me.sugarkawhi.pulltomark;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Pull To Mark Layout
 * 下拉添加书签
 * Created by ZhaoZongyao on 2018/3/20.
 */

public class PtmLayout extends ViewGroup {

    private static final String TAG = "PtmLayout";

    private PtmHeader mPtmHeader;
    private View mContentView;
    private int mPtmHeaderHeight;

    //阻尼系数
    private float mResistance = 2.0f;
    //阈值 PtmHeaderHeight 的倍数
    private float mThreSholdValue = 1.5f;
    //滑动产生距离
    private int mScaledTouchSlop;
    //起始点坐标
    private int mStartX, mStartY;
    //实时触摸点
    private int mTouchX, mTouchY;
    //
    private int mMoveX, mMoveY;
    //临时距离变量
    private int mDistanceTemp;
    //Y轴垂直滑动距离
    private int mDistanceY;
    private boolean isMoveState;
    private Scroller mScroller;
    //当前是否是书签
    private boolean isMark;
    //正在滚动
    private boolean isScroll;

    public PtmLayout(Context context) {
        this(context, null);
    }

    public PtmLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtmLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initPtmHeader();
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2;
        mScroller = new Scroller(getContext());
    }

    /**
     * 初始化 PtmHeader
     */
    private void initPtmHeader() {
        mPtmHeader = new PtmHeader(getContext());
        mPtmHeader.setBackgroundColor(Color.parseColor("#dddddd"));
        LayoutParams lp = (LayoutParams) generateDefaultLayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        addView(mPtmHeader, lp);
        if (isMark) mPtmHeader.prepareAdd();
        else mPtmHeader.prepareDelete();
    }

    @Override

    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Log.e(TAG, "onLayout: ");
        layoutChildren();
    }

    /**
     * 对子View进行布局
     */
    private void layoutChildren() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (mPtmHeader != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mPtmHeader.getLayoutParams();
            int left;
            int right;
            int top;
            int bottom;
            LayoutParams headerParams = (LayoutParams) mPtmHeader.getLayoutParams();
            if (mDistanceY <= mPtmHeaderHeight) {
                left = paddingLeft + lp.leftMargin;
                right = left + mPtmHeader.getMeasuredWidth();
                top = -mPtmHeaderHeight + paddingTop + lp.topMargin + mDistanceY;
                bottom = top + mPtmHeaderHeight;
                mPtmHeader.layout(left, top, right, bottom);
                headerParams.height = mPtmHeaderHeight;
            } else {
                left = 0;
                right = left + mPtmHeader.getMeasuredWidth();
                top = 0;
                bottom = mDistanceY;
                headerParams.height = bottom;
            }
            mPtmHeader.setLayoutParams(headerParams);
            mPtmHeader.layout(left, top, right, bottom);
        }
        if (mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int right = left + mContentView.getMeasuredWidth();
            int top = paddingTop + lp.topMargin + mDistanceY;
            int bottom = top + mContentView.getMeasuredHeight();
            mContentView.layout(left, top, right, bottom);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPtmHeader != null) {
            measureChildWithMargins(mPtmHeader, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mPtmHeader.getLayoutParams();
//            mHeaderHeight = mPtmHeader.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mPtmHeaderHeight = mPtmHeader.getPtmHeaderHeight();
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
            Log.i(TAG, "child is ok");
            MarginLayoutParams lp = (MarginLayoutParams) generateDefaultLayoutParams();
            mContentView = getChildAt(1);
            Log.e(TAG, "onFinishInflate: " + (mContentView.getLayoutParams() instanceof LayoutParams));
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("Child View is Empty!");
            mContentView = errorView;
            LayoutParams lp = (LayoutParams) generateDefaultLayoutParams();
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = LayoutParams.MATCH_PARENT;
            addView(mContentView, lp);
        }

        super.onFinishInflate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isScroll || !isEnabled() || mContentView == null) {
            return false;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        mTouchX = x;
        mTouchY = y;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent: ACTION_DOWN");
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent: ACTION_MOVE");
                // 如果是左滑右滑 幅度 大于 上下滑动
                if (Math.abs(mTouchX - mStartX) > Math.abs(mTouchY - mStartY)) {
                    break;
                }
                if (!isMoveState) isMoveState = Math.abs(mTouchY - mStartY) > mScaledTouchSlop;
                if (!isMoveState) break;
                if (mTouchY <= mStartY) {
                    reset();
                    break;
                }
                mMoveX = x;
                mMoveY = y;
                updatePos();
                return true;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent: ACTION_UP");
                moveTop();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 对PtmHeader和contentView进行重新布局
     */
    private void updatePos() {
        mDistanceTemp = mDistanceY = (int) ((mMoveY - mStartY) / mResistance);
        updatePtmHeader();
        requestLayout();
    }

    /**
     * 更新 PtmHeader
     */
    private void updatePtmHeader() {
        if (mDistanceY > mPtmHeaderHeight * mThreSholdValue) {
            mPtmHeader.arrowUp();
            if (isMark) mPtmHeader.releaseDelete();
            else mPtmHeader.releaseAdd();
        } else {
            mPtmHeader.arrowDown();
            if (isMark) mPtmHeader.prepareDelete();
            else mPtmHeader.prepareAdd();
        }
    }

    /**
     * 移动到顶部
     */
    private void moveTop() {
        if (mDistanceY == 0) return;
        isScroll = true;
        mScroller.startScroll(0, 0, 0, mDistanceY, 400);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            mDistanceY = mDistanceTemp - Math.abs(currY);
            //Log.e(TAG, "computeScroll  currY=" + currY + "   distance=" + mDistanceY);
            if (mScroller.getFinalY() == currY) {
                isScroll = false;
            }
            invalidate();
            requestLayout();
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /**
     * 设置是否是书签
     *
     * @param mark y/n
     */
    public void setMark(boolean mark) {
        isMark = mark;
        if (mark) mPtmHeader.prepareDelete();
        else mPtmHeader.prepareDelete();
    }

    /**
     * 是否是书签
     *
     * @return y/n
     */
    public boolean isMark() {
        return isMark;
    }

    /**
     * 重置当前布局
     */
    private void reset() {
        mDistanceY = 0;
        requestLayout();
    }
}

