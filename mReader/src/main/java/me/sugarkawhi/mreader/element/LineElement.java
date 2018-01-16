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

    private float mHeaderHeight;
    private float mFooterHeight;
    private float mPadding;
    private float mContentWidth;
    private float mContentHeight;
    private float mLineSpacing = Config.DEFAULT_CONTENT_LINE_SPACING;
    private float mParagraphSpacing = Config.DEFAULT_CONTENT_PARAGRAPH_SPACING;
    private Paint mPaint;

    private PageData mPageData;

    public LineElement(float contentWidth, float contentHeight, float headerHeight, float footerHeight, float padding, float lineSpacing, float paragraphSpacing, Paint paint) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        this.mLineSpacing = lineSpacing;
        this.mParagraphSpacing = paragraphSpacing;
        this.mPaint = paint;
    }

    public void setPageData(PageData pageData) {
        mPageData = pageData;
    }

    @Override
    public void onDraw(Canvas canvas) {
//        mPaint.setColor();
//        canvas.drawRect(mPadding, mHeaderHeight, mPadding + mContentWidth, mHeaderHeight + mContentHeight, mPaint);
        mPaint.setColor(Color.BLACK);
        if (mPageData == null) return;
        List<String> lines = mPageData.getLines();
        if (lines == null) return;
        float fontHeight = mPaint.getFontSpacing();
        float currentHeight = mHeaderHeight + fontHeight;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            //单独一行是换行 就不画了 避免段落间距多大
            if (!line.equals("\n")) {
                canvas.drawText(line, mPadding, currentHeight, mPaint);
                currentHeight += fontHeight;
            }
            if (line.endsWith("\n")) {
                currentHeight += mParagraphSpacing;
                if (line.equals("\n")) {
                    currentHeight -= mLineSpacing;
                }
            } else {
                currentHeight += mLineSpacing;
            }
        }
    }
}
