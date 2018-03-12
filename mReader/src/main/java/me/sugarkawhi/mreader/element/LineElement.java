package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import me.sugarkawhi.mreader.data.LetterData;
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
    private Paint mTtsPanit;

    private List<LineData> mLineDataList;
    private List<LetterData> mLetterDataList;

    private int mBeginPos;
    private int mEndPos;
    private List<LetterData> mTtsLetters;

    public LineElement(float contentWidth, float contentHeight,
                       float headerHeight, float footerHeight, float padding,
                       Paint contentPaint, Paint chapterNamePaint) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        this.mContentPaint = contentPaint;
        this.mChapterNamePaint = chapterNamePaint;

        mTtsPanit = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTtsPanit.setStrokeWidth(10);
        mTtsPanit.setColor(Color.parseColor("#CDAA7D"));
    }

    public void setLineData(List<LineData> lineDataList) {
        this.mLineDataList = lineDataList;
    }

    public void setLetterData(List<LetterData> letterDataList) {
        this.mLetterDataList = letterDataList;
    }

    public void setTtsProgress(int beginPos, int endPos) {
        this.mBeginPos = beginPos;
        this.mEndPos = endPos;
    }


    /**
     * 画语音合成背景
     */
    public void drawTts(Canvas canvas) {
        if (mLetterDataList == null || mLetterDataList.isEmpty()) return;
        int size = mLetterDataList.size();
        if (mBeginPos == mEndPos) return;
        if (mBeginPos >= size || mEndPos >= size) return;
        int beginPos = mBeginPos;
        int endPos = mEndPos + 1;
        List<LetterData> ttsList = mLetterDataList.subList(beginPos, endPos);
        for (LetterData ttsLetter : ttsList) {
            if (ttsLetter.getLetter() == '　' || ttsLetter.getLetter() == '\n') continue;
            Rect area = ttsLetter.getArea();
//            area.left = (int) (area.left + mPadding);
//            area.right = (int) (area.right + mPadding);
//            area.top = (int) (area.top + mHeaderHeight);
//            area.bottom = (int) (area.bottom + mHeaderHeight);
            canvas.drawRect(area.left + mPadding,
                    area.top + mHeaderHeight,
                    (area.right + mPadding),
                    (area.bottom + mHeaderHeight),
                    mTtsPanit);
//            canvas.drawRect(area,mTtsPanit);
        }
    }

    /**
     * 画语音合成背景
     */
    public void drawTtsLetters(Canvas canvas) {
        if (mTtsLetters == null || mTtsLetters.isEmpty()) return;
        for (LetterData ttsLetter : mTtsLetters) {
            if (ttsLetter.getLetter() == '　' || ttsLetter.getLetter() == '\n') continue;
            Rect area = ttsLetter.getArea();
            canvas.drawRect(area.left + mPadding,
                    area.top + mHeaderHeight,
                    (area.right + mPadding),
                    (area.bottom + mHeaderHeight),
                    mTtsPanit);
        }
    }

    @Override
    public boolean onDraw(Canvas canvas) {
//        if (mLineDataList == null) return false;
//        for (int i = 0; i < mLineDataList.size(); i++) {
//            LineData lineData = mLineDataList.get(i);
//            if (lineData == null) continue;
//            //画单个字符
//            List<LetterData> letterDataList = lineData.getLetters();
//            if (letterDataList != null) {
//                for (LetterData letter : letterDataList) {
//                    String str = String.valueOf(letter.getLetter());
//                    float x = letter.getOffsetX() + mPadding;
//                    float y = lineData.getOffsetY() + mHeaderHeight;
//                    if (lineData.isChapterName()) {
//                        canvas.drawText(str, x, y, mChapterNamePaint);
//                    } else {
//                        canvas.drawText(str, x, y, mContentPaint);
//                    }
//                }
//            }
//        }
            drawTtsLetters(canvas);
            if (mLetterDataList == null) return false;
            for (LetterData letter : mLetterDataList) {
                String str = String.valueOf(letter.getLetter());
                float x = letter.getOffsetX() + mPadding;
                float y = letter.getOffsetY() + mHeaderHeight;
                if (letter.isChapterName()) {
                    canvas.drawText(str, x, y, mChapterNamePaint);
                } else {
                    canvas.drawText(str, x, y, mContentPaint);
                }
        }

        return true;
    }

    public void setTtsLetters(List<LetterData> ttsLetters) {
        mTtsLetters = ttsLetters;
    }

    public void stopTts() {
        if (mTtsLetters != null) mTtsLetters.clear();
    }
}
