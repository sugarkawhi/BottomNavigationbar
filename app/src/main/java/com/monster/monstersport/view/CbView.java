package com.monster.monstersport.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * canvas bitmap view
 * Created by ZhaoZongyao on 2018/1/17.
 */

public class CbView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }
}
