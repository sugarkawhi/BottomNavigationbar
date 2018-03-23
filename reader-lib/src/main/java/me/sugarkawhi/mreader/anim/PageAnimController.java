package me.sugarkawhi.mreader.anim;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.view.ReaderView;

import static me.sugarkawhi.mreader.config.IReaderConfig.DURATION_PAGE_SWITCH;

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
    private boolean isMoveState;
    //翻下页 判断是否有下页、翻上页 判断是否有上页
    private boolean hasNextOrPre = true;
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

    /**
     * 一屏中至多显示两页
     * mCurrentBitmap 为 左边页
     * mNextBitmap 为右边页
     */
    public Bitmap mCurrentBitmap;
    public Bitmap mNextBitmap;


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

        mLeftRect = new Rect(0, 0, readerWidth / 4, readerHeight);
        mRightRect = new Rect(readerWidth / 4 * 3, 0, readerWidth, readerHeight);
        mCenterRect = new Rect(readerWidth / 4, 0, readerWidth / 4 * 3, readerHeight);
        mCurrentBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mScroller = new Scroller(mReaderView.getContext(), new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(readerView.getContext()).getScaledTouchSlop();
    }

    public void setIReaderTouchListener(IReaderTouchListener listener) {
        mIReaderTouchListener = listener;
    }


    abstract void drawStatic(Canvas canvas);

    abstract void drawMove(Canvas canvas);

    protected void startScroll() {
        isScroll = true;
        int dx = 0;
        switch (mDirection) {
            case IReaderDirection.NEXT:
                if (isCancel) {
                    if (mStartX <= mTouchX) return;
                    dx = mStartX - mTouchX;
                } else {
                    dx = -(mReaderWidth - (mStartX - mTouchX));
                }
                break;
            case IReaderDirection.PRE:
                if (isCancel) {
                    if (mStartX >= mTouchX) return;
                    dx = mStartX - mTouchX;
                } else {
                    dx = mReaderWidth - (mTouchX - mStartX);
                }
                break;
        }
        int duration = 0;
        if (mReaderView.getPageMode() != IReaderConfig.PageMode.NONE) {
            duration = DURATION_PAGE_SWITCH;
        }
        duration = duration * Math.abs(dx) / mReaderWidth;
        mScroller.startScroll(mTouchX, 0, dx, 0, duration);
        mReaderView.postInvalidate();
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
        //非打开书籍状态
        if (!mReaderView.isOpening()) {
            return true;
        }
        if (mIReaderTouchListener != null && !mIReaderTouchListener.canTouch()) {
            if (event.getAction() == MotionEvent.ACTION_UP) mIReaderTouchListener.onTouchCenter();
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        mTouchX = x;
        mTouchY = y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.e(TAG,"dispatchTouchEvent ACTION_DOWN");
                //NOTICE:务必在最前面调用
                abortAnim();
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                //判断是否有上下页  默认有上/下页
                hasNextOrPre = true;
                //正在滚动 - false
                isScroll = false;
                //是否取消 - false
                isCancel = false;
                //是否是滑动状态
                isMoveState = false;
                //停止动画

                return true;
            case MotionEvent.ACTION_MOVE:
                L.e(TAG,"dispatchTouchEvent ACTION_MOVE");
                //如果不在滑动状态 判断是否需要进入滑动状态
                if (!isMoveState) isMoveState = Math.abs(mTouchX - mStartX) > mTouchSlop;
                //如果不构成滑动 不处理
                if (!isMoveState) return true;
                //构成滑动第一次 处理事件：1.判断方向2.根据方向判断存在上/下页进行回调
                if (mMoveX == 0 && mMoveY == 0) {
                    L.e(TAG, "MotionEvent1.构成滑动 判断方向 x=" + x + " mStartX=" + mStartX);
                    //上一页
                    if (x - mStartX > 0) {
                        L.e(TAG, "MotionEvent2.方向为上一页");
                        //处理事件1 判断方向
                        mDirection = IReaderDirection.PRE;
                        //处理事件2 判断存在上页
                        boolean hasPre = hasPre();
                        if (!hasPre) {//不存在上一页
                            L.e(TAG, "不存在上一页");
                            hasNextOrPre = false;
                            return true;
                        }
                    } else {
                        L.e(TAG, "MotionEvent2.方向为下一页");
                        //处理事件1 判断方向
                        mDirection = IReaderDirection.NEXT;
                        //处理事件2 判断存在下页
                        boolean hasPre = hasNext();
                        if (!hasPre) {//不存在下一页
                            L.e(TAG, "不存在下一页");
                            hasNextOrPre = false;
                            return true;
                        }
                    }
                }
                //开始滑动 这时候已经确定了方向
                // 处理事件：根据翻页方向判断是否取消翻页了
                else {
                    L.e(TAG, "MotionEvent3.开始滑动");
                    switch (mDirection) {
                        case IReaderDirection.NEXT:
                            isCancel = (x - mMoveX >= 0);
                            L.e(TAG, "下一页：isCancel=" + isCancel);
                            break;
                        case IReaderDirection.PRE:
                            L.e(TAG, "上一页：isCancel=" + isCancel);
                            isCancel = (x - mMoveX <= 0);
                    }
                }
                mMoveX = x;
                mMoveY = y;
                isScroll = true;
                mReaderView.invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                L.e(TAG,"dispatchTouchEvent ACTION_UP");
                //i:非滑动状态 点击屏幕 1.中间2.左侧3.右侧
                if (!isMoveState) {
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
                        }
                    } else if (mRightRect.contains(mTouchX, mTouchY)) {
                        //下一页
                        mDirection = IReaderDirection.NEXT;
                        boolean hasNext = hasNext();
                        if (!hasNext) {//不存在下一页
                            return true;
                        }
                    }
                }
                //ii：滑动状态下
                // 1.处理是否取消的情况2.有无上下页的情况
                if (isCancel) {
                    L.e(TAG, "ACTION_UP isCancel = " + isCancel);
                    mPageChangeListener.onCancel(mDirection);
                }
                L.e(TAG, "ACTION_UP hasNextOrPre = " + hasNextOrPre);
                if (hasNextOrPre) {
                    startScroll();
                    mReaderView.invalidate();
                }
                return true;

        }
        return true;
    }

    public void computeScroll() {
        boolean notFinished = mScroller.computeScrollOffset();
        //L.e(TAG, "computeScroll  computeScrollOffset -> " + notFinished);
        if (notFinished) {
            mTouchX = mScroller.getCurrX();
            mTouchY = mScroller.getCurrY();
            if (mScroller.getFinalX() == mTouchX && mScroller.getFinalY() == mTouchY) {
                isScroll = false;
                mPageChangeListener.onSelectPage(mDirection, isCancel);
            }
            mReaderView.invalidate();
        }
    }

    /**
     * 是否有上一页
     */
    private boolean hasPre() {
        return mPageChangeListener.hasPre();
    }


    /**
     * 是否有下一页
     */
    private boolean hasNext() {
        return mPageChangeListener.hasNext();
    }

    /**
     * 结束动画
     */
    private void abortAnim() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            isScroll = false;
            mTouchX = mScroller.getFinalX();
            mTouchY = mScroller.getFinalY();
            mPageChangeListener.onSelectPage(mDirection, isCancel);
            L.e(TAG, "abortAnim");
        }
    }


    /**
     * 获取下一页Bitmap
     */
    public Bitmap getNextBitmap() {
        return mNextBitmap;
    }

    /**
     * 获取当前页的Bitmap
     */
    public Bitmap getCurrentBitmap() {
        return mCurrentBitmap;
    }


    public interface IPageChangeListener {

        /**
         * 取消
         *
         * @param direction 方向
         */
        void onCancel(int direction);

        /**
         * 选中
         *
         * @param direction 方向
         * @param isCancel  是否取消
         */
        void onSelectPage(int direction, boolean isCancel);

        /**
         * 下一页
         *
         * @return t/f
         */
        boolean hasPre();

        /**
         * 上一页
         *
         * @return t/f
         */
        boolean hasNext();

    }
}
