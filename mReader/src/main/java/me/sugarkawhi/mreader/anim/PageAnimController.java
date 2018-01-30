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
import me.sugarkawhi.mreader.view.ReaderView;

/**
 * page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public abstract class PageAnimController {
    public String TAG = getClass().getSimpleName();

    protected ReaderView mReaderView;
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
    protected boolean isCancel;
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
    //判断当前是否取消坐标 - 类似于mTouchX,mTouchY。
    protected int mMoveX, mMoveY;


    private IPageChangeListener mPageChangeListener;
    private IReaderTouchListener mIReaderTouchListener;


    public PageAnimController(ReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement, IPageChangeListener pageChangeListener) {
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
        mTouchSlop = ViewConfiguration.get(readerView.getContext()).getScaledPagingTouchSlop();
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
                abortAnim();
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                isScroll = false;
                isCancel = false;
                isMove = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                //如果不在滑动状态 判断是否需要进入滑动状态
                if (!isMove) isMove = Math.abs(mTouchY - mStartX) > mTouchSlop;
                //如果不构成滑动 不处理
                if (!isMove) return true;
                //构成滑动第一次 处理事件：1.判断方向2.根据方向判断存在上/下页进行回调
                if (mMoveX == 0 && mMoveY == 0) {
                    //上一页
                    if (x - mStartX > 0) {
                        //处理事件1 判断方向
                        mDirection = IReaderDirection.PRE;
                        //处理事件2 判断存在上页
                        boolean hasPre = hasPre();
                        if (!hasPre) {//不存在上一页
                            Log.e(TAG, "不存在上一页");
                            return true;
                        } else {//存在上一页
                            PageData prePage = mPageChangeListener.getPrePageData();
                            if (prePage != mCurrentPageData) {
                                mNextPageData = mCurrentPageData;
                                mCurrentPageData = prePage;
                                invalidate();
                            }
                        }
                    } else {
                        //处理事件1 判断方向
                        mDirection = IReaderDirection.NEXT;
                        //处理事件2 判断存在下页
                        boolean hasPre = hasNext();
                        if (!hasPre) {//不存在下一页
                            Log.e(TAG, "不存在下一页");
                            return true;
                        } else {//存在下一页
                            PageData nextPage = mPageChangeListener.getNextPageData();
                            if (nextPage != mNextPageData) {
                                mNextPageData = nextPage;
                                invalidate();
                            }
                        }
                    }

                }
                //开始滑动 这时候已经确定了方向
                // 处理事件：根据翻页方向判断是否取消翻页了
                else {
                    switch (mDirection) {
                        case IReaderDirection.NEXT:
                            isCancel = (x - mMoveX >= 0);
                            Log.e(TAG, "下一页：isCancel=" + isCancel);
                            break;
                        case IReaderDirection.PRE:
                            Log.e(TAG, "上一页：isCancel=" + isCancel);
                            isCancel = (x - mMoveX <= 0);
                    }
                }
                mMoveX = x;
                mMoveY = y;
                isScroll = true;
                mReaderView.postInvalidate();
                return true;
            case MotionEvent.ACTION_UP:
                //i:非滑动状态 点击屏幕 1.中间2.左侧3.右侧
                if (!isMove) {
                    if (mCenterRect.contains(mTouchX, mTouchY)) {
                        //点击中间区域
                        if (mIReaderTouchListener != null) mIReaderTouchListener.onTouchCenter();
                        return true;
                    } else if (mLeftRect.contains(mTouchX, mTouchY)) {
                        //上一页
                        mDirection = IReaderDirection.PRE;
                        boolean hasPre = hasPre();
                        if (!hasPre) {
                            return true;
                        } else {
                            PageData prePage = mPageChangeListener.getPrePageData();
                            if (prePage != mCurrentPageData) {
                                mNextPageData = mCurrentPageData;
                                mCurrentPageData = prePage;
                                invalidate();
                            }
                            startScroll();
                        }
                        mReaderView.postInvalidate();
                    } else if (mRightRect.contains(mTouchX, mTouchY)) {
                        //下一页
                        mDirection = IReaderDirection.NEXT;
                        boolean hasNext = hasNext();
                        if (!hasNext) {//不存在下一页
                            return true;
                        } else {//存在下一页
                            PageData nextPage = mPageChangeListener.getNextPageData();
                            if (nextPage != mNextPageData) {
                                mNextPageData = nextPage;
                                invalidate();
                            }
                            startScroll();
                        }
                        mReaderView.postInvalidate();
                    }
                }
                //ii：滑动状态下
                // 1.处理是否取消的情况2.有无上下页的情况
                else {
                    if (isCancel) {
                        //TODO 这里需要回调吗？
                        mPageChangeListener.onCancel();
                    }
                    switch (mDirection) {
                        case IReaderDirection.NEXT:
                            boolean hasNext = hasNext();
                            if (!hasNext) return true;
                            break;
                        case IReaderDirection.PRE:
                            boolean hasPre = hasPre();
                            if (!hasPre) return true;
                            break;
                    }
                    startScroll();
                    mReaderView.postInvalidate();
                }
                return true;

        }
        return true;
    }

    public void computeScroll() {
        boolean notFinished = mScroller.computeScrollOffset();
        Log.e(TAG, "computeScroll  computeScrollOffset -> " + notFinished);
        if (notFinished) {
            mTouchX = mScroller.getCurrX();
            mTouchY = mScroller.getCurrY();
            if (mScroller.getFinalX() == mTouchX && mScroller.getFinalY() == mTouchY) {
                isScroll = false;
                if (!isCancel) {
                    switch (mDirection) {
                        case IReaderDirection.NEXT:
                            mPageChangeListener.onSelectNext();
                            break;
                        case IReaderDirection.PRE:
                            mPageChangeListener.onSelectPre();
                            break;
                    }
                }
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
     * 根据{#PageData}生成页面
     *
     * @param pageData 需要生成的页面数据
     * @param bitmap   要绘制的bitmap
     */
    private void generatePage(PageData pageData, Bitmap bitmap) {
        if (pageData == null) return;
        Log.e(TAG, "generatePage: " + pageData.getProgress());
        mPageElement.generatePage(pageData, bitmap);
    }

    /**
     * 是否有上一页
     */
    protected boolean hasPre() {
        return mPageChangeListener.hasPre();
    }

    /**
     * 是否有下一页
     */
    protected boolean hasNext() {
        return mPageChangeListener.hasNext();
    }

    private void abortAnim() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            isScroll = false;
            mReaderView.postInvalidate();
        }
    }

    public interface IPageChangeListener {

        void onCancel();

        boolean hasPre();

        boolean hasNext();

        PageData getPrePageData();

        void onSelectPre();

        PageData getNextPageData();

        void onSelectNext();
    }
}
