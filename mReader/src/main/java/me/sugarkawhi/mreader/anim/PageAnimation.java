package me.sugarkawhi.mreader.anim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import me.sugarkawhi.mreader.view.BaseReaderView;

/**
 * page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public abstract class PageAnimation {

    Bitmap mCurrentBitmap;
    Bitmap mNextBitmap;

    public PageAnimation(Bitmap currentBitmap, Bitmap nextBitmap) {

    }

    public enum Direction {

    }

    public boolean isAnimRunning;

    abstract void dispatchDrawPage(Canvas canvas);

    abstract void dispatchTouchEvent(MotionEvent event, BaseReaderView baseReaderView);
}
