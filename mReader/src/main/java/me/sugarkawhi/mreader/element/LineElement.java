package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import me.sugarkawhi.mreader.data.LineData;

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
    private Paint mContentPaint;
    private Paint mChapterNamePaint;

    private List<LineData> mLineDataList;

    public LineElement(float contentWidth, float contentHeight,
                       float headerHeight, float footerHeight,
                       float padding, float lineSpacing,
                       Paint contentPaint, Paint chapterNamePaint) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        this.mContentPaint = contentPaint;
        this.mChapterNamePaint = chapterNamePaint;
    }

    public void setLineData(List<LineData> lineDataList) {
        this.mLineDataList = lineDataList;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
//        mContentPaint.setColor(Color.parseColor("#abcdef"));
//        canvas.drawRect(mPadding, mHeaderHeight, mPadding + mContentWidth, mHeaderHeight + mContentHeight, mContentPaint);
//        mContentPaint.setColor(Color.BLACK);
        if (mLineDataList == null) return false;
        for (int i = 0; i < mLineDataList.size(); i++) {
            LineData lineData = mLineDataList.get(i);
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
        return true;
    }
}
