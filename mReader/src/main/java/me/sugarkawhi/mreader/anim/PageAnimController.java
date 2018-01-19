package me.sugarkawhi.mreader.anim;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.view.MReaderView;

/**
 * page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public abstract class PageAnimController {
    private String TAG = "PageAnimController";

    protected MReaderView mReaderView;
    protected PageElement mPageElement;
    public GestureDetector mGestureDetector;

    public int mReaderWidth, mReaderHeight;
    private boolean isRunning;
    //滑动方向
    protected int mDirection = IReaderDirection.NONE;

    public Bitmap mCurrentBitmap;
    public Bitmap mNextBitmap;

    private PageData mCurrentPageData;
    private PageData mNextPageData;

    //x轴滑动差值
    protected int mXoffset;


    public PageAnimController(MReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement) {
        this.mReaderView = readerView;
        this.mPageElement = pageElement;
        this.mReaderWidth = readerWidth;
        this.mReaderHeight = readerHeight;
        mCurrentBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(readerWidth, readerHeight, Bitmap.Config.RGB_565);
        mGestureDetector = new GestureDetector(mReaderView.getContext(), new MyOnGestureListener());
    }

    abstract void drawStatic(Canvas canvas);

    abstract void drawMove(Canvas canvas);

    public void dispatchDrawPage(Canvas canvas) {
        if (isRunning) {
            drawMove(canvas);
        } else {
            drawStatic(canvas);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    public void setCurrentPageData(PageData currentPageData) {
        mCurrentPageData = currentPageData;
        Canvas canvas = new Canvas(mCurrentBitmap);
        mPageElement.setPageData(currentPageData);
        mPageElement.onDraw(canvas);
    }

    public void setNextPageData(PageData nextPageData) {
        mNextPageData = nextPageData;
        mPageElement.setPageData(mNextPageData);
        Canvas canvas = new Canvas(mNextBitmap);
        mPageElement.onDraw(canvas);
    }

    class MyOnGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG, " onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e(TAG, " onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG, " onSingleTapUp");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, " onScroll");
            isRunning = true;
            float x1 = e1.getX();
            float x2 = e2.getX();
            Log.e(TAG, "x1=" + x1 + ",x2=" + x2 + ",distanceX=" + distanceX);
            mXoffset = (int) (x1 - x2);
            if (x2 > x1) { //上一页
                mDirection = IReaderDirection.PRE;
            } else if (x2 < x1) {//下一页
                mDirection = IReaderDirection.NEXT;
            } else { //没动

            }
            mReaderView.invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e(TAG, " onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x1 = e1.getX();
            float x2 = e2.getX();
            Log.e(TAG, " onFling");
            return true;
        }
    }

}
