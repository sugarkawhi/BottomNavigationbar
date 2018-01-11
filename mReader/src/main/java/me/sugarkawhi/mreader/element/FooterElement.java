package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

/**
 * 页尾部分：绘制每一页的页尾,包括进度，时间和电量。
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class FooterElement extends Element {

    private float mReaderWidth;
    private float mReaderHeight;
    private float mFooterHeight;
    private float mPadding;
    private String progress;
    private String time;
    private float electric;
    private Paint mPaint;
    private Paint.FontMetrics mFontMetrics;
    private int mBatteryWidth, mBatteryHeight, mBatteryHeadSize, mBatteryGap;

    public FooterElement(float footerHeight, float padding, float textSize,
                         int batteryWidth, int batteryHeight, int batteryHead, int batteryGap) {
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mBatteryWidth = batteryWidth;
        this.mBatteryHeight = batteryHeight;
        this.mBatteryHeadSize = batteryHead;
        this.mBatteryGap = batteryGap;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        mFontMetrics = mPaint.getFontMetrics();
    }

    public void setReaderSize(float readerWidth, float readerHeight) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
    }


    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setElectric(float electric) {
        this.electric = electric;
    }

    @Override
    public void onDraw(Canvas canvas) {

        //画进度
        if (!TextUtils.isEmpty(progress)) {
            mPaint.measureText(progress);
            float titleHeight = Math.abs(mFontMetrics.descent + mFontMetrics.ascent);
            float titleY = mReaderHeight - mFooterHeight / 2 + titleHeight / 2;
            canvas.drawText(progress, mPadding, titleY, mPaint);
        }
        //画电池 STEP1: 电池头
        canvas.drawRect(mReaderWidth - mPadding - mBatteryHeadSize,
                mReaderHeight - mFooterHeight / 2 - mBatteryHeadSize / 2,
                mReaderWidth - mPadding,
                mReaderHeight - mFooterHeight / 2 + mBatteryHeadSize / 2,
                mPaint);
        //画电池 STEP2: 电池外壳
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.drawRect(mReaderWidth - mPadding - mBatteryHeadSize - mBatteryWidth,
                mReaderHeight - mFooterHeight / 2 - mBatteryHeight / 2,
                mReaderWidth - mPadding - mBatteryHeadSize,
                mReaderHeight - mFooterHeight / 2 + mBatteryHeight / 2,
                mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mReaderWidth - mPadding - mBatteryHeadSize - mBatteryWidth + mBatteryGap,
                mReaderHeight - mFooterHeight / 2 - mBatteryHeight / 2 + mBatteryGap,
                mReaderWidth - mPadding - mBatteryHeadSize - mBatteryGap - (mBatteryWidth * (1 - electric)),
                mReaderHeight - mFooterHeight / 2 + mBatteryHeight / 2 - mBatteryGap,
                mPaint);
        //画时间
        if (!TextUtils.isEmpty(time)) {
            mPaint.measureText(time);
            float timeWidth = mPaint.measureText(time, 0, time.length());
            float timeMargin = 20; //与右边电池的偏移量
            float x = mReaderWidth - mPadding - timeWidth - mBatteryWidth - timeMargin;
            float timeHeight = Math.abs(mFontMetrics.descent + mFontMetrics.ascent);
            float timeY = mReaderHeight - mFooterHeight / 2 + timeHeight / 2;
            canvas.drawText(time, x, timeY, mPaint);
        }
    }
}
