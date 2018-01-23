package me.sugarkawhi.mreader.anim;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.view.MReaderView;

/**
 * page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public abstract class PageAnimController {
    public String TAG = getClass().getSimpleName();

    protected MReaderView mReaderView;
    protected PageElement mPageElement;

    //阅读器宽高
    public int mReaderWidth, mReaderHeight;
    //中间区域
    private Rect mCenterRect;
    //右侧区域
    private Rect mRightRect;
    //左侧区域
    private Rect mLeftRect;
    //是否处于滑动状态
    private boolean isMove;
    //是否处于滚动状态
    protected boolean isScroll;
    //是否取消翻页
    private boolean isCancel;
    //滑动方向
    protected int mDirection = IReaderDirection.NONE;
    //View滚动
    protected Scroller mScroller;
    //滑动的最小距离
    protected float mTouchSlop;

    public Bitmap mCurrentBitmap;
    public Bitmap mNextBitmap;

    public PageData mCurrentPageData;
    public PageData mNextPageData;

    //按下开始坐标
    protected int mStartX, mStartY;
    //滑动当前坐标
    protected int mTouchX, mTouchY;

    private IPageChangeListener mPageChangeListener;
    private IReaderTouchListener mIReaderTouchListener;


    public PageAnimController(MReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement,IPageChangeListener pageChangeListener) {
        this.mReaderView = readerView;
        this.mPageElement = pageElement;
        this.mReaderWidth = readerWidth;
        this.mReaderHeight = readerHeight;
        mPageChangeListener = pageChangeListener;

        mLeftRect = new Rect(0, 0, readerWidth / 3, readerHeight);
        mRightRect = new Rect(readerWidth / 3 * 2, 0, readerWidth, readerHeight);
        mCenterRect = new Rect(readerWidth / 3, 0, readerWidth / 3 * 2, readerHeight);
        mCurrentBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mScroller = new Scroller(mReaderView.getContext(), new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(readerView.getContext()).getScaledTouchSlop();
    }

    public void setIReaderTouchListener(IReaderTouchListener IReaderTouchListener) {
        mIReaderTouchListener = IReaderTouchListener;
    }


    abstract void drawStatic(Canvas canvas);

    abstract void drawMove(Canvas canvas);

    abstract void startScroll();

    /**
     * 刷新重新生成
     */
    public void invalidate() {
        generatePage(mCurrentPageData, mCurrentBitmap);
        generatePage(mNextPageData, mNextBitmap);
    }


    /**
     * 设置滑动方向 上页、下页
     */
    private void setDirection() {
        if (mTouchX < mStartX) {
            mDirection = IReaderDirection.NEXT;
        } else if (mTouchX > mStartX) {
            mDirection = IReaderDirection.PRE;
        } else {
            mDirection = IReaderDirection.NONE;
        }
    }

    /**
     * 直接设置滚动方向
     */
    private void setDirection(int direction) {
        mDirection = direction;
    }

    /**
     * drawPage 绘制页面
     */
    public void dispatchDrawPage(Canvas canvas) {
        if (isScroll) {
            drawMove(canvas);
        } else {
            drawStatic(canvas);
        }
    }

    /**
     * 分发事件
     */
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        mTouchX = x;
        mTouchY = y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                isMove = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                //如果不在滑动状态 判断是否需要进入滑动状态
                if (!isMove) isMove = Math.abs(mTouchY - mStartX) > mTouchSlop;
                if (isMove) {
                    setDirection();
                    isScroll = true;
                    mReaderView.postInvalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isMove) {//正在滑动，滑动到指定位置
                    startScroll();
                    mReaderView.invalidate();
                } else {//只是点击屏幕 1.中间2.左侧3.右侧
                    if (mCenterRect.contains(mTouchX, mTouchY)) {
                        //点击中间区域
                        if (mIReaderTouchListener != null) mIReaderTouchListener.onTouchCenter();
                    } else if (mLeftRect.contains(mTouchX, mTouchY)) {
                        //上一页
                        Log.e(TAG, "dispatchTouchEvent: 上一页");
                        setDirection(IReaderDirection.PRE);
                        startScroll();
                        mReaderView.postInvalidate();
                    } else if (mRightRect.contains(mTouchX, mTouchY)) {
                        //下一页
                        Log.e(TAG, "dispatchTouchEvent: 下一页");
                        setDirection(IReaderDirection.NEXT);
                        startScroll();
                        mReaderView.postInvalidate();
                    }
                }
                return true;

        }
        return true;
    }

    public void computeScroll() {

        boolean mFinished = mScroller.computeScrollOffset();
        Log.e(TAG, "computeScroll  mFinished=" + mFinished);
        if (mFinished) {
            mTouchX = mScroller.getCurrX();
            mTouchY = mScroller.getCurrY();
            if (mScroller.getFinalX() == mTouchX && mScroller.getFinalY() == mTouchY) {
                isScroll = false;
                if (mDirection == IReaderDirection.NEXT)
                    mPageChangeListener.onNext();
                else if (mDirection == IReaderDirection.PRE)
                    mPageChangeListener.onPre();
            }
            mReaderView.invalidate();
        }
    }


    /**
     * 设置当前页内容
     *
     * @param currentPageData 当前页
     */
    public void setCurrentPageData(PageData currentPageData) {
        mCurrentPageData = currentPageData;
        generatePage(currentPageData, mCurrentBitmap);
    }

    /**
     * 设置下一页内容
     *
     * @param nextPageData 下一页
     */
    public void setNextPageData(PageData nextPageData) {
        mNextPageData = nextPageData;
        generatePage(nextPageData, mNextBitmap);
    }

    /**
     * 根据{#PageData}生成页面
     *
     * @param pageData 需要生成的页面数据
     * @param bitmap   要绘制的bitmap
     */
    public void generatePage(PageData pageData, Bitmap bitmap) {
        if (pageData == null) return;
        mPageElement.generatePage(pageData, bitmap);
    }

    public interface IPageChangeListener {
        void onCancel();

        boolean onPre();

        boolean onNext();
    }
}
