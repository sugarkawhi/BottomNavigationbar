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

    public SlideAnimController(MReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement, IPageChangeListener pageChangeListener) {
        super(readerView, readerWidth, readerHeight, pageElement, pageChangeListener);
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
        Log.e(TAG, "drawMove: isCancel=" + isCancel);
        int xOffset = Math.abs(mStartX - mTouchX);
        switch (mDirection) {
            case IReaderDirection.NEXT:
                // 取消状态下，并且发生越界了
                if (mTouchX >= mStartX) {
                    canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
                    return;
                }
                mCurSrcRect.left = xOffset;
                mCurDstRect.right = mReaderWidth - xOffset;
                canvas.drawBitmap(mCurrentBitmap, mCurSrcRect, mCurDstRect, null);
                mNextSrcRect.right = xOffset;
                mNextDstRect.left = mReaderWidth - xOffset;
                canvas.drawBitmap(mNextBitmap, mNextSrcRect, mNextDstRect, null);
                break;
            case IReaderDirection.PRE:
                // 取消状态下，并且发生越界了
                if (mStartX >= mTouchX) {
                    canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
                    return;
                }
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
        isScroll = true;
        int dx = 0;
        switch (mDirection) {
            case IReaderDirection.NEXT:
                if (!hasNext()) return;
                if (isCancel) {
                    if (mStartX <= mTouchX) return;
                    dx = mStartX - mTouchX;
                } else {
                    dx = -(mReaderWidth - (mStartX - mTouchX));
                }
                break;
            case IReaderDirection.PRE:
                if (!hasPre()) return;
                if (isCancel) {
                    if (mStartX >= mTouchX) return;
                    dx = mStartX - mTouchX;
                } else {
                    dx = mReaderWidth - (mTouchX - mStartX);
                }
                break;
        }
        int duration = 300 * Math.abs(dx) / mReaderWidth;
        mScroller.startScroll(mTouchX, 0, dx, 0, duration);
        mReaderView.postInvalidate();
    }

}
