package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import me.sugarkawhi.mreader.config.IReaderConfig;

import static me.sugarkawhi.mreader.config.IReaderConfig.Battery.RADIUS;

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
    private float batteryLevel;
    private Paint mPaint;
    private float mBatteryWidth, mBatteryInWidth;
    private float mBatteryGap;
    private RectF mBatteryRectF, mBatteryHeadRectF;

    public FooterElement(float readerWidth, float readerHeight, float footerHeight, float padding, Paint paint) {
        this.mReaderWidth = readerWidth;
        this.mReaderHeight = readerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mBatteryWidth = IReaderConfig.Battery.WIDTH;
        float batteryHeight = IReaderConfig.Battery.HEIGHT;
        float batteryHeadSize = IReaderConfig.Battery.HEAD;
        this.mBatteryGap = IReaderConfig.Battery.GAP;
        mBatteryInWidth = mBatteryWidth - 2 * mBatteryGap;
        this.mPaint = paint;
        mBatteryHeadRectF = new RectF(mReaderWidth - mPadding - batteryHeadSize,
                (mReaderHeight - mFooterHeight / 2 - batteryHeadSize / 2),
                (mReaderWidth - mPadding),
                (mReaderHeight - mFooterHeight / 2 + batteryHeadSize / 2));
        mBatteryRectF = new RectF(mReaderWidth - mPadding - batteryHeadSize - mBatteryWidth,
                mReaderHeight - mFooterHeight / 2 - batteryHeight / 2,
                mReaderWidth - mPadding - batteryHeadSize,
                mReaderHeight - mFooterHeight / 2 + batteryHeight / 2);
    }

    /**
     * 当前页在章节中的进度
     *
     * @param progress 进度
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }

    /**
     * 当前时间
     *
     * @param time 时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 电量百分比
     * @param electric
     */
    public void setBatteryLevel(float level) {
        this.batteryLevel = level;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        //画进度
        if (!TextUtils.isEmpty(progress)) {
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float titleY = (mReaderHeight + (mReaderHeight - mFooterHeight) - fontMetrics.top - fontMetrics.bottom) / 2;
            canvas.drawText(progress, mPadding, titleY, mPaint);
        }
        //画电池 STEP1: 电池头
        canvas.drawRoundRect(mBatteryHeadRectF, RADIUS, RADIUS, mPaint);
        //画电池 STEP2: 电池外壳
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.drawRoundRect(mBatteryRectF, RADIUS, RADIUS, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mBatteryRectF.left + mBatteryGap,
                mBatteryRectF.top + mBatteryGap,
                mBatteryRectF.left + mBatteryGap + (mBatteryInWidth * batteryLevel),
                mBatteryRectF.bottom - mBatteryGap,
                mPaint);
        //画时间
        if (!TextUtils.isEmpty(time)) {
            float timeWidth = mPaint.measureText(time, 0, time.length());
            float timeMargin = 20; //与右边电池的偏移量
            float x = mReaderWidth - mPadding - timeWidth - mBatteryWidth - timeMargin;
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float timeY = (mReaderHeight + (mReaderHeight - mFooterHeight) - fontMetrics.top - fontMetrics.bottom) / 2;
            canvas.drawText(time, x, timeY, mPaint);
        }
//        canvas.drawLine(0, mReaderHeight - mFooterHeight / 2, mReaderWidth, mReaderHeight - mFooterHeight / 2, mPaint);
        return true;
    }
}
