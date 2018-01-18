package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
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
    private Paint mContentPaint;
    private Paint mChapterNamePaint;

    private PageData mPageData;

    public LineElement(float contentWidth, float contentHeight,
                       float headerHeight, float footerHeight,
                       float padding, float lineSpacing,
                       Paint contentPaint, Paint chapterNamePaint) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        this.mLineSpacing = lineSpacing;
        this.mContentPaint = contentPaint;
        this.mChapterNamePaint = chapterNamePaint;
    }

    public void setPageData(PageData pageData) {
        mPageData = pageData;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mPageData == null) return;
        List<LineData> lines = mPageData.getLines();
        if (lines == null) return;

        for (int i = 0; i < lines.size(); i++) {
            LineData lineData = lines.get(i);
            for (LineData.LetterData letter : lineData.getLetters()) {
                String str = String.valueOf(letter.getLetter());
                float x = letter.getOffsetX() + mPadding;
                float y = lineData.getOffsetY() + mHeaderHeight;
                if (lineData.isChapterName()) {
                    canvas.drawText(str, x, y, mChapterNamePaint);
                } else {
                    canvas.drawText(str, x, y, mContentPaint);
                }
            }
        }
    }
}
