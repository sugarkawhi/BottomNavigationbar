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

    private int mWidth, mHeight;
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
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(30);
        mPaint.setTextSize(50);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(mBitmap, 0, 0, null);
//        mTouchPath.reset();
        for (Point point : mPoints) {
            canvas.drawPoint(point.x, point.y, mPaint);
        }
        String testString = "我是在中间位置吗";
        canvas.drawColor(Color.parseColor("#fdafda"));
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontWidth = mPaint.measureText(testString);
        int baseline = (int) ((mHeight - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(testString, mWidth / 2 - fontWidth / 2, baseline, mPaint);
//        mPaint.setColor(Color.YELLOW);
//        float fontHeight = Math.abs(fontMetrics.ascent - fontMetrics.descent);
//        canvas.drawText(testString, mWidth / 2 - fontWidth / 2,
//                mHeight / 2 + fontHeight / 2, mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.e(TAG, "computeScroll: ");
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
                Log.e(TAG, "slop " + slop);
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
