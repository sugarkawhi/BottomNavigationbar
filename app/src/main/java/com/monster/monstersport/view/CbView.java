package com.monster.monstersport.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * canvas bitmap view
 * Created by ZhaoZongyao on 2018/1/17.
 */

public class CbView extends View {
    String TAG = "CbView";

    private Bitmap mBitmap;
    private Paint mPaint;
    private Path mTouchPath;

    private List<Point> mPoints = new ArrayList<>();

    public CbView(Context context) {
        this(context, null);
    }

    public CbView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmap = Bitmap.createBitmap(500, 800, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawColor(Color.BLUE);
        //主要是为了得到这个BITMAP WHAT THE FUCK
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(30);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(mBitmap, 0, 0, null);
//        mTouchPath.reset();
        for (Point point : mPoints) {
            canvas.drawPoint(point.x, point.y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: event.getAction()=" + event.getAction() + " > " + (event.getAction() == MotionEvent.ACTION_MOVE));
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoints.clear();
                point = new Point(x, y);
                mPoints.add(point);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                Log.e(TAG,"slop " + slop );
                point = new Point(x, y);
                mPoints.add(point);

                invalidate();
                break;
        }
        return true;
    }

    public void reset() {
        mPoints.clear();
        invalidate();
    }
}
