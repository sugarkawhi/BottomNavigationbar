package me.sugarkawhi.mreader.anim;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.view.MReaderView;

/**
 * Slide page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class SlideAnimController extends PageAnimController {

    //当前页 图片裁剪区域
    private Rect mCurSrcRect;
    //当前页 图片绘制的位置
    private Rect mCurDstRect;
    //下一页
    private Rect mNextSrcRect;
    private Rect mNextDstRect;

    public SlideAnimController(MReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement) {
        super(readerView, readerWidth, readerHeight, pageElement);
        mCurSrcRect = new Rect(0, 0, readerWidth, readerHeight);
        mCurDstRect = new Rect(0, 0, readerWidth, readerHeight);
        mNextSrcRect = new Rect(0, 0, readerWidth, readerHeight);
        mNextDstRect = new Rect(0, 0, readerWidth, readerHeight);
    }


    @Override
    void drawStatic(Canvas canvas) {
        canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
    }

    @Override
    void drawMove(Canvas canvas) {
        Log.e(TAG, "drawMove: ");
        int xOffset = Math.abs(mStartX - mTouchX);
        switch (mDirection) {
            case IReaderDirection.NEXT:
                mCurSrcRect.left = xOffset;
                mCurDstRect.right = mReaderWidth - xOffset;
                canvas.drawBitmap(mCurrentBitmap, mCurSrcRect, mCurDstRect, null);
                mNextSrcRect.right = xOffset;
                mNextDstRect.left = mReaderWidth - xOffset;
                canvas.drawBitmap(mNextBitmap, mNextSrcRect, mNextDstRect, null);
                break;
            case IReaderDirection.PRE:
            case IReaderDirection.NONE:
                mCurSrcRect.left = mReaderWidth - xOffset;
                mCurDstRect.right = xOffset;
                canvas.drawBitmap(mCurrentBitmap, mCurSrcRect, mCurDstRect, null);
                mNextSrcRect.right = mReaderWidth - xOffset;
                mNextDstRect.left = xOffset;
                canvas.drawBitmap(mNextBitmap, mNextSrcRect, mNextDstRect, null);
                break;
        }
    }

    @Override
    void startScroll() {
        int dx;
        isScroll = true;
        switch (mDirection) {
            case IReaderDirection.NEXT:
                dx = -(mTouchX + (mReaderWidth - mStartX));
                break;
            case IReaderDirection.PRE:
                dx = (mReaderWidth - (mTouchX - mStartX));
                break;
            default:
                dx = 0;
        }
        Log.e(TAG, "startScroll: dx=" + dx);
        int duration = ((444 * Math.abs(dx)) / mReaderWidth);
        mScroller.startScroll(mTouchX, 0, dx, 0, duration);
    }

    @Override
    public void computeScroll() {
        boolean mFinished = mScroller.computeScrollOffset();
        Log.e(TAG, "computeScroll  mFinished=" + mFinished);
        if (mFinished) {
            mTouchX = mScroller.getCurrX();
            mTouchY = mScroller.getCurrY();
            if (mScroller.getFinalX() == mTouchX && mScroller.getFinalY() == mTouchY) {
                isScroll = false;
                mCurrentBitmap = mNextBitmap;
            }
            mReaderView.invalidate();
        }
    }
}
