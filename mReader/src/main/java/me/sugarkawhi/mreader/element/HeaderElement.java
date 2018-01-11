package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
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
    private Paint.FontMetrics mFontMetrics;

    public HeaderElement(float headerHeight, float padding, float textSize) {
        super();
        this.mHeaderHeight = headerHeight;
        this.mPadding = padding;
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mFontMetrics = mPaint.getFontMetrics();
    }

    public void setTextColor(int textColor) {
        mPaint.setColor(textColor);
    }

    public void setChapterTitle(String chapterTitle) {
        mChapterTitle = chapterTitle;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(mChapterTitle)) return;
        mPaint.measureText(mChapterTitle);
        float fontHeight = Math.abs(mFontMetrics.ascent + mFontMetrics.descent);
        canvas.drawText(mChapterTitle, mPadding, mHeaderHeight + (fontHeight / 2), mPaint);
    }

}
