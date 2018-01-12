package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.data.LineData;
import me.sugarkawhi.mreader.data.PageData;

/**
 * 文字行部分：测量一行文字需要的字数；测量行高；绘制行文字；绘制笔记内容；测量每一个字在屏幕中的位置，用于笔记功能；
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class LineElement extends Element {

    private float mReaderWidth;
    private float mReaderHeight;
    private float mHeaderHeight;
    private float mFooterHeight;
    private float mPadding;
    private float mContentWidth;
    private float mContentHeight;
    private Paint mPaint;
    private int mColor;
    private float mTextSize = Config.DEFAULT_CONTENT_TEXTSIZE;
    private PageData mPageData;

    public LineElement(float headerHeight, float footerHeight, float padding) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColor = Color.BLACK;
        mPaint.setTextSize(mTextSize);
    }

    public void setPageData(PageData pageData) {
        mPageData = pageData;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mPaint.setTextSize(textSize);
    }

    public void setReaderSize(float readerWidth, float readerHeight) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        mContentWidth = readerWidth - 2 * mPadding;
        mContentHeight = readerHeight - mHeaderHeight - mFooterHeight;
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (mPageData == null) return;
        LineData lineData = mPageData.getLineData();
        if (lineData == null) return;
        List<String> lines = lineData.getLines();
        if (lines == null) return;
        float fontHeight = mPaint.getFontSpacing();
        float currentHeight = mHeaderHeight;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            currentHeight += fontHeight;
            if (i != 0) {
                if (line.contains("\n")) {
                    currentHeight += Config.DEFAULT_CONTENT_PARAGRAPH_SPACING;
                } else {
                    currentHeight += Config.DEFAULT_CONTENT_LINE_SPACING;
                }
            }
            canvas.drawText(line, mPadding, currentHeight, mPaint);
        }

    }

    public void setContentTextSize(float textSize) {
        mPaint.setTextSize(textSize);

    }

    public void setContentTextColor(int color) {
        mPaint.setColor(color);
    }
}
