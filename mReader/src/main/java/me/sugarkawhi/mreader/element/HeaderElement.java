package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import me.sugarkawhi.mreader.config.Config;

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
    private int mTextColor = Config.DEFAULT_COLOR;

    public HeaderElement(float headerHeight, float padding, float textSize) {
        this.mHeaderHeight = headerHeight;
        this.mPadding = padding;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        mFontMetrics = mPaint.getFontMetrics();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mPaint.setColor(textColor);
    }

    public void setChapterName(String chapterTitle) {
        mChapterTitle = chapterTitle;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(mChapterTitle)) return;
        if (Config.DEBUG) {
            mPaint.setColor(Color.YELLOW);
            canvas.drawRect(0, 0, 500, mHeaderHeight, mPaint);
            mPaint.setColor(mTextColor);
        }
        mPaint.measureText(mChapterTitle);
        float titleHeight = Math.abs(mFontMetrics.descent + mFontMetrics.ascent);
        canvas.drawText(mChapterTitle, mPadding, mHeaderHeight / 2 + (titleHeight / 2), mPaint);
    }

}
