package me.sugarkawhi.pulltomark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Pull To Mark Layout
 * 下拉添加书签
 * Created by ZhaoZongyao on 2018/3/20.
 */

public class PtmLayout extends ViewGroup {

    private static final String TAG = "PtmLayout";
    //取消状态
    public static final int STATUS_CANCEL = 1;
    //松手添加书签
    public static final int STATUS_RELEASE_ADD = 2;
    //松手删除书签
    public static final int STATUS_RELEASE_DELETE = 3;


    private PtmHeader mPtmHeader;
    private ImageView mPtmBookmark;
    private View mContentView;
    private int mPtmHeaderHeight;

    //阻尼系数
    private float mResistance;
    //阈值 PtmHeaderHeight 的倍数 超过这个高度才进行变化删除/添加
    private float mThreSholdValue;
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

    private Scroller mScroller;
    //当前是否是书签
    private boolean isMark;
    //正在滚动
    private boolean isScroll;

    private OnPtmHandleListener mListener;
    private int mCurPtmStatus;

    public PtmLayout(Context context) {
        this(context, null);
    }

    public PtmLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtmLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PtmLayout);
        mResistance = array.getFloat(R.styleable.PtmLayout_ptm_resistance, 4.0f);
        mThreSholdValue = array.getFloat(R.styleable.PtmLayout_ptm_threSholdValue, 1.2f);
        array.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initPtmHeader();
        initBookmark();
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
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
    }

    /**
     *
     */
    private void initBookmark() {
        mPtmBookmark = new ImageView(getContext());
        mPtmBookmark.setImageResource(R.drawable.book_mark);
        LayoutParams lp = (LayoutParams) generateDefaultLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        addView(mPtmBookmark, lp);
        mPtmBookmark.setVisibility(GONE);
    }

    /**
     * 重置 PtmHeader
     */
    private void resetPtmHeader() {
        if (isMark) mPtmHeader.prepareAdd();
        else mPtmHeader.prepareDelete();
    }

    @Override

    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        PtmLogger.e(TAG, "onLayout: ");
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

        int bmLeft = getMeasuredWidth() - mPtmBookmark.getMeasuredWidth();
        int bmTop = 0;
        int bmRight = getMeasuredWidth();
        int bmBottom = mPtmBookmark.getMeasuredHeight();
        mPtmBookmark.layout(bmLeft, bmTop, bmRight, bmBottom);
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
        measureChild(mPtmBookmark, widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        if (childCount > 3) {
            throw new IllegalStateException("PtmLayout only contain three child ");
        } else if (childCount == 3) {
            Log.i(TAG, "child is ok");
            mContentView = getChildAt(2);
        } else if (childCount == 2) {
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
        mPtmBookmark.bringToFront();
        super.onFinishInflate();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        PtmLogger.e(TAG, "onInterceptTouchEvent");
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        mTouchX = x;
        mTouchY = y;
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(x - mStartX);
                float deltaY = Math.abs(y - mStartY);
                if (deltaX >= mScaledTouchSlop && deltaY >= mScaledTouchSlop) {
                    if (deltaX > deltaY) {//左右滑动  不拦截
                        return false;
                    } else {//上下滑动  拦截
                        if (mListener != null)
                            mListener.onPtmStart();
                        return true;
                    }
                } else if (deltaX >= mScaledTouchSlop && deltaY < mScaledTouchSlop) {
                    return false;//左右滑动  不拦截
                } else if (deltaX < mScaledTouchSlop && deltaY >= mScaledTouchSlop) {
                    if (mListener != null)
                        mListener.onPtmStart();
                    return true;//上下滑动  拦截
                }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        PtmLogger.e(TAG, "onTouchEvent");
        if (mListener != null && !mListener.canTouch()) {
            PtmLogger.e(TAG, "canTouch is false");
            return true;
        }
        if (isScroll || !isEnabled() || mContentView == null) {
            return false;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        mTouchX = x;
        mTouchY = y;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PtmLogger.e(TAG, "onTouchEvent: ACTION_DOWN");
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                PtmLogger.e(TAG, "onTouchEvent: ACTION_MOVE");
                if (mTouchY <= mStartY) {
                    reset();
                    return true;
                }
                mMoveX = x;
                mMoveY = y;
                updatePos();
                return true;
            case MotionEvent.ACTION_UP:
                PtmLogger.e(TAG, "onTouchEvent: ACTION_UP");
                moveTop();
                return true;
        }
        return super.onTouchEvent(ev);
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
            if (isMark) {
                mCurPtmStatus = STATUS_RELEASE_DELETE;
                mPtmHeader.releaseDelete();
                hideBookMark();
            } else {
                mCurPtmStatus = STATUS_RELEASE_ADD;
                mPtmHeader.releaseAdd();
            }
        } else {
            mCurPtmStatus = STATUS_CANCEL;
            mPtmHeader.arrowDown();
            if (isMark) {
                showBookMark();
                mPtmHeader.prepareDelete();
            } else mPtmHeader.prepareAdd();
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
        callBackRelease();
    }

    @Override
    public void computeScroll() {
        boolean isNotFinish = mScroller.computeScrollOffset();
        PtmLogger.e(TAG, "isNotFinish=" + isNotFinish);
        if (isNotFinish) {
            int currY = mScroller.getCurrY();
            mDistanceY = mDistanceTemp - Math.abs(currY);
            if (isScroll && mScroller.getFinalY() == currY) {
                isScroll = false;
                callBackSuccess();
            }
            invalidate();
            requestLayout();
        }
    }


    /**
     * 松手回调
     */
    private void callBackRelease() {
        //回调
        switch (mCurPtmStatus) {
            case STATUS_RELEASE_ADD:
                showBookMark();
                break;
            case STATUS_RELEASE_DELETE:
                hideBookMark();
                break;
            case STATUS_CANCEL:
                if (isMark) showBookMark();
                else hideBookMark();
                break;
        }
    }

    /**
     * 动画结束回调
     */
    private void callBackSuccess() {
        PtmLogger.e(TAG, "callBackSuccess ");
        if (mListener != null) {
            //回调
            switch (mCurPtmStatus) {
                case STATUS_RELEASE_ADD:
                    mListener.onPtmAddSuccess();
                    break;
                case STATUS_RELEASE_DELETE:
                    mListener.onPtmDeleteSuccess();
                    break;
                case STATUS_CANCEL:
                    mListener.onPtmCancel();
            }
        }
        hideBookMark();
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

    /**
     * 隐藏书签
     */
    private void hideBookMark() {
        mPtmBookmark.setVisibility(GONE);
    }

    /**
     * 显示书签
     */
    private void showBookMark() {
        mPtmBookmark.setVisibility(VISIBLE);
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
        if (mark) {
            mPtmHeader.prepareDelete();
            showBookMark();
        } else {
            mPtmHeader.prepareDelete();
            hideBookMark();
        }
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
        mCurPtmStatus = -1;
        resetPtmHeader();
        requestLayout();
    }

    public void setListener(OnPtmHandleListener listener) {
        mListener = listener;
    }

    public interface OnPtmHandleListener {

        /**
         * 可分发处理事件
         */
        boolean canTouch();

        /**
         * 开始
         */
        void onPtmStart();

        /**
         * 添加书签完成
         */
        void onPtmAddSuccess();


        /**
         * 删除书签成功
         */
        void onPtmDeleteSuccess();

        /**
         * 取消操作
         */
        void onPtmCancel();
    }

}

