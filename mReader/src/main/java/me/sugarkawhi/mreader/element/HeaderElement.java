package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

/**
 * 页头部分：显示章节的标题；绘制每一页的头部。
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class HeaderElement extends Element {

    private float mHeaderHeight;
    private float mPadding;
    private String mChapterTitle;
    private Paint mPaint;

    public HeaderElement(float headerHeight, float padding, Paint paint) {
        this.mHeaderHeight = headerHeight;
        this.mPadding = padding;
        this.mPaint = paint;
    }

    public void setChapterName(String chapterTitle) {
        mChapterTitle = chapterTitle;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(mChapterTitle)) return false;
        mPaint.measureText(mChapterTitle);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float titleY = (mHeaderHeight - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(mChapterTitle, mPadding, titleY, mPaint);
//        canvas.drawLine(0, mHeaderHeight / 2, 1080, mHeaderHeight / 2, mPaint);
        return true;
    }

}
